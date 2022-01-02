package com.memorio.memorio.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provisorische Mapperklasse, die wir eventuell später noch brauchen, um uns die anderen Klassen nicht mit Mapping zuzumüllen
 */
public class MemorioJsonMapper {

    public static Map<String, String> getMapFromString(String s) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(s);
        // das muss eine LinkedHashMap bleiben, damit die Reihenfolge der JSON-Werte nicht variiert!
        LinkedHashMap<String, String> map = mapper.readValue(s, LinkedHashMap.class);
        return map;
    }

    public static String getStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}