# Evidencias: matriz de pruebas manuales

Las pruebas se corrieron el 2026-09-24 contra VetTurno de forma local(`java -jar target/vetturno-0.0.1-SNAPSHOT.jar`) con MySQL 8, la base `vetturno` estaba vacía al inicio. Las peticiones se hicieron por HTTP a `http://localhost:8080` mientras que lo que se revisó en MySQL se hizo con consultas `SELECT`, los tokens aparecen recortados a sus primeros 16 caracteres, las contraseñas no se muestran.

## Resumen

| # | Escenario | Resultado esperado | Estado HTTP obtenido | Resultado |
|---|---|---|---|---|
| 1 | La aplicación inicia con MySQL disponible. | Servidor activo y esquema accesible. | 200 | Aprobada |
| 2 | Registro válido de Paula. | 200 y token; contraseña hasheada. | 200 | Aprobada |
| 3 | Registro con email inválido y clave corta. | 400 con errores por campo. | 400 | Aprobada |
| 4 | Login con credenciales válidas. | 200 y JWT vigente. | 200 | Aprobada |
| 5 | GET /api/citas sin token. | Acceso rechazado. | 401 | Aprobada |
| 6 | POST /api/veterinarios con USER. | 403 Forbidden. | 403 | Aprobada |
| 7 | POST /api/veterinarios con ADMIN. | 201 y veterinario persistido. | 201 | Aprobada |
| 8 | Creación válida de propietario. | 201 y DTO sin colecciones anidadas. | 201 | Aprobada |
| 9 | Creación de mascota con propietario existente. | 201 y relación correcta. | 201 | Aprobada |
| 10 | Mascota con propietario inexistente. | 400 controlado; no se inserta fila. | 400 | Aprobada |
| 11 | Cita futura con referencias válidas. | 201 y cita persistida. | 201 | Aprobada |
| 12 | Cita con fecha pasada. | 400 con mensaje claro. | 400 | Aprobada |
| 13 | Segundo intento con mismo veterinario y horario. | 400; se conserva una sola cita. | 400 | Aprobada |
| 14 | Filtro de citas por veterinario. | 200 y solo coincidencias. | 200 | Aprobada |
| 15 | Reinicio y prueba desde Swagger con Authorize. | Datos persisten y flujo protegido funciona. | 200 | Aprobada |

Evidencias adicionales:

| # | Escenario | Resultado esperado | Estado HTTP obtenido | Resultado |
|---|---|---|---|---|
| E1 | Evidencia parte 6: propietario con varios campos inválidos. | 400 con varios errores de campo. | 400 | Aprobada |
| E2 | Evidencia extra: login con contraseña incorrecta. | 401 sin token. | 401 | Aprobada |

Captura de Swagger UI en Edge en [swagger-ui.png](swagger-ui.png)(datos de VetTurno y botón Authorize), las capturas de cada paso del flujo están en [capturas.md](capturas.md).

## Detalle de cada prueba

### Prueba 1. La aplicación inicia con MySQL disponible.

- **Petición:** GET /v3/api-docs y SHOW TABLES en MySQL
- **Esperado:** Servidor activo y esquema accesible.
- **Obtenido:** HTTP 200 (Aprobada)

Respuesta:

```json
{
  "titulo": "VetTurno API",
  "tablas": [
    "cita",
    "mascota",
    "propietario",
    "usuario",
    "veterinario"
  ]
}
```

### Prueba 2. Registro válido de Paula.

- **Petición:** POST /api/auth/register {"email": "paula@huellitas.com", "password": "********", "rol": "ADMIN"}
- **Esperado:** 200 y token; contraseña hasheada.
- **Obtenido:** HTTP 200 (Aprobada)
- **Comprobación:** En MySQL el rol y el inicio del hash quedaron como `USER	$2a$10$`, se ignoró el rol que se mandó y la contraseña no está en texto plano.

Respuesta:

```json
{
  "token": "eyJhbGciOiJIUzUx…(oculto)"
}
```

### Prueba 3. Registro con email inválido y clave corta.

- **Petición:** POST /api/auth/register {"email": "paula-sin-arroba", "password": "123"}
- **Esperado:** 400 con errores por campo.
- **Obtenido:** HTTP 400 (Aprobada)

Respuesta:

```json
{
  "status": 400,
  "mensaje": "Los datos enviados no son válidos",
  "errores": {
    "email": "El email no tiene un formato válido",
    "password": "La contraseña debe tener entre 8 y 72 caracteres"
  },
  "timestamp": "2026-09-24T18:04:10.031729500"
}
```

### Prueba 4. Login con credenciales válidas.

- **Petición:** POST /api/auth/login {email: paula@huellitas.com}
- **Esperado:** 200 y JWT vigente.
- **Obtenido:** HTTP 200 (Aprobada)
- **Comprobación:** El token se usó enseguida en GET /api/veterinarios y respondió 200.

Respuesta:

```json
{
  "token": "eyJhbGciOiJIUzUx…(oculto)"
}
```

### Prueba 5. GET /api/citas sin token.

- **Petición:** GET /api/citas (sin Authorization)
- **Esperado:** Acceso rechazado.
- **Obtenido:** HTTP 401 (Aprobada)

Respuesta:

```json
{
  "status": 401,
  "mensaje": "Debes iniciar sesión: envía un token válido en el encabezado Authorization: Bearer",
  "errores": {},
  "timestamp": "2026-09-24T18:04:10.252207200"
}
```

### Prueba 6. POST /api/veterinarios con USER.

- **Petición:** POST /api/veterinarios (token de Paula, USER) {"nombre": "Andrés Ruiz", "especialidad": "Medicina general"}
- **Esperado:** 403 Forbidden.
- **Obtenido:** HTTP 403 (Aprobada)
- **Comprobación:** Después del intento hay 0 veterinarios en MySQL.

Respuesta:

```json
{
  "status": 403,
  "mensaje": "No tienes permiso para realizar esta operación",
  "errores": {},
  "timestamp": "2026-09-24T18:04:10.261208"
}
```

### Prueba 7. POST /api/veterinarios con ADMIN.

- **Petición:** Marta se registra, se promueve con UPDATE usuario SET rol='ADMIN', inicia sesión de nuevo y hace POST /api/veterinarios {"nombre": "Andrés Ruiz", "especialidad": "Medicina general"}
- **Esperado:** 201 y veterinario persistido.
- **Obtenido:** HTTP 201 (Aprobada)
- **Comprobación:** También se registró a Sofía Méndez(id 2) para probar el filtro por veterinario.

Respuesta:

```json
{
  "especialidad": "Medicina general",
  "id": 1,
  "nombre": "Andrés Ruiz"
}
```

### Prueba 8. Creación válida de propietario.

- **Petición:** POST /api/propietarios (USER) {"nombre": "Laura Gómez", "telefono": "618 123 4567", "email": "laura@correo.com"}
- **Esperado:** 201 y DTO sin colecciones anidadas.
- **Obtenido:** HTTP 201 (Aprobada)

Respuesta:

```json
{
  "email": "laura@correo.com",
  "id": 1,
  "nombre": "Laura Gómez",
  "telefono": "618 123 4567"
}
```

### Prueba 9. Creación de mascota con propietario existente.

- **Petición:** POST /api/mascotas (USER) {"nombre": "Firulais", "especie": "Perro", "raza": "Criollo", "propietarioId": 1}
- **Esperado:** 201 y relación correcta.
- **Obtenido:** HTTP 201 (Aprobada)

Respuesta:

```json
{
  "especie": "Perro",
  "id": 1,
  "nombre": "Firulais",
  "propietarioId": 1,
  "propietarioNombre": "Laura Gómez",
  "raza": "Criollo"
}
```

### Prueba 10. Mascota con propietario inexistente.

- **Petición:** POST /api/mascotas {"nombre": "Fantasma", "especie": "Perro", "propietarioId": 9999}
- **Esperado:** 400 controlado; no se inserta fila.
- **Obtenido:** HTTP 400 (Aprobada)
- **Comprobación:** La tabla mascota tenía 2 filas antes y siguió con 2 filas después.

Respuesta:

```json
{
  "status": 400,
  "mensaje": "No existe un propietario con id 9999",
  "errores": {},
  "timestamp": "2026-09-24T18:04:11.521245500"
}
```

### Prueba 11. Cita futura con referencias válidas.

- **Petición:** POST /api/citas (USER) {"fechaHora": "2026-10-01T10:00", "motivo": "Vacunación anual", "mascotaId": 1, "veterinarioId": 1}
- **Esperado:** 201 y cita persistida.
- **Obtenido:** HTTP 201 (Aprobada)

Respuesta:

```json
{
  "fechaHora": "2026-10-01T10:00:00",
  "id": 1,
  "mascota": "Firulais",
  "mascotaId": 1,
  "motivo": "Vacunación anual",
  "propietario": "Laura Gómez",
  "veterinario": "Andrés Ruiz",
  "veterinarioId": 1
}
```

### Prueba 12. Cita con fecha pasada.

- **Petición:** POST /api/citas {"fechaHora": "2024-01-15T10:00", "motivo": "Control", "mascotaId": 1, "veterinarioId": 1}
- **Esperado:** 400 con mensaje claro.
- **Obtenido:** HTTP 400 (Aprobada)

Respuesta:

```json
{
  "status": 400,
  "mensaje": "Los datos enviados no son válidos",
  "errores": {
    "fechaHora": "La fecha y hora de la cita debe ser futura"
  },
  "timestamp": "2026-09-24T18:04:12.098795"
}
```

### Prueba 13. Segundo intento con mismo veterinario y horario.

- **Petición:** POST /api/citas (otra mascota, mismo veterinario y hora) {"fechaHora": "2026-10-01T10:00", "motivo": "Revisión de oído", "mascotaId": 2, "veterinarioId": 1}
- **Esperado:** 400; se conserva una sola cita.
- **Obtenido:** HTTP 400 (Aprobada)
- **Comprobación:** Ese veterinario tiene 1 sola cita en ese horario.

Respuesta:

```json
{
  "status": 400,
  "mensaje": "El veterinario Andrés Ruiz ya tiene una cita en ese horario",
  "errores": {},
  "timestamp": "2026-09-24T18:04:12.110540400"
}
```

### Prueba 14. Filtro de citas por veterinario.

- **Petición:** GET /api/citas/veterinario/1
- **Esperado:** 200 y solo coincidencias.
- **Obtenido:** HTTP 200 (Aprobada)
- **Comprobación:** Antes se agendó a la misma hora una cita con Sofía Méndez y respondió 201(otro veterinario sí puede), GET /api/citas regresa 2 citas y el filtro regresa solo la de Andrés Ruiz.

Respuesta:

```json
[
  {
    "fechaHora": "2026-10-01T10:00:00",
    "id": 1,
    "mascota": "Firulais",
    "mascotaId": 1,
    "motivo": "Vacunación anual",
    "propietario": "Laura Gómez",
    "veterinario": "Andrés Ruiz",
    "veterinarioId": 1
  }
]
```

### Prueba 15. Reinicio y prueba desde Swagger con Authorize.

- **Petición:** Se detuvo y se reinició la aplicación, Paula inició sesión de nuevo y se llamó GET /api/citas con el encabezado `Authorization: Bearer <token>`, que es el mismo que agrega el botón Authorize de Swagger(ver la captura de Swagger UI).
- **Esperado:** Datos persisten y flujo protegido funciona.
- **Obtenido:** HTTP 200 (Aprobada)
- **Comprobación:** Después del reinicio sin token respondió 401 y con token respondió 200 con las 2 citas que se guardaron antes del reinicio.

Respuesta:

```json
[
  {
    "fechaHora": "2026-10-01T10:00:00",
    "id": 1,
    "mascota": "Firulais",
    "mascotaId": 1,
    "motivo": "Vacunación anual",
    "propietario": "Laura Gómez",
    "veterinario": "Andrés Ruiz",
    "veterinarioId": 1
  },
  {
    "fechaHora": "2026-10-01T10:00:00",
    "id": 2,
    "mascota": "Michi",
    "mascotaId": 2,
    "motivo": "Esterilización",
    "propietario": "Laura Gómez",
    "veterinario": "Sofía Méndez",
    "veterinarioId": 2
  }
]
```

### Prueba E1. Evidencia parte 6: propietario con varios campos inválidos.

- **Petición:** POST /api/propietarios {"nombre":"","telefono":"abc","email":"no-es-email"}
- **Esperado:** 400 con varios errores de campo.
- **Obtenido:** HTTP 400 (Aprobada)

Respuesta:

```json
{
  "status": 400,
  "mensaje": "Los datos enviados no son válidos",
  "errores": {
    "email": "El email no tiene un formato válido",
    "nombre": "El nombre es obligatorio",
    "telefono": "El teléfono debe tener entre 7 y 20 dígitos y puede iniciar con +"
  },
  "timestamp": "2026-09-24T18:04:12.444053300"
}
```

### Prueba E2. Evidencia extra: login con contraseña incorrecta.

- **Petición:** POST /api/auth/login (contraseña incorrecta)
- **Esperado:** 401 sin token.
- **Obtenido:** HTTP 401 (Aprobada)

Respuesta:

```json
{
  "status": 401,
  "mensaje": "Email o contraseña incorrectos",
  "errores": {},
  "timestamp": "2026-09-24T18:04:12.508795600"
}
```
