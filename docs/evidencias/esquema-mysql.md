# Evidencias: esquema en MySQL

La base `vetturno` se creó vacía con `CREATE DATABASE vetturno;` y Hibernate generó las tablas al primer inicio(`spring.jpa.hibernate.ddl-auto=update` y `spring.jpa.show-sql=true`).

## SQL generado por Hibernate al iniciar

```sql
    create table cita (
        id bigint not null auto_increment,
        fecha_hora datetime(6) not null,
        motivo varchar(255) not null,
        mascota_id bigint not null,
        veterinario_id bigint not null,
        primary key (id)
    ) engine=InnoDB
    create table mascota (
        id bigint not null auto_increment,
        especie varchar(40) not null,
        nombre varchar(60) not null,
        raza varchar(60),
        propietario_id bigint not null,
        primary key (id)
    ) engine=InnoDB
    create table propietario (
        id bigint not null auto_increment,
        email varchar(120),
        nombre varchar(100) not null,
        telefono varchar(20) not null,
        primary key (id)
    ) engine=InnoDB
    create table usuario (
        id bigint not null auto_increment,
        email varchar(120) not null,
        password varchar(100) not null,
        rol varchar(10) not null check ((rol in ('USER','ADMIN'))),
        primary key (id)
    ) engine=InnoDB
    create table veterinario (
        id bigint not null auto_increment,
        especialidad varchar(80) not null,
        nombre varchar(100) not null,
        primary key (id)
    ) engine=InnoDB
    alter table cita 
       drop index uk_cita_veterinario_fecha_hora
    alter table cita 
       add constraint uk_cita_veterinario_fecha_hora unique (veterinario_id, fecha_hora)
    alter table usuario 
       drop index UK5171l57faosmj8myawaucatdw
    alter table usuario 
       add constraint UK5171l57faosmj8myawaucatdw unique (email)
    alter table cita 
       add constraint FKjjr9rbirfalfxoq1rndrc8sqq 
       foreign key (mascota_id) 
       references mascota (id)
    alter table cita 
       add constraint FKpod5e68nvkvih2v45qkdf7bwc 
       foreign key (veterinario_id) 
       references veterinario (id)
    alter table mascota 
       add constraint FK4m4uy4tmtoora1pd8q0v892fd 
       foreign key (propietario_id) 
       references propietario (id)
```

## Tablas, llaves foráneas y restricciones únicas

```text
mysql> SHOW TABLES;
Tables_in_vetturno
cita
mascota
propietario
usuario
veterinario

mysql> SELECT TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA='vetturno' AND REFERENCED_TABLE_NAME IS NOT NULL ORDER BY TABLE_NAME, COLUMN_NAME;
TABLE_NAME	COLUMN_NAME	REFERENCED_TABLE_NAME
cita	mascota_id	mascota
cita	veterinario_id	veterinario
mascota	propietario_id	propietario

mysql> SELECT TABLE_NAME, CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA='vetturno' AND CONSTRAINT_TYPE='UNIQUE' ORDER BY TABLE_NAME;
TABLE_NAME	CONSTRAINT_NAME
cita	uk_cita_veterinario_fecha_hora
usuario	UK5171l57faosmj8myawaucatdw

mysql> SELECT COLUMN_NAME, COLUMN_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='vetturno' AND TABLE_NAME='usuario' ORDER BY ORDINAL_POSITION;
COLUMN_NAME	COLUMN_TYPE
id	bigint(20)
email	varchar(120)
password	varchar(100)
rol	varchar(10)

```

## Contraseñas guardadas con BCrypt

Se muestra solo el inicio de cada hash, la contraseña original nunca se guarda.

```text
mysql> SELECT id, email, CONCAT(LEFT(password, 12), '...') AS hash, rol FROM usuario;
id	email	hash	rol
1	paula@huellitas.com	$2a$10$.o5aq...	USER
2	marta@huellitas.com	$2a$10$jslZb...	ADMIN

```

## Datos persistidos después del reinicio

```text
mysql> SELECT id, nombre, telefono, email FROM propietario;
id	nombre	telefono	email
1	Laura Gómez	618 123 4567	laura@correo.com

mysql> SELECT id, nombre, especie, raza, propietario_id FROM mascota;
id	nombre	especie	raza	propietario_id
1	Firulais	Perro	Criollo	1
2	Michi	Gato	Siamés	1

mysql> SELECT id, nombre, especialidad FROM veterinario;
id	nombre	especialidad
1	Andrés Ruiz	Medicina general
2	Sofía Méndez	Cirugía

mysql> SELECT id, fecha_hora, motivo, mascota_id, veterinario_id FROM cita;
id	fecha_hora	motivo	mascota_id	veterinario_id
1	2026-10-01 10:00:00.000000	Vacunación anual	1	1
2	2026-10-01 10:00:00.000000	Esterilización	2	2

```
