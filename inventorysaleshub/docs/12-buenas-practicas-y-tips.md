## Buenas prácticas y tips

- Usar DTOs para proteger entidades.
- Validar entradas con `@Valid` y constraints (`@NotBlank`, `@Email`, `@Size`).
- Manejo centralizado de excepciones (`@ControllerAdvice`) que devuelva `ApiResponseDTO`.
- No almacenar secretos en el repo; usar variables de entorno.
- Documentar la API con Springdoc OpenAPI / Swagger.
- Mantener commits atómicos y PRs revisables.
- Añadir tests unitarios para cada servicio crítico.