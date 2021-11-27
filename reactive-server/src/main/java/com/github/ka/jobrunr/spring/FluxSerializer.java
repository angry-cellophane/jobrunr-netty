package com.github.ka.jobrunr.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import reactor.core.publisher.Flux;

import java.io.IOException;

final class FluxSerializer extends StdSerializer<Flux> {

    FluxSerializer() {
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
