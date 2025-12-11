# Y así habremos terminado de crear nuestra base de datos, sin embargo, es importante revisar los siguientes puntos:
1. Todas las entidades tienen su clase @Entity.

2.  Todas están en el paquete model/.

3. Tienen sus @Id y @GeneratedValue.

4. Las relaciones están mapeadas correctamente (@OneToMany, @ManyToOne, etc.).

Validaciones y restricciones en las entidades

Las validaciones y restricciones nos ayudaran para que los usuarios ingresen la información que buscamos que ingrese, que no dejen casillas en blanco o que solo pueda ingresar algunos caracteres o tipos de datos en concreto, por ejemplo:

@NotNull → Evita valores nulos.

@NotEmpty → Evita valores nulos y Evita valores vacíos ("").

@NotBlank → Evita valores nulos, Evita valores vacíos ("") e Ignora espacios en blanco.

@Size(min, max) → Restringe el tamaño de un campo.

@Email → Verifica que el campo sea un correo válido.

@Column(unique = true) → Evita duplicados en la base de datos.

@Pattern(regexp) → Se usa para restringir y validar que un campo solo acepte ciertos valores o formatos (ejemplo: correos, teléfonos, estados de una orden).

@Past → Asegura que la fecha sea en el pasado.

@Future → Asegura que la fecha sea en el futuro.

Ten presente que para poder trabajar con validaciones necesitaremos importar las librerías correspondientes, para esto podremos utilizar “import jakarta.validation.constraints.*;”.

Sin embargo, hay un proceso anterior que debemos realizar para evitar problemas futuros y verificar con Maven que todo funciona correctamente.

1. En nuestro POM, agregaremos las siguientes dependencias:

Ya que estas dos dependencias trabajan en conjunto y son fundamentales si quieres añadir validaciones automáticas a tus entidades, formularios o peticiones REST usando anotaciones como @NotNull, @Email, @Size, etc.

2. Agregaremos esta configuración a nuestro plugin dentro de POM:

Este plugin es fundamental en los proyectos Spring Boot que se compilan y ejecutan con Maven y la parte que agregamos le dice explícitamente al plugin cuál es tu clase principal.

3. Antes de ejecutar “mvn clean install” en nuestra terminal o en nuestro CMD ubicándonos en nuestro proyecto con el comando “cd C:\Users\virtu\Documents\GitHub\inventory-sales-hub\inventorysaleshub” para realizar la limpieza, compilación e instalación.

Sin embargo, para evitar errores, como recomendación sería necesario eliminar momentáneamente la clase “InventorysaleshubApplicationTests.java”, debido a que cuando lancemos el comando “mvn clean install” al final intentará realizar las pruebas automatizadas, pero como aun no llegamos a esa parte del proyecto y no hemos modificado el archivo nos lanzará un error.

Puedes guardar la información que está dentro en un bloc de notas para luego crear nuevamente la clase y pegar la información.

4. Si hemos hecho todo correctamente podremos ejecutar el comando “mvn clean install” y podremos utilizar sin problemas las Validaciones y restricciones en las entidades.

Un ejemplo de uso sería el de @NotBlank y @Size en nuestra clase “Category”, No debe marcar ningún tipo de error y podremos también adicionar atributos (Valores o Mensajes):

Recordar realizar las validaciones y restricciones en cada una de nuestras entidades según correspondan, otro ejemplo seria nuestra clase “Invoice”, donde las hemos definido de la siguiente manera:

Interfaces de repositorio

Ahora crearemos todas las interfaces de los repositorios correspondientes para cada una de nuestras entidades, recordar que al inicio cuando creamos nuestra estructura, definimos el ProductRepository (La interfaz de repositorio de nuestra clase Product), ahora haremos lo mismo para el resto, lo cual es más sencillo ya que copiaremos y pegaremos el código únicamente cambiando el nombre de la entidad en los puntos marcados:

Es bueno recordar que el nombre de la entidad debe coincidir tanto en el import .model.Category como en la extensión de nuestra interfaz <Category , Long>, con el nombre de la entidad, ya que así tendría que estar en nuestra imagen anterior (Las entidades son las que se encuentran en nuestro paquete model).

Y al finalizar tendremos la siguiente estructura de repositorios:

# 2. Dentro de la conexión (LocalHost) crearemos una nueva base de datos:
CREATE DATABASE inventory_test_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

3. Crearemos un nuevo archivo en nuestra carpeta resources y lo llamaremos “application-test.properties”.

Dentro de este nuevo archivo pondremos la siguiente configuración:

spring.datasource.url=jdbc:mysql://localhost:3306/inventory_test_db

spring.datasource.username=root

spring.datasource.password=root

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create-drop

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

NOTA: create-drop borra y vuelve a crear las tablas cada vez que se inicia la app. Útil para pruebas.

Para probar que la conexión sea exitosa en nuestra terminal realizaremos la consulta:

./mvnw spring-boot:run

Al finalizar de correr si todo salió bien no nos tendría que arrojar ningún error y al ir Dbeaver veremos que se habrán creado nuestras tablas de prueba automáticamente:

Nota: Si posteriormente cambiamos los nombres de las tablas en nuestra base de datos desde VScode, al ejecutarlo creara copias de las tablas con los nuevos nombres conservando las tablas anteriores con los nombres anteriores, por lo que para corregir esto podemos eliminar la base de datos en DBeaver y posteriormente en la terminal dentro de VScode ejecutaremos “./mvnw spring-boot:run” para crear nuevamente las tablas con sus nombres correspondientes y sin copias.

Clase TestDataLoader

¿Qué es TestDataLoader?

Será la clase dentro de nuestro proyecto que se encargue de cargar datos de prueba automáticamente cuando el sistema se ejecuta en modo prueba (Test). Su función es crear información inicial en la base de datos de prueba (inventory_test_db) para que podamos evaluar funcionalidades sin necesidad de ingresar datos manualmente.

Flujo de interacción

1️⃣ Ejecutar la aplicación con SPRING_PROFILES_ACTIVE=test2️⃣ Spring Boot activa application-test.properties y usa inventory_test_db3️⃣ TestDataLoader crea roles, usuarios y productos automáticamente4️⃣ Podemos probar los endpoints sin necesidad de registrar datos manualmente

Pasos e implementación

Nota: Antes de la creación de la clase directamente debemos crear los constructores vacíos que necesitaremos. Recuerda que estos deben crearse antes de los getters y setters previamente definidos.

1- Vamos a generar los constructores necesarios para su correcto uso:

Role

User

Product

2- También podemos generar los constructores opcionales que podríamos utilizar:

Order

Invoice

Pay

OrderDetails

# 1. Evitar exponer la base de datos directamente:
Las entidades (User, Order, Product) contienen relaciones internas (foráneas, historiales, etc.) que no siempre quieres mostrar. Con un DTO complejo decides exactamente qué campos viajan en la respuesta.

2. Optimizar la comunicación:

En lugar de hacer múltiples llamadas a la API para armar una pantalla (ej. pedido + productos + factura + pago), puedes devolver todo en un único DTO compuesto. Esto reduce peticiones y mejora el rendimiento.

3. Adaptar la respuesta al cliente:

La forma en que un frontend necesita los datos no siempre coincide con cómo se almacenan en la base. Un DTO complejo transforma la información para que sea intuitiva y práctica de usar.

4. Estandarizar la API:

Permiten mantener respuestas consistentes y fáciles de documentar en Swagger/OpenAPI.

Vamos a crear algunos DTOs para respuestas complejas y lo implementaremos en nuestro programa, como por ejemplo un Order que devuelve detalles anidados (productos, factura, pago).

# Agregamos la interfaz que permite acceder a la base de datos:
Y agregaremos el Endpoint:

