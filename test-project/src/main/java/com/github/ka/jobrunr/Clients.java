package com.github.ka.jobrunr;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Clients {

    public static record Client(String name) {}

    public List<Client> findAllClients() {
        return IntStream.range(0, 20)
                .mapToObj(i -> new Client("client" + i))
                .collect(Collectors.toList());
    }
}
