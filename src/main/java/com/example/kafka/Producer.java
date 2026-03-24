package com.example.kafka;

import com.sun.net.httpserver.HttpServer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

public class Producer {
    private static final String TOPIC_ALUMNOS    = "TOPICO_ALUMNOS";
    private static final String TOPIC_PROFESORES = "TOPICO_PROFESORES";
    private static final String TOPIC_DIRECTORES = "TOPICO_DIRECTORES";
    static KafkaProducer<String, String> producer;

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);

        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/enviar", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String topico = query != null && query.startsWith("topico=") ? query.substring(7) : "";
                InputStream is = exchange.getRequestBody();
                String mensaje = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                if (!topico.isEmpty() && !mensaje.isEmpty()) {
                    producer.send(new ProducerRecord<>(topico, mensaje));
                    System.out.println("[WEB] Enviado a [" + topico + "]: " + mensaje);
                    byte[] resp = "OK".getBytes();
                    exchange.sendResponseHeaders(200, resp.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(resp);
                    }
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    exchange.getResponseBody().close();
                }
            } else {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
            }
        });
        server.start();
        System.out.println("Producer web iniciado en http://localhost:8082");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Producer iniciado.");
        while (true) {
            System.out.println("\nSelecciona el topico:");
            System.out.println("  1 - TOPICO_ALUMNOS");
            System.out.println("  2 - TOPICO_PROFESORES");
            System.out.println("  3 - TOPICO_DIRECTORES");
            System.out.println("  0 - Salir");
            System.out.print("Opcion: ");
            String opcion = scanner.nextLine().trim();
            if (opcion.equals("0")) break;
            String topic;
            switch (opcion) {
                case "1": topic = TOPIC_ALUMNOS;    break;
                case "2": topic = TOPIC_PROFESORES; break;
                case "3": topic = TOPIC_DIRECTORES; break;
                default: System.out.println("Opcion invalida."); continue;
            }
            System.out.print("Escribe tu mensaje: ");
            String message = scanner.nextLine();
            producer.send(new ProducerRecord<>(topic, message));
            System.out.println("Enviado a [" + topic + "]: " + message);
        }
        producer.close();
    }
}