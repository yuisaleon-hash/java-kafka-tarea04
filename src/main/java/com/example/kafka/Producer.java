package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class Producer {

    public static void main(String[] args) {

        // 🔧 Configuración Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        // Producer + consola
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("🚀 Producer iniciado. Escribe mensajes:");

            while (true) {
                String message = scanner.nextLine();

                ProducerRecord<String, String> record =
                        new ProducerRecord<>("test-topic", message);

                producer.send(record);

                System.out.println("✅ Enviado: " + message);
            }
        }
    }
}