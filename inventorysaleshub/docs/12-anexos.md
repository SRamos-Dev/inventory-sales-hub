# Anexos

Manual y guía de proyecto (Gestor de ventas e inventario)

Por Sergio Esteban Ramos

Introducción:3

Objetivo del proyecto:3

¿Por qué Spring Boot y Java?3

Estructura del Manual3

Definición de requisitos y objetivos de diseño4

Objetivos específicos del proyecto4

Flujo de interacción5

Arquitectura (Spring MVC)5

¿Qué es Spring MVC(Modelo Vista Controlador)?5

¿Cuál es su propósito?5

Configuración del entorno de desarrollo6

Estructura necesaria para iniciar nuestro proyecto8

¿Ejemplos de implementación MVC?9

Creación de la base de datos y gestión de datos (Model)11

Definir las entidades principales:11

Como realizar nuestra Base de datos13

Interfaces de repositorio22

Configuración de la base de datos en application.properties:23

Controladores (Controller)26

¿Qué son los controladores en Spring Boot?26

Implementación de funcionalidades (ProductController)28

UserController35

OrderController36

InvoiceController37

PayController38

CategoryController39

StoreController39

SupplierController40

RoleController40

Implementación de usuario y administrador (Modo prueba)41

¿Qué es el modo prueba?41

¿Para qué sirve?41

Desarrollo41

Clase TestDataLoader43

Uso de DTOs (Opcional pero recomendado)46

DTOs para respuestas complejas:58

DTO genérico “<T>” para respuestas estándar:60

Changelog de DTOs y Refactorización de Controladores75

Seguridad y Autenticación77

Configuración de Spring Security:78

JwtProvider81

AuthService84

Flujo de Autenticación con JWT89

Gestión de roles y protección de endpoints (Opcional pero recomendable)90

Configuración adicional de seguridad95

Estadísticas y Panel Administrativo (Dashboard)96

Creación de DTOs necesarios97

DashboardController99

Configuración esencial100

Refactor DashboardController103

Adición de métricas104

Adiciones que pueden hacer nuestro Dashboard más dinámico (Opcional)109

Documentación de la API (Swagger/OpenAPI)114

¿Qué es Swagger/OpenAPI?114

¿Para qué sirve?114

Springdoc OpenAPI115

ModelMapper116

Uso en los controladores117

Despliegue y ejecución del proyecto143

