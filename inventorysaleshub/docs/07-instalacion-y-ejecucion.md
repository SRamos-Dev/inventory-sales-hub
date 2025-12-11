## Instalación y ejecución

### Requisitos
- Java JDK 17+
- Maven 3.8+
- MySQL 8.x

### Clonar repo
```bash
git clone https://github.com/SRamos-Dev/inventory-sales-hub.git
cd inventory-sales-hub
```

### Crear base de datos (MySQL)
```sql
CREATE DATABASE inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Configurar `src/main/resources/application.properties`
Ejemplo mínimo:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### Ejecutar
```bash
./mvnw clean package
./mvnw spring-boot:run
```
O ejecutar el `jar`:
```bash
java -jar target/inventorysaleshub-0.0.1-SNAPSHOT.jar
```
