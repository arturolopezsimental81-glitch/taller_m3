# Evidencias: capturas del flujo

Las capturas se tomaron el 2026-09-24 con VetTurno corriendo de forma local contra MySQL 8(AppServ) en el puerto 3306 con la base `vetturno`. El flujo se hizo desde Swagger UI en Microsoft Edge, los tokens aparecen recortados a sus primeros 16 caracteres con la marca `(oculto)`.

| # | Captura | Qué se ve |
|---|---|---|
| 00 | [Árbol de paquetes](capturas/00-arbol-paquetes.png) | Paquete base `com.huellitas.vetturno` con `VetTurnoApplication.java` y los paquetes config, controller, dto, exception, model, repository, security y service. |
| 01 | [Servidor iniciado](capturas/01-servidor-iniciado.png) | Terminal con `.\mvnw.cmd spring-boot:run`, el SQL de Hibernate que crea la llave foránea de mascota y el mensaje `Started VetTurnoApplication`, Tomcat queda en el puerto 8080. |
| 02 | [Registro de Paula, 200](capturas/02-registro-paula-200.png) | `POST /api/auth/register` donde el cliente manda `"rol": "ADMIN"` y la API responde 200 con token, la cuenta se guarda como USER. |
| 03 | [Login de ADMIN, 200](capturas/03-login-admin-token-oculto.png) | `POST /api/auth/login` de Doña Marta(después de pasarla a ADMIN con el `UPDATE` en la base), la respuesta trae el JWT parcialmente oculto. |
| 04 | [Authorize](capturas/04-authorize-bearer.png) | Diálogo Authorize de Swagger con el esquema `bearerAuth` autorizado y el valor oculto como `******`. |
| 05 | [Veterinario con ADMIN, 201](capturas/05-veterinario-admin-201.png) | `POST /api/veterinarios` con el token de Marta, responde 201 y se crea Andrés Ruiz. |
| 06 | [Veterinario con USER, 403](capturas/06-veterinario-user-403.png) | El mismo `POST /api/veterinarios` con el token de Paula responde 403 con el ApiError "No tienes permiso para realizar esta operación". |
| 07 | [Propietario inválido, 400](capturas/07-propietario-400-errores.png) | `POST /api/propietarios` con nombre vacío, teléfono y email inválidos, responde 400 con un error por cada campo. |
| 08 | [Propietario, 201](capturas/08-propietario-201.png) | `POST /api/propietarios` válido, responde 201 con el DTO sin colecciones anidadas. |
| 09 | [Mascota, 201](capturas/09-mascota-201.png) | `POST /api/mascotas` con un propietario que existe, responde 201 con `propietarioId` y `propietarioNombre`. |
| 10 | [Cita, 201](capturas/10-cita-201.png) | `POST /api/citas` con fecha futura, mascota y veterinario que existen, responde 201 con el CitaDTO plano. |
| 11 | [Cita cruzada, 400](capturas/11-cita-cruce-400.png) | Otra cita para el mismo veterinario a la misma hora, responde 400 con "El veterinario Andrés Ruiz ya tiene una cita en ese horario". |
| 12 | [Cita con fecha pasada, 400](capturas/12-cita-fecha-pasada-400.png) | `POST /api/citas` con fecha de 2024, responde 400 porque la fecha tiene que ser futura. |
| 13 | [Agenda por veterinario, 200](capturas/13-agenda-veterinario-200.png) | `GET /api/citas/veterinario/1` regresa solo las citas de Andrés Ruiz. |
| 14 | [Sin token, 401](capturas/14-citas-sin-token-401.png) | `GET /api/citas` después de hacer Logout en Authorize, responde 401 y pide iniciar sesión. |
| 15 | [MySQL](capturas/15-mysql-consola.png) | Consola de MySQL con las llaves foráneas(`cita.mascota_id`, `cita.veterinario_id` y `mascota.propietario_id`), la tabla `usuario` con las contraseñas guardadas como hash de BCrypt con el rol como texto, al final las citas guardadas. |
| 16 | [Citas después de reiniciar, 200](capturas/16-citas-despues-reinicio-200.png) | Se detuvo y se volvió a iniciar el servidor, con el token nuevo de Paula `GET /api/citas` regresa las 2 citas que se guardaron antes del reinicio. |
