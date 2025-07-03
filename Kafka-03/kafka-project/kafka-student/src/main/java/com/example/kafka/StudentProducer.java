package com.example.kafka;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class StudentProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Gson gson = new Gson();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            Student student = new Student(1, "Alice");
            String json = gson.toJson(student);

            ProducerRecord<String, String> record = new ProducerRecord<>("student-topic", json);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Sent: " + json);
                } else {
                    exception.printStackTrace();
                }
            });

            producer.flush();
        } // try-with-resources bloğu producer'ı otomatik olarak kapatır
    }
}

