package org.chomookun.arch4j.core.common.data.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.support.ObjectMapperHolder;

@Converter
@Slf4j
public class GenericObjectConverter<T> implements AttributeConverter<T, String> {

    private final Class<T> classType;

    protected GenericObjectConverter(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = ObjectMapperHolder.getObject();
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception ignore) {
            log.warn(ignore.getMessage());
            return null;
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = ObjectMapperHolder.getObject();
            return objectMapper.readValue(dbData, classType);
        } catch (Exception ignore) {
            log.warn(ignore.getMessage());
            return null;
        }
    }

}
