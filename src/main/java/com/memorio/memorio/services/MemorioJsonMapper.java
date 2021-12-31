package com.memorio.memorio.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Provisorische Mapperklasse, die wir eventuell später noch brauchen, um uns die anderen Klassen nicht mit Mapping zuzumüllen
 */
public class MemorioJsonMapper {

    public static Map<String, String> getMapFromString(String s) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = mapper.readValue(s, HashMap.class);
        return map;
    }
}