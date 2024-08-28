package com.dv.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public final class ModelHelper {

    private ModelHelper() {
    }

    /**
     * Jackson object mapper that excludes null fields and map entries
     * with null values.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <K, V> Map.Entry<K, V> entry(final K key, final V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> constructMap(final Map.Entry<K, V>... entries) {
        return Collections.unmodifiableMap(Arrays
                .stream(entries)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    /**
     * Serialises an object to JSON, excluding null fields and map entries with null values.
     *
     * @param object the object to serialise
     * @param <T>    type of the object
     * @return JSON seralisation of the object, or "{}" on error
     */
    public static <T> String toJson(final T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            final String message = String.format("Error serialising object of class %s JSON",
                    object.getClass().getName());
            log.warn(message, e);
            return "{}";
        }
    }

    /**
     * De-serialises JSON to an object.
     *
     * @param json        JSON serialisation of the object
     * @param objectClass class of the object to create
     * @param <T>         type of the returned object
     * @return an instance of the de-serialised object, or null on error
     */
    public static <T> T fromJson(final String json, final Class<T> objectClass) {
        try {
            return MAPPER.readValue(json, objectClass);
        } catch (Exception e) {
            final String message = String.format("Error de-serialising JSON '%s' to object of %s",
                    json, objectClass.getName());
            log.warn(message, e);
            return null;
        }
    }

    /**
     * Clones an object by serialising to JSON and back again.
     *
     * @param object      a JSON-serialisable object to clone
     * @param objectClass class of the object
     * @return a clone of the object
     */
    public static <T> T clone(final T object, final Class<T> objectClass) {
        return fromJson(toJson(object), objectClass);
    }


}
