package com.github.ka;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.utils.mapper.jackson.modules.JobRunrModule;
import org.jobrunr.utils.mapper.jackson.modules.JobRunrTimeModule;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class JacksonTest {

    static class FluxSerializer extends StdSerializer<Flux> {

        public FluxSerializer() {
            super(Flux.class);
        }

        @Override
        public void serialize(Flux value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartArray();
            value.doOnComplete(() -> {
                try {
                    gen.writeEndArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).subscribe(v -> {
                try {
                    gen.writePOJO(v);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void test() throws JsonProcessingException {
        var mapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new JobRunrTimeModule())
                .registerModule(new JobRunrModule())
                .registerModule(new SimpleModule() {{
                    addSerializer(Flux.class, new FluxSerializer());
                }})
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"))
                .activateDefaultTypingAsProperty(LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS,
                        "@class");

//        log.info(new JacksonJsonMapper().serialize(List.of(1, 2, 3)));
//        log.info(new JacksonJsonMapper().serialize(new int[]{1, 2, 3}));
//        log.info(mapper.writeValueAsString(List.of(1, 2, 3)));
        log.info(mapper.writeValueAsString(Flux.fromIterable(List.of(1, 2, 3))));
    }
}
