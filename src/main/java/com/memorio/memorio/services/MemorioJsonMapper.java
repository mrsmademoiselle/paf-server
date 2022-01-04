package com.memorio.memorio.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provisorische Mapperklasse, die wir eventuell später noch brauchen, um uns die anderen Klassen nicht mit Mapping zuzumüllen
 */
public class MemorioJsonMapper {

    public static Logger logger = LoggerFactory.getLogger(MemorioJsonMapper.class);

    public static Map<String, String> getMapFromString(String s) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // das muss eine LinkedHashMap bleiben, damit die Reihenfolge der JSON-Werte nicht variiert!
        LinkedHashMap<String, String> map = mapper.readValue(s, LinkedHashMap.class);

        logger.info("JSON wurde erfolgreich in Map<String, String> geparsed: " + s);
        return map;
    }

    public static String getStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}