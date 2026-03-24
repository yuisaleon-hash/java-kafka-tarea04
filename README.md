KAFKA DEMO - README (INTELLIJ IDEA)

========================================
DESCRIPCIÓN
===========

Este proyecto es una demo de mensajería distribuida usando Apache Kafka.
Permite enviar y recibir mensajes entre diferentes tipos de usuarios:

* Alumnos
* Profesores
* Directores

Utiliza:

* Producer (Java)
* Consumer (Java)
* Frontend (HTML)
* Docker para la infraestructura

========================================
REQUISITOS
==========

Antes de ejecutar el proyecto debes tener instalado:

* Java 11
* Maven
* Docker
* Docker Compose

Verificar instalación:

java -version
mvn -version
docker -v
docker-compose -v

========================================
PASOS PARA EJECUTAR EL PROYECTO
===============================

1. ABRIR EL PROYECTO EN INTELLIJ

* Abrir IntelliJ IDEA
* Seleccionar "Open"
* Elegir la carpeta del proyecto
* Esperar a que Maven cargue dependencias

---

2. COMPILAR EL PROYECTO

---

Abrir terminal dentro de IntelliJ o usar terminal del sistema:

mvn clean package

Esto generará los archivos:

target/

* producer-jar-with-dependencies.jar
* consumer-jar-with-dependencies.jar

---

3. LEVANTAR DOCKER

---

Ejecutar:

docker-compose up

O en segundo plano:

docker-compose up -d

---

4. ESPERAR INICIALIZACIÓN

---

Esperar unos segundos mientras:

* Kafka inicia
* Se crean los tópicos automáticamente
* Producer y Consumer se conectan

---

5. ABRIR INTERFAZ WEB

---

Abrir el archivo:

index.html

(en el navegador)

---

6. USAR EL SISTEMA

---

* Seleccionar tipo de usuario:

  * Alumnos
  * Profesores
  * Directores

* Escribir mensaje

* Presionar "Enviar"

Los mensajes aparecerán automáticamente.

========================================
ENDPOINTS
=========

Producer:
POST http://localhost:8082/enviar?topico=TOPICO_ALUMNOS

Consumer:
GET http://localhost:8081/mensajes

========================================
SERVICIOS DOCKER
================

* zookeeper
* kafka
* kafka-ui (http://localhost:8080)
* kafka-setup (crea tópicos)
* kafka-producer (puerto 8082)
* kafka-consumer (puerto 8081)

========================================
COMANDOS ÚTILES
===============

Ver logs:
docker-compose logs -f

Ver contenedores:
docker ps

Detener todo:
docker-compose down

Reiniciar:
docker-compose down
docker-compose up -d

========================================
PROBLEMAS COMUNES
=================

ERROR: No se generan JAR
Solución:
mvn clean package

ERROR: Kafka no arranca
Solución:
Verificar que Docker esté activo

ERROR: No se ven mensajes
Solución:
docker-compose logs

ERROR: Puertos ocupados
Solución:
Cambiar puertos en docker-compose.yml

========================================
NOTAS
=====

* Los mensajes se almacenan en memoria
* No hay persistencia en base de datos
* Se usa polling (no WebSockets)

========================================
FIN
===
