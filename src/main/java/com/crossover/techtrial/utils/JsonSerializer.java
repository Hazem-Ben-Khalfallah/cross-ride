package com.crossover.techtrial.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JsonSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

    final ObjectMapper mapper;

    public JsonSerializer(@Qualifier("CustomObjectMapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @param object object to serialize to json
     * @return json string
     * @should generate Json String from object
     */
    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            LOGGER.error(String.format("Error when serializing %s to json", object.getClass().getName()), ex);
        }
        return null;
    }
}