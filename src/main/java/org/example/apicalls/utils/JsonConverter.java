package org.example.apicalls.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.api.data.entity.Card;

import java.io.IOException;
import java.util.List;

public class JsonConverter {

    private final ObjectMapper objectMapper;

    public JsonConverter() {
        // Configura ObjectMapper según sea necesario
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public String convertListToJson(List<Card> cardsList) {
        try {
            return objectMapper.writeValueAsString(cardsList);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}"; // Retorna un JSON vacío en caso de error
        }
    }
}
