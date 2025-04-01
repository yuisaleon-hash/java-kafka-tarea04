// Producer.java
package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class Producer {
    private static final String BOOTSTRAP_SERVERS = "kafka:9092";
    private static final String TOPIC = "demo-topic";

    public static void main(String[] args) {
        // Configurar propiedades del productor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Crear el productor
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Enviar mensajes
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== KAFKA PRODUCER DEMO =====");
        System.out.println("Escribe mensajes para enviar a Kafka (escribe 'exit' para salir):");

        String message;
        while (true) {
            System.out.print("> ");
            message = scanner.nextLine();

            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            // Crear el registro a enviar
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);

            // Enviar el mensaje
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Mensaje enviado: " +
                            "Topic: " + metadata.topic() + ", " +
                            "Partition: " + metadata.partition() + ", " +
                            "Offset: " + metadata.offset() + ", " +
                            "Timestamp: " + metadata.timestamp());
                } else {
                    System.err.println("Error al enviar mensaje: " + exception.getMessage());
                }
            });

            // Forzar el envío
            producer.flush();
        }

        // Cerrar el productor
        producer.close();
        scanner.close();
        System.out.println("Productor cerrado correctamente.");
    }
}