# Configuración de la base de datos en application.properties:
En el momento en que creamos el proyecto, Spring Boot generó una estructura de carpetas como esta:

Encontrarás en resources un archivo de texto plano que forma parte del mismo proyecto de Spring Boot que estamos desarrollando de nombre “application.properties”, el cual es fundamental para que:

Spring Boot sepa a qué base de datos conectarse.

Se genere correctamente el esquema a partir de las entidades.

Se apliquen validaciones, logs de SQL y otras opciones útiles en desarrollo.

Pasos para generar la conexión:

1. Crear una base de datos vacía: Debemos tener una base de datos vacía llamada “inventory_db” en MySQL en mi caso (DBeaver).

Ventajas de usar DBeaver:

Compatible con MySQL, PostgreSQL y muchos más (es multi-motor).

Visualiza la estructura de tus tablas creadas automáticamente por Spring Boot.

Puedes ejecutar consultas SQL manuales para verificar datos, insertar pruebas, etc.

Tiene interfaz gráfica intuitiva para revisar relaciones entre tablas (muy útil si usas muchas entidades con relaciones JPA como en tu proyecto).

Totalmente gratuito y multiplataforma.

¿Cómo configurar DBeaver para evitar errores?

1. En DBeaver, ve a la configuración de tu conexión:

Haz clic derecho sobre la conexión → Editar conexión.

2. Ve a la pestaña Driver Properties.

3. Busca la propiedad allowPublicKeyRetrieval:

Si no está, haz clic en Agregar nueva propiedad.

Escribe:

Name: allowPublicKeyRetrieval

Value: true

4. También asegúrate de que esté:

useSSL = false (o configurado correctamente según tu entorno)

5. Guarda e intenta conectar.

¿Cómo conectar DBeaver a la base de datos local de MySQL?

Abre DBeaver.

Ve a Archivo > Nuevo.

Luego DataBase connection.

Elige MySQL y haz clic en Siguiente.

Llena los campos así:

Servidor: localhost

Puerto: 3306

Base de datos: La dejamos vacía

Usuario: el que tengas (por defecto es root)

Contraseña: la que tengas configurada (En mi caso dejaré root)

Haz clic en Finalizar.

Si no tienes el driver MySQL, DBeaver te pedirá descargarlo automáticamente. Acepta.

Luego ingresaremos en nuestra base de datos local y crearemos nuestra base de datos con el comando “CREATE DATABASE inventory_db;”.

Y por último iremos a “Edit Connection” dando clic derecho sobre “inventory_db”.

Espacio vacío que dejamos antes de “Base de datos” lo llenaremos con el nombre “inventory_db” y daremos “Aceptar”.

¿Como conectar tu proyecto Spring Boot (VS Code) a inventory_db?

Ya con la base de datos vacía, nos dirigiremos nuevamente a nuestro archivo “application.properties” y allí agregaremos la siguiente configuración, agregando los datos reales que hemos configurado antes:

# Configuración de conexión a la base de datos MySQL

spring.application.name=inventorysaleshub

spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db

spring.datasource.username=root

spring.datasource.password=root

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA / Hibernate

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

Ahora desde la raíz del proyecto, abrimos una terminal integrada (Ctrl + ñ o Terminal > New Terminal) y ejecutamos “./mvnw spring-boot:run” y si todo va bien se crearán las tablas automáticamente y nos saldrá:

Y en DBeaver al renovar la página, lo podremos observar así:

Controladores (Controller)

¿Qué son los controladores en Spring Boot?

Los controladores son clases que gestionan las solicitudes HTTP y definen cómo responde tu aplicación a dichas solicitudes. En el patrón Modelo-Vista-Controlador (MVC), los controladores actúan como intermediarios entre el modelo (lógica y datos) y la vista (interfaz de usuario).

¿Para qué sirven?

