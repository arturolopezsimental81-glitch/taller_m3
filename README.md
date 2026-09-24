# VetTurno

VetTurno es la agenda digital de **Veterinaria Huellitas**.

## Historia

Veterinaria Huellitas es una clínica de barrio que abrió Doña Marta hace seis años, donde atiende junto al doctor Andrés y a Paula(quien recibe las llamadas y organiza las citas). La agenda todavía se lleva entre un cuaderno y conversaciones de WhatsApp, esto provoca que a veces se reserven dos consultas para el mismo veterinario a la misma hora o se pierda el teléfono de algún responsable.

El objetivo de VetTurno es poder ordenar la agenda mediante una API REST donde se registran responsables, mascotas y veterinarios, asimismo se agendan citas sin cruces de horario para consultar la agenda de forma clara sin perder información.

## Tecnologías

Java 17, Spring Boot 4.1(Web MVC, Data JPA, Security y Validation), Maven con el wrapper incluido y MySQL 8.

## Cómo ejecutar

1. Crear la base de datos vacía en MySQL:

   ```sql
   CREATE DATABASE vetturno;
   ```

2. Copiar `src/main/resources/application-local.properties.example` como `application-local.properties` en la misma carpeta y poner el usuario y la contraseña de MySQL.

3. Iniciar la aplicación:

   ```bash
   .\mvnw.cmd spring-boot:run    # Windows
   ./mvnw spring-boot:run        # macOS / Linux
   ```

La API queda disponible en `http://localhost:8080`.
