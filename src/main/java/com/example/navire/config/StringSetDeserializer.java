package com.example.navire.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StringSetDeserializer extends JsonDeserializer<Set<String>> {
    
    @Override
    public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Set<String> result = new HashSet<>();
        
        JsonToken currentToken = p.getCurrentToken();
        
        // Si c'est un tableau JSON
        if (currentToken == JsonToken.START_ARRAY) {
            while (p.nextToken() != JsonToken.END_ARRAY) {
                String value = p.getValueAsString();
                if (value != null && !value.trim().isEmpty()) {
                    result.add(value.trim());
                }
            }
        }
        // Si c'est un objet JSON (ne devrait pas arriver normalement)
        else if (currentToken == JsonToken.START_OBJECT) {
            // Skip l'objet
            p.skipChildren();
        }
        // Si c'est une valeur unique
        else if (currentToken == JsonToken.VALUE_STRING) {
            String value = p.getValueAsString();
            if (value != null && !value.trim().isEmpty()) {
                result.add(value.trim());
            }
        }
        
        return result;
    }
}
