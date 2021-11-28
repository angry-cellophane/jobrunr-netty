package com.github.ka.jobrunr;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SomeService {

    void serve() {
        anotherMethod2();
    }

    static void anotherMethod2() {
        anotherMethod3();
    }

    static void anotherMethod3() {
        anotherMethod4();
    }

    static void anotherMethod4() {
        anotherMethod5();
    }

    static void anotherMethod5() {
        anotherMethod6();
    }

    static void anotherMethod6() {
        Integer i = ThreadLocalRandom.current().nextBoolean() ? 1 : null;
        Objects.requireNonNull(i, "number is null");
    }
}
