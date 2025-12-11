# Objetivo del proyecto:
El propósito de esta aplicación es simular un entorno de comercio digital, con funcionalidades para diferentes tipos de usuario:

Administradores: gestión de inventarios, promociones y auditoría de acciones.

Usuarios: navegación, selección de productos, compras simuladas, historial de transacciones.

Modo prueba: modo test para evaluar escenarios sin afectar datos reales.

¿Por qué Spring Boot y Java?

Spring Boot ofrece una arquitectura flexible, escalable y productiva para APIs REST.

Java garantiza portabilidad, estabilidad y gran comunidad.

Este stack permite cubrir desde la lógica del negocio y persistencia de datos hasta la exposición de servicios para frontend o aplicaciones móviles.

Estructura del Manual

Este documento está organizado siguiendo una metodología progresiva:

Definición de requisitos y objetivos de diseño

Arquitectura (Spring MVC)

Creación de la base de datos y gestión de datos (Model)

Controladores (Controller)

Implementación de usuario y administrador (Modo prueba)

Seguridad y Autenticación

Estadísticas y Panel Administrativo (Dashboard)

Documentación de la API (Swagger/OpenAPI)

Despliegue y ejecución del proyecto

Interfaz de Usuario (Vista)

Pruebas Automatizadas (Junit/Testcontainers)

Optimización y mejoras finales

Pruebas que realizar durante el desarrollo

Lecciones aprendidas y conclusiones

Este manual no solo es documentación técnica, también actúa como guía de aprendizaje para desarrolladores que quieran replicar o ampliar este tipo de proyectos.

Definición de requisitos y objetivos de diseño

Antes de escribir cualquier línea de código, es fundamental planificar cómo funcionará la aplicación, qué tecnologías se utilizarán y cómo se estructurará el proyecto. Esta fase sienta las bases del desarrollo posterior y garantiza una implementación más organizada, eficiente y escalable.

Herramientas:

Para comenzar con el desarrollo del proyecto, es necesario contar con un conjunto de herramientas que faciliten tanto la escritura de código como las pruebas y el monitoreo de la base de datos.

Herramientas principales:

Java JDK: Compilación y ejecución de la aplicación.

Spring Boot: Estructura y ejecución del backend.

Visual Studio Code: Editor de código liviano y versátil.

Maven: Gestión de dependencias y ciclo de vida del proyecto.

Herramientas complementarias:

Postman (recomendado): Para probar los endpoints desde el inicio.

DBeaver: Ideal para explorar visualmente la base de datos y sus relaciones.

MySQL Workbench / pgAdmin: Alternativas gráficas para trabajar con SQL.

Recomendación: Aunque puedes avanzar sin estas herramientas, te facilitarán el monitoreo, pruebas y depuración.

Objetivos específicos del proyecto

Funcionalidades por tipo de usuario:

👤 Usuario

Registro y gestión de perfil

Navegación con filtros

Carrito de productos

Simulación de compras

Historial de pedidos

Valoraciones y comentarios

🛠️ Administrador

Gestión de productos y stock

Aplicación de descuentos y promociones

Monitorización de ventas

Alertas de stock bajo

Gestión de empleados y permisos

Auditoría de cambios

🌐 Modo prueba

Autenticación segura con Spring Security

Emisión de facturas

Pasarela de pagos ficticia

Modo test sin impacto real

Posible conversión a PWA

Flujo de interacción

1️. El usuario navega por la plataforma y añade productos al carrito.2️. El sistema valida la acción y guarda la información en la base de datos.3️. El administrador revisa ventas, aplica cambios de precio o ajusta stock según demanda.4️. El usuario recibe notificaciones y puede completar la compra.

Arquitectura (Spring MVC)

¿Qué es Spring MVC(Modelo Vista Controlador)?

Spring MVC es un módulo del framework Spring diseñado para desarrollar aplicaciones web en Java siguiendo el patrón Modelo-Vista-Controlador (MVC). Ayuda a separar la lógica de negocio (Modelo), la presentación (Vista) y el flujo de control (Controlador) para una arquitectura más organizada y mantenible.

Modelo (@Entity) → Representa los datos. Ejemplo: la tabla "Productos" en la base de datos.Vista (Thymeleaf o React/Vue/Angular) → Es la interfaz con la que el usuario interactúa.Controlador (@RestController) → Maneja las solicitudes y envía datos de la base de datos a la vista.

Ejemplo: Si un usuario quiere ver los productos disponibles:

La Vista envía la solicitud → GET /productos

El Controlador procesa la solicitud y pregunta al Modelo por los datos.

El Modelo (base de datos) devuelve la lista de productos.

El Controlador envía los datos a la Vista, que los muestra en pantalla.

¿Cuál es su propósito?

El propósito principal de Spring MVC es facilitar el desarrollo de aplicaciones web escalables, proporcionando herramientas para gestionar solicitudes HTTP, manipular datos, interactuar con bases de datos y generar vistas dinámicas de manera eficiente.

Configuración del entorno de desarrollo

Tendremos que crear nuestro repositorio, en mi caso lo llamaré (Inventory-sales-hub):

Como trabajaremos con Spring Boot instalaremos la siguiente extensión:

Si ya lo tienen instalado solo deben abrir su VS code y en la ventana de bienvenida daremos clic en “more” y luego “Getting Started with Spring Boot in VS Code”.

Luego daremos clic en “Create new project” y seleccionaremos la versión más reciente que no diga “SNAPSHOT” debido a que son versiones en prueba y que pueden contener fallos:

Posteriormente seleccionaremos la opción de “JAVA” y daremos un “Group Id” que tenga sentido para nosotros, en mi caso “com.github”, ya que posteriormente será parte de mi repositorio personal:

A continuación, en mi caso lo llamaré similar a mi repositorio en Github para mantener coherencia “inventorysaleshub”:

Seguidamente seleccionaremos “Jar” y la versión en Java que tenemos configurada, en mi caso “23”, si no aparece pueden seleccionar cualquiera y cambiaremos la versión en el “Pom” posteriormente:

Luego buscaremos y seleccionaremos las dependencias requeridas:

Spring Web (para APIs REST).

Spring Boot DevTools (reinicio automático por cada cambio).

Thymeleaf (motor de plantillas para Java).

Spring Data JPA (para la base de datos).

Spring Security (para autenticación).

Al finalizar le daremos a “Enter” y seleccionaremos la carpeta donde guardaremos nuestro proyecto.

Si todo es correcto veremos una estructura similar a esta:

Estructura necesaria para iniciar nuestro proyecto

Generamos una estructura de paquetes clara, recordando que se debe usar nombres en singular para mantener el estándar en Spring Boot de la siguiente manera:

Crearemos dentro de nuestro main y dentro de nuestro paquete “inventorysaleshub”, tres nuevos paquetes” model, repository y controller”; Dentro de cada uno de ellos haremos su clase correspondiente, tener presente que repository será una interfaz “model -> Product.java”, “repository -> ProductRepository.java”, “controller -> ProductController.java”.

Y así tendremos la siguiente estructura:

controller/ → Maneja las solicitudes HTTP.

model/ → Representa los datos de la aplicación.

repository/ → Se encarga de acceder a la base de datos.

InventorysaleshubApplication.java → Es el punto de entrada del proyecto.

¿Ejemplos de implementación MVC?

Entidades: En nuestro paquete “model”, crearemos clases que representarán las entidades principales para nuestra base de datos, por ejemplo: Product.java:

¿Qué hace cada parte del código?

@Entity → Indica que esta clase se convertirá en una tabla en la base de datos.@Id → Define el campo clave primaria, lo que significa que cada producto tendrá un ID único.@GeneratedValue(strategy = GenerationType.IDENTITY) → Hace que la base de datos genere automáticamente los valores del ID (1, 2, 3, etc.), sin que el usuario los asigne manualmente.

Repositorios: En nuestro paquete “repository” generaremos interfaces que en el contexto de Spring Boot y Java Persistence, se encargaran de interactuar con la base de datos y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en las entidades.

Por ejemplo: Podemos crear una interfaz “ProductRepository”:

¿Qué hace cada parte del código?

public interface ProductRepository → Define una interfaz en lugar de una clase.

extends JpaRepository → Es una interfaz de Spring Data JPA que proporciona métodos CRUD (Create, Read, Update, Delete) listos para usar.

<Product, Long > → Product es la entidad que representa la tabla en la base de datos. Long es el tipo de dato de la clave primaria (ID) de la entidad Product.

Funcionalidad principal:

Cuando ProductRepository extiende JpaRepository, hereda varios métodos útiles para manejar datos de Product sin necesidad de escribir SQL. Por ejemplo:

findAll(): Recupera todos los registros de la tabla.

findById(Long id): Encuentra un producto por su ID.

save(Product product): Guarda un nuevo producto o actualiza uno existente.

deleteById(Long id): Elimina un producto por su ID.

Así, el repositorio permite gestionar Product de manera eficiente sin tener que escribir consultas manuales.

Controladores: Esta es una clase que gestiona las peticiones HTTP y define cómo debe responder la aplicación a esas solicitudes. Es decir, conecta la lógica del backend con el frontend o con clientes externos.

Por ejemplo: Crearemos en el paquete “controller” una clase llamada “ProductController”:

¿Qué hace cada parte del código?

@RestController Convierte la clase en un controlador REST, permitiendo responder con datos JSON.

@RequestMapping("/productos") Define la ruta base para acceder al recurso de productos.

@Autowired Spring inyecta automáticamente ProductRepository, lo que permite interactuar con la base de datos sin necesidad de instanciarlo manualmente.

@GetMapping Especifica que este método responde a solicitudes HTTP GET.

productRepository.findAll() Devuelve todos los productos almacenados en la base de datos en forma de lista.

Funcionalidad principal:

Este código define un API REST en Java con Spring Boot, permitiendo a los clientes solicitar una lista de productos mediante una llamada GET a /products. Gracias a la inyección de dependencias, el controlador accede eficientemente a los datos sin necesidad de inicializar el repositorio manualmente.

Creación de la base de datos y gestión de datos (Model)

Definir las entidades principales:

1. ¿Qué es un modelo entidad-relación (ER)?

Un modelo entidad-relación (ER) es una forma de organizar y representar los datos de un sistema usando entidades, atributos y relaciones.¿Por qué es importante?

Ayuda a visualizar cómo los datos están conectados.

Facilita la planificación de la base de datos antes de implementarla.

Evita errores al diseñar el sistema.

2. Partes principales de un modelo ER

Entidad → Representa un objeto real con características únicas. Ejemplo: Usuario, Producto, Pedido.

Atributo → Es una característica de una entidad. Ejemplo: nombre (de un usuario), precio (de un producto).

Relación → Conecta dos entidades. Ejemplo: "Un Usuario hace muchos Pedidos".

Cardinalidad → Define cuántos elementos de una entidad están relacionados con otra (Uno a Uno, Uno a Muchos, Muchos a Muchos).

3. ¿Cómo definir un modelo ER?

Para diseñar un modelo entidad-relación, sigue estos pasos:

Identifica las entidades clave → ¿Qué objetos necesita el sistema? (Usuario, Producto, Pedido...).

Define los atributos esenciales → ¿Qué características tiene cada entidad? (nombre, precio, stock...).

Establece las relaciones → ¿Cómo interactúan las entidades entre sí? (Usuario hace Pedidos, Pedido contiene Productos).

Dibuja un diagrama ER → Usa herramientas como Draw.io, Lucidchart, Umbrello UML o MySQL Workbench para representar visualmente la base de datos.

Convierte el diseño en código → Usa JPA en Spring Boot para implementar el modelo en Java.

4. Consejos para diseñar un buen modelo ER

Evita redundancias → No guardes la misma información en varias entidades.

Usa claves foráneas (@ManyToOne) para conectar entidades correctamente.

Define bien la cardinalidad → ¿Es necesario que una entidad tenga varias relaciones o solo una?

Piensa en escalabilidad → ¿Tu modelo soportará cambios futuros sin problemas?

Por ejemplo:

5. ¿Qué sigue ahora?

Antes de escribir código, piensa qué entidades son necesarias. Para un sistema de gestión de inventario y ventas como es este proyecto podemos utilizar, por ejemplo:

User → Representa a los clientes o administradores.

Product → Información sobre los productos en stock.

Order → Registra las compras realizadas por los usuarios.

Category → Organiza los productos por tipos.

OrderDetails → Relación entre un pedido y los productos comprados.

Pay → Registra el método de pago y el estado de este (efectivo, tarjeta, transferencia).

Invoice → Genera un comprobante de compra para cada pedido.

Suplier → Guarda información sobre los proveedores de los productos.

Store → Gestiona la ubicación de los productos en distintos almacenes o sucursales.

Role → Si deseas manejar permisos, puedes diferenciar entre Administradores y Clientes.

ProductHistory → Para registrar cambios en precios o movimientos de stock.

Como realizar nuestra Base de datos

Uso de Spring Boot (Spring Data JPA)

En un inicio habíamos definido la dependencia Spring Data JPA, ahora explicaré su uso ya que Spring Boot permite definir la base de datos directamente desde el código gracias a esta herramienta, sin necesidad de crear manualmente las tablas en programas externos como: DBeaver, MySQL Workbench o pgAdmin. Aunque no significa que no se puedan utilizar para visualizar las tablas y hacer consultas manualmente.

En Spring Boot con JPA cada entidad debe tener su propia clase en Java, porque cada clase representa una tabla en la base de datos; Estas deben crearse dentro del paquete model

¿Por qué crear una clase por entidad?

Organización → Mantiene el código estructurado y facilita su mantenimiento.

Integración con JPA → Spring Boot usa cada clase como una tabla en la base de datos.

Facilidad de expansión → Puedes agregar atributos y relaciones sin afectar otras clases.

Por lo que tendremos al final algo similar a esta estructura:

Tener en cuenta los siguientes aspectos:

Debes crear estas clases en Java dentro de la carpeta model/.

Cada una de estas clases debe llevar la anotación @Entity para que JPA las convierta en tablas.

@Id → Establece la clave primaria.

@GeneratedValue(strategy = GenerationType.IDENTITY) → Para generar automáticamente los IDs.

Relaciones (@OneToMany, @ManyToOne, @ManyToMany) según corresponda.

Por Ejemplo (nuestra clase User) se definiría así:

Como aclaración quiero comentar que hay dos tipos de import que podremos utilizar, el jakarta.persistence y el org.springframework.data.annotation, utilizaremos jakarta.persistence ya que nos dará las siguientes ventajas:

Es la versión estándar para trabajar con JPA y mapeo de entidades.

Permite que Spring Boot genere correctamente las tablas en la base de datos.

Compatible con las anotaciones como @GeneratedValue, @OneToMany, @ManyToOne, etc.

Mientras que org.springframework.data.annotation, se usa en algunos casos específicos de Spring Data MongoDB, pero no es la opción adecuada para bases de datos relacionales con JPA.

Ejemplo:

Un Usuario (User) puede hacer muchos Pedidos (Orders), pero un Pedido (Order) pertenece a un solo Usuario (User).

La clave foránea está en Pedido (Order), y user_id conecta con Usuario (User).

¿Qué hace cada parte del código?

Entidad (User):

Definición como entidad JPA:

La anotación @Entity indica que esta clase será mapeada a una tabla en la base de datos.

Identificación única (id):

Se usa @Id para definir el campo como clave primaria.

@GeneratedValue(strategy = GenerationType.IDENTITY) permite que el valor de id se genere automáticamente.

Campos name e email:

Se definen como atributos de tipo String para almacenar el nombre y correo electrónico del usuario.

Relación con Order:

Se establece una relación uno a muchos con la entidad Order usando @OneToMany(mappedBy = "user").

Esto significa que un usuario puede tener varios pedidos, pero cada pedido pertenece a un solo usuario.

Entidad (Order)

Definición como entidad JPA:

Al igual que User, @Entity indica que Order se mapeará a una tabla en la base de datos.

Identificación única (id):

Se usa @Id para definir el campo como clave primaria.

@GeneratedValue(strategy = GenerationType.IDENTITY) permite que el valor de id se genere automáticamente.

Campo date:

Se define como LocalDate para almacenar la fecha en que se realizó el pedido.

Relación con User:

Se usa @ManyToOne para indicar que varios pedidos pueden pertenecer a un solo usuario.

@JoinColumn(name = "user_id") establece que la columna user_id en la base de datos almacenará la clave foránea del usuario.

Relación con Product:

Se usa @ManyToMany para indicar que un pedido puede incluir varios productos, y un producto puede estar en varios pedidos.

Ahora bien, si queremos realizar un código más limpio y no poner varios imports, ten presente que podemos poner un (*) que nos ayudará a traer todo lo que necesitamos.

Por ejemplo: Con nuestra clase Category.java, al crear el código solo pondremos un import jakarta.persistence.* <- Y el asterisco final nos traerá todos los demás automáticamente:

Relaciones completas entre entidades

Como en un inicio solo realizamos ejemplos de lo principal que necesitábamos para formar nuestra estructura de Base de datos ahora debemos completar cada entidad y sus relaciones completas de la siguiente manera:

Category:

Que contiene:

Atributo: nombre.

Relación uno a muchos con Product.

Invoice:

Que contiene:

Atributos: fecha de emisión, monto total.

Relación uno a uno con Order.

Order:

Que contiene:

Atributos: dateCreated, status, total.

@ManyToOne User Muchos pedidos pueden pertenecer a un mismo usuario.

@OneToMany OrderDetails Un pedido puede tener muchos productos (líneas de pedido).

@OneToOne Invoice Un pedido tiene una única factura.

@OneToOne Pay Un pedido tiene una única forma de pago

OrderDetails:

Que contiene:

Atributos: cantidad, precio unitario.

Muchos a uno con Order.

Muchos a uno con Product

Pay:

Que contiene:

Atributos: método de pago, estado.

Relación uno a uno con Order

Product:

Que contiene:

Atributos: nombre, descripción, precio, stock.

Muchos a uno con Category, Supplier, y Store.

Uno a muchos con OrderDetails (cuando aparece en un pedido).

Uno a muchos con ProductHistory (registro de cambios).

ProductHistory:

Que contiene:

Atributos: acción realizada, fecha/hora.

Relación muchos a uno con Product.

Role:

Que contiene:

Atributo: nombre del rol.

Relación uno a muchos con User.

Store:

Que contiene:

Atributos: nombre, ubicación.

Relación uno a muchos con Product

Supplier:

Que contiene:

Atributos: nombre de la empresa, información de contacto.

Relación uno a muchos con Product

User:

Que contiene:

Atributos: nombre, correo electrónico.

Relación muchos a uno con Role.

Uno a muchos con Order.

