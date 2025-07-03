package com.hazelcast.example;

import java.lang.reflect.Constructor;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastExample {

    public static void main(String[] args) throws Exception {
        // Hazelcast client yapılandırması
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("127.0.0.1:5701");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<Integer, Person> map = client.getMap("persons");

        // 10.000 Person nesnesini map'e ekle
        for (int i = 1; i <= 10_000; i++) {
            Person p = createPerson("Person-" + i);
            map.put(i, p);
        }

        // 1000'er atlayarak bazı nesneleri oku ve yazdır
        for (int i = 1; i <= 10_000; i += 1000) {
            System.out.println("Retrieved: " + map.get(i));
        }

        client.shutdown();
    }

    // Private constructor olan Person nesnesini reflection ile yarat
    private static Person createPerson(String name) {
        try {
            Constructor<Person> constructor = Person.class.getDeclaredConstructor();
            constructor.setAccessible(true); // private constructor erişilebilir oldu
            Person p = constructor.newInstance();
            p.setName(name);
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
