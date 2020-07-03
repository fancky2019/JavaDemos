package Test.opensource.Jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JacksonLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime date, JsonGenerator generator, SerializerProvider arg) throws IOException,
            JsonProcessingException {
        final String dateString = date.format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        generator.writeString(dateString);
    }
}

