## Despliegue

Opciones comunes:
- Dockerize: crear `Dockerfile` y `docker-compose` para app + MySQL.
- CI/CD: GitHub Actions para `mvn -B -DskipTests=false verify` + build + deploy.
- Variables sensibles: usar secrets (DB credentials, JWT secret) en el entorno (no en `application.properties` en repo).

Ejemplo `Dockerfile` (esqueleto):
```dockerfile
FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```