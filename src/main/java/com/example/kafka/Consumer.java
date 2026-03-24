package com.example.kafka;

import com.sun.net.httpserver.HttpServer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Consumer {
    static final List<String> mensajes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/mensajes", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            StringBuilder json = new StringBuilder("[");
            synchronized (mensajes) {
                for (int i = 0; i < mensajes.size(); i++) {
                    json.append("\"").append(mensajes.get(i).replace("\"", "'")).append("\"");
                    if (i < mensajes.size() - 1) json.append(",");
                }
            }
            json.append("]");
            byte[] response = json.toString().getBytes();
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        });
        server.start();
        System.out.println("Consumer web iniciado en http://localhost:8081");

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "grupo-1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("TOPICO_ALUMNOS", "TOPICO_PROFESORES", "TOPICO_DIRECTORES"));
        System.out.println("Consumer escuchando los 3 topicos...");

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                String msg = "[" + record.topic() + "] -> " + record.value();
                System.out.println("Mensaje recibido: " + msg);
                synchronized (mensajes) {
                    mensajes.add(msg);
                }
            }
        }
    }
}