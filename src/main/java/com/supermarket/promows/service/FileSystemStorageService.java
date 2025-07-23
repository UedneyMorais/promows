package com.supermarket.promows.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.supermarket.promows.config.StorageProperties;
import com.supermarket.promows.exception.StorageException;
import com.supermarket.promows.exception.StorageFileNotFoundException;
import com.supermarket.promows.repository.StorageService;
import com.supermarket.promows.utils.SlugUtil;

@Service
public class FileSystemStorageService implements StorageService {

	@Value("${server.address}")
	private String serverHost;

	@Value("${server.port}")
	private int serverPort;

    private final Path rootLocation;

	public FileSystemStorageService(StorageProperties properties) {
        
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
	}

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

	@Override
	public String store(MultipartFile file) {
	    try {
	        if (file.isEmpty()) {
	            throw new StorageException("Failed to store empty file.");
	        }

	        // Gera nome seguro
	        String safeFilename = SlugUtil.slugifyFileName(file.getOriginalFilename());

	        Path destinationFile = this.rootLocation.resolve(Paths.get(safeFilename))
	                .normalize().toAbsolutePath();

	        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
	            throw new StorageException("Cannot store file outside current directory.");
	        }

	        try (InputStream inputStream = file.getInputStream()) {
	            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
	        }

	        return safeFilename;

	    } catch (IOException e) {
	        throw new StorageException("Failed to store file.", e);
	    }
	}

    @Override
    public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
    }

    @Override
    public Path load(String filename) {
       return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }


	String getUrl(String filename) {
		if (filename == null || filename.startsWith("http") || filename.startsWith("https") || filename.startsWith("192.168"))
		return filename;
		
		String finalUrl = "http://"+serverHost + ":" + serverPort + "/uploads/" + filename;
		return finalUrl;
	}
    
}
