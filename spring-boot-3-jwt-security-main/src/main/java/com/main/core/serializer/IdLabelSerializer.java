package com.main.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.main.core.annotation.Label;
import com.main.core.entity.BaseEntity;

import java.io.IOException;
import java.lang.reflect.Field;

public class IdLabelSerializer extends JsonSerializer<BaseEntity> {

    @Override
    public void serialize(BaseEntity value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeStartObject();
        gen.writeNumberField("id",
                value.getId());
        gen.writeStringField("label",
                value.getLabel());
        gen.writeEndObject();
    }
}

