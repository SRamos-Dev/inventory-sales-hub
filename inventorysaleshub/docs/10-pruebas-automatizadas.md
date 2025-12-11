## Pruebas automatizadas

Recomendado:
- **JUnit 5** para tests unitarios.
- **Mockito** para mocks de servicios/repositorios.
- **Spring Boot Test** (`@SpringBootTest`) para integración.
- **Testcontainers** si quieres levantar contenedores de MySQL durante pruebas de integración.

Ejemplo comando:
```bash
./mvnw test
```