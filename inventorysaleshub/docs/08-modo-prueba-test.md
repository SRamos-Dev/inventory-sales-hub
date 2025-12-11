## Modo prueba (Test Mode)

Para entorno de pruebas aislado:

1. Crear DB de pruebas:
```sql
CREATE DATABASE inventory_test_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Añadir `application-test.properties` en `resources` con:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_test_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

3. Ejecutar:
```bash
SPRING_PROFILES_ACTIVE=test ./mvnw spring-boot:run
```

En modo test la clase `TestDataLoader` precarga roles, usuarios demo y productos. (Ver implementación en `service/config` o `loader`).