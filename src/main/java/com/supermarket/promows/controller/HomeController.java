package com.supermarket.promows.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serve a página inicial na raiz. Com EnableWebMvc, forward para ficheiros estáticos
 * não funciona; devolve-se o HTML via ClassPathResource.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Resource> root() {
        Resource body = new ClassPathResource("static/index.html");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .body(body);
    }
}
