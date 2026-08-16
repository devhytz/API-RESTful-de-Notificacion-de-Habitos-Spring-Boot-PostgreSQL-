#  Habitos Backend - API REST

API RESTful desarrollada con **Spring Boot** y **Java 17/21** para la gestión de hábitos y usuarios. El proyecto utiliza una arquitectura modular en capas, persistencia en la nube con **PostgreSQL (Supabase)**, validación estricta de DTOs y manejo global centralizado de excepciones.

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 21
- **Framework:** Spring Boot
- **Persistencia / ORM:** Spring Data JPA / Hibernate
- **Base de Datos:** PostgreSQL (alojado en Supabase)
- **Utilidades:** Lombok, Bean Validation (`@Valid`)
- **Gestor de Dependencias:** Maven
- **Pruebas de API:** Postman

---

##  Arquitectura del Proyecto

El proyecto sigue el patrón de **Arquitectura en Capas (Layered Architecture)** para garantizar una clara separación de responsabilidades:

```text
com.habitos.backend
├── controller   # Capa de presentación (Endpoints REST HTTP)
├── service      # Capa de lógica de negocio y reglas del sistema
├── repository   # Capa de acceso a datos (Spring Data JPA)
├── model        # Entidades JPA y mapeo relacional de la BD
├── dto          # Objetos de transferencia de datos
└── exception    # Manejo global centralizado de excepciones (@RestControllerAdvice)