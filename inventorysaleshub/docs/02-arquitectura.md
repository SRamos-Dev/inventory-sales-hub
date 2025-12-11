## Arquitectura

Patrón: **Spring MVC** (Modelo — Vista — Controlador).  
Separación en capas: Model, Repository, Service, Controller, Security.

### Componentes principales

- **Model (entities)**: Clases `@Entity` que representan tablas.
- **Repository**: Extiende `JpaRepository` para cada entidad.
- **Service**: Lógica de negocio (AuthService, ProductService, etc.).
- **Controller**: Endpoints REST (`@RestController`) que usan DTOs.
- **Security**: `JwtProvider`, filtro JWT, `SecurityConfig`, manejo de `PasswordEncoder`.

### Flujo típico de una petición (resumido)

```
Cliente -> Controller -> Service -> Repository -> Base de datos
```