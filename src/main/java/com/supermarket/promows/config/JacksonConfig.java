package com.supermarket.promows.config;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        // Configuração mínima e suficiente para UTF-8
        mapper.getFactory().setCharacterEscapes(new JacksonConfig.DefaultCharacterEscapes());
        
        return mapper;
    }

    // Classe interna para tratamento de caracteres
    private static class DefaultCharacterEscapes extends com.fasterxml.jackson.core.io.CharacterEscapes {
        private final int[] asciiEscapes;
        
        public DefaultCharacterEscapes() {
            asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        }
        
        @Override
        public int[] getEscapeCodesForAscii() {
            return asciiEscapes;
        }
        
        @Override
        public SerializableString getEscapeSequence(int ch) {
            return null; // Usa escapes padrão
        }
    }
}