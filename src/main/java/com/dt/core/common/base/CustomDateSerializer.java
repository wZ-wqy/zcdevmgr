package com.dt.core.common.base;

import java.time.LocalDateTime;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
public class CustomDateSerializer extends JsonSerializer<LocalDateTime>  {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException {
        if (value != null) {
            jgen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}
