# ProductRequestDTO:
Posteriormente podemos generar el PostMapping, para esto primero inyectaremos el repositorio CategoryRepository en producto controller agregando la siguiente línea:

Luego de su inyección adaptaremos el código PostMapping así:

↓

Hemos realizado unos cambios importantes que explicaré con un poco más de detalle:

Antes:

Entrada: Recibía un objeto completo Product desde el cliente, lo cual es riesgoso porque el cliente puede manipular atributos sensibles (ej. ID, relaciones).

Proceso: guardaba el producto directamente y registraba un historial (ProductHistory).

Salida: devolvía la entidad Product completa al cliente, lo cual expone la estructura interna de la base de datos.

Ahora:

Entrada: Recibe un ProductRequestDTO, que contiene solo los campos necesarios para crear un producto (name, description, price, stock, categoryId).

Controla qué datos puede enviar el cliente.

Proceso:

Busca la categoría por ID (categoryRepository.findById(...)) usando el valor del DTO.

Construye un objeto Product manualmente a partir de los datos del DTO.

Guarda el producto en la base de datos.

(Falta reinsertar el guardado de ProductHistory, que podría volver a agregar con DTOs si lo deseas).

Salida: Devuelve un ProductDTO, no la entidad.

Solo muestra los datos que realmente interesan al cliente (ej. id, nombre, precio, categoría).

No expone relaciones internas como ProductHistory.

Nota: Recuerda revisar que en model.Product debemos tener bien configurados los setters y getters de category: public Category getCategory() { return category; }  public void setCategory(Category category) { this.category = category; }. Adicionalmente, tener todos los Imports necesarios para su correcto funcionamiento.

@DeleteMapping("/{id}") es un método que no necesitamos modificar

¿Por qué?

Aquí solo devuelve ResponseEntity<Void> (sin cuerpo).No hace falta DTO porque no responde un objeto, solo un 204 No Content.

Los métodos que adaptaremos son porque devuelven entidades Product directamente, esto para evitar que los clientes o usuarios que no correspondan modifiquen datos importantes como:

Productos por categoría:

↓

Buscar productos por nombre:

↓

Historial de producto: Aquí devuelve ProductHistory, por lo que, si el historial se usa solo internamente, podemos dejarlo así, pero si lo exponemos a clientes/usuarios, sería mejor un ProductHistoryDTO para no devolver la entidad completa, que es lo que realizaremos en esta parte.

# ProductHistoryDTO:
Ahora podremos adaptar en el controlador el historial del producto:

↓

Productos con stock bajo:

↓

Paginación:

↓

Ordenar productos:

↓

¿Por qué hemos realizado todos estos cambios?

Todos los cambios los hicimos para:

Proteger la API → que un usuario no pueda acceder ni enviar información indebida.

Definir contratos claros → qué recibe y qué devuelve cada endpoint.

Mantener el sistema flexible y seguro → entidades para la BD, DTOs para la API.

# DTOs para respuestas complejas:
Un DTO para respuestas complejas es un objeto de transferencia de datos diseñado para agrupar y estructurar información proveniente de múltiples entidades o fuentes antes de enviarla al cliente (API, frontend, móvil).

En lugar de devolver directamente las entidades del modelo (que suelen tener relaciones internas, atributos sensibles o estructuras difíciles de consumir), se construye un DTO personalizado que:

Combina datos de varias entidades relacionadas.

Filtra y expone solo los campos relevantes.

Presenta la información de forma clara, lista para el consumo en la interfaz de usuario.

¿Para qué sirven?

# OrderResponseDTO:
Nota: Utilizaremos InvoiceDTO y payDTO, que anteriormente ya teníamos creados y configurados y crearemos OrderItemDTO. Con esto, el endpoint getOrderById puede devolver un OrderResponseDTO bien estructurado.

# OrderItemDTO:
# DTO genérico “<T>” para respuestas estándar:
Para este punto podemos crear un ApiResponseDTO<T>, este será un envoltorio genérico que sirve para estandarizar todas las respuestas de la API, sin importar si son de éxito o error.

El <T> indica que tu clase es genérica, es decir, que puede trabajar con cualquier tipo de dato sin que tengas que crear una clase distinta por cada caso.

# Cuando la defines → escribes public class ApiResponseDTO<T> de la siguiente manera:
Nota: Tener presente que, si decidimos aplicar un DTO genérico, tendremos que refactorizar todos nuestros controladores ya creados, sin embargo, todos quedarían alineados con DTOs y ApiResponseDTO.

# Refactor ProductController con ApiResponseDTO:
# Refactor OrderResponseDTO:
# Refactor OrderController con ApiResponseDTO:
Nota: Ten presente que para que esta refactorización funcione correctamente tendremos que crear y modificar nuestros DTOs (OrderItemDTO, OrderItemRequestDTO, OrderRequestDTO, OrderResponseDTO) y algunas pequeñas adaptaciones en nuestros modelos (Order y OrderDetails), cambios los cuales se mostraran a continuacion.

# OrderItemDTO:
# OrderItemRequestDTO:
# OrderRequestDTO:
# OrderResponseDTO:
Order(MODEL):

Aquí los cambios han sido muy sutiles pero importantes hemos cambiado la entidad Order a un método setOrderDetails() porque en la clase teníamos definido el campo como details y no como orderDetails, entonces para darle claridad a la variable haremos este cambio y generaremos el getter y setter correspondientes:

↓

OrderDetails(MODEL):

Para OrderDetails muy parecido como hemos hecho con Order, los cambios fueron sutiles pero importantes, en este caso solo hemos agregado los setters correspondientes para Product y Order:

¿Por qué es tan importante?

Los setters permiten construir y modificar entidades en tiempo de ejecución (desde los DTOs o la base de datos). Sin ellos, no podríamos asignar productos a un OrderDetails, ni vincular un OrderDetails a un Order.

# Refactor InvoiceController con ApiResponseDTO:
Nota: Ten presente que para que esta refactorización funcione correctamente tendremos que crear nuestro DTO (InvoiceRequestDTO) y algunas pequeñas adaptaciones en nuestro modelo (Invoice), cambios los cuales se mostraran a continuación.

# InvoiceRequestDTO:
Invoice(MODEL):

Debemos agregar el setter correspondiente para Order:

# Refactor PayController con ApiResponseDTO:
Nota: Ten presente que para que esta refactorización funcione correctamente tendremos que crear nuestro DTO (PayRequestDTO) y algunas pequeñas adaptaciones en nuestro modelo (Pay), cambios los cuales se mostraran a continuación.

# PayRequestDTO:
Pay(MODEL):

Debemos agregar el setter correspondiente para Order:

# Refactor UserController con ApiResponseDTO:
Changelog de DTOs y Refactorización de Controladores

Este documento registra la creación e integración de DTOs (Data Transfer Objects) en el proyecto, así como la refactorización de controladores para usarlos junto con ApiResponseDTO.

Estandarización de Respuestas

Se creó ApiResponseDTO<T>, una clase genérica para todas las respuestas.

Atributos:

boolean success → indica si la operación fue exitosa.

String message → mensaje de estado.

T data → datos devueltos.

Uso: todos los controladores devuelven respuestas en este formato.

UserController

# Request DTOs:
UserRequestDTO → recibe datos al crear/actualizar un usuario (name, email, roleId).

# Response DTOs:
UserDTO → devuelve datos de usuario (id, name, email, roleName).

Endpoints afectados:

POST /users → usa UserRequestDTO.

PUT /users/{id} → usa UserRequestDTO.

GET /users → devuelve lista de UserDTO.

GET /users/{id} → devuelve un UserDTO.

ProductController

# Request DTOs:
ProductRequestDTO → recibe datos de producto (name, price, stock, categoryId).

# Response DTOs:
ProductDTO → devuelve datos de producto (id, name, price, stock, categoryName).

Endpoints afectados:

POST /products → usa ProductRequestDTO.

PUT /products/{id} → usa ProductRequestDTO.

GET /products → devuelve lista de ProductDTO.

GET /products/{id} → devuelve un ProductDTO.

Otros endpoints (delete, history, stock/low, etc.) → devuelven ProductDTO o colecciones.

OrderController

# Request DTOs:
OrderRequestDTO → recibe información de una orden (items).

OrderItemRequestDTO → representa un producto en la orden (productId, quantity, price).

# Response DTOs:
# OrderResponseDTO → devuelve información completa de la orden:
id, dateCreated, status

items (lista de OrderItemDTO)

invoice (InvoiceDTO)

pay (PayDTO)

Endpoints afectados:

POST /orders → usa OrderRequestDTO.

GET /orders → devuelve lista de OrderResponseDTO.

GET /orders/{id} → devuelve un OrderResponseDTO.

InvoiceController

# Request DTOs:
InvoiceRequestDTO → recibe datos para crear facturas (issueDate, totalAmount, orderId).

# Response DTOs:
InvoiceDTO → devuelve datos de factura (id, issueDate, totalAmount, orderId).

Endpoints afectados:

POST /invoices → usa InvoiceRequestDTO.

GET /invoices → devuelve lista de InvoiceDTO.

GET /invoices/{id} → devuelve un InvoiceDTO.

PayController

# Request DTOs:
PayRequestDTO → recibe datos para crear pagos (method, status, orderId).

# Response DTOs:
PayDTO → devuelve datos de pago (id, method, status, orderId).

Endpoints afectados:

POST /payments → usa PayRequestDTO.

GET /payments → devuelve lista de PayDTO.

GET /payments/{id} → devuelve un PayDTO.

Cambios en Entidades

Order

Renombrado campo details → orderDetails.

Se añadieron getters/setters para orderDetails.

OrderDetails

Se añadieron setters para order, product, quantity, price.

Invoice y Pay

Se añadieron setters para soportar mapeo desde DTOs.

Seguridad y Autenticación

En este capítulo implementaremos la seguridad en nuestra API utilizando Spring Security y JWT (JSON Web Token). El objetivo es proteger los recursos del sistema, controlar el acceso a los endpoints según el rol del usuario (ejemplo: ADMIN, USER) y manejar la autenticación de manera estandarizada mediante tokens.

Al finalizar este módulo:

Nuestra API solo permitirá el acceso a usuarios autenticados.

Los endpoints estarán protegidos según permisos y roles.

La autenticación y el registro de usuarios estarán totalmente integrados en el sistema.

Lo que veremos en este capítulo

Configurar Spring Security en el proyecto

Añadir dependencias necesarias.

Crear la clase SecurityConfig para personalizar la seguridad.

Habilitar JWT (JSON Web Token)

Configurar un JwtProvider que pueda generar y validar tokens.

Implementar filtros para que cada request valide el token antes de acceder a los recursos.

Proteger endpoints según el rol del usuario

Definir roles (ADMIN, USER, etc.) en la entidad Role.

Configurar reglas de acceso en SecurityConfig.

Crear DTOs de autenticación

LoginRequestDTO → para enviar credenciales (email, password).

LoginResponseDTO → para devolver el token JWT y datos básicos del usuario.

RegisterDTO → para registrar nuevos usuarios con credenciales seguras.

Implementar servicios de autenticación

AuthService que maneje el registro, inicio de sesión y validación de tokens.

Integración con UserRepository y RoleRepository para validar usuarios.

Integrar seguridad con las entidades User y Role

Asociar cada usuario a un rol.

Definir permisos para proteger controladores y operaciones específicas.

Configuración de Spring Security:

¿Qué es Spring Security?

Es un framework de Java diseñado para proteger aplicaciones desarrolladas con el ecosistema Spring. Es el estándar de facto para implementar autenticación y autorización, y ofrece una gran flexibilidad para adaptarse a las necesidades específicas de cada proyecto.

¿Qué ofrece Spring Security?

- Autenticación: Verifica la identidad del usuario (por ejemplo, mediante usuario y contraseña).

- Autorización: Controla qué recursos puede acceder cada usuario según sus roles o permisos.

- Protección contra ataques comunes: Incluye medidas contra session fixation, clickjacking, CSRF (Cross-Site Request Forgery), entre otros.

- Integración fluida: Funciona perfectamente con Spring Boot, Spring MVC y otros módulos del ecosistema Spring.

- Alta personalización: Puedes definir reglas de seguridad específicas, integrar JWT, OAuth2, LDAP, y mucho más.

¿Por qué usarlo?

Porque escribir lógica de seguridad desde cero es tedioso y propenso a errores. Spring Security te da una base sólida, extensible y probada para proteger tu aplicación sin reinventar la rueda.

Antes de empezar realizaremos los DTOs de autenticación y registro.

¿Por qué?

Nos permiten definir cómo entran y salen los datos en login/register.

Después conectamos esos DTOs con los servicios (AuthService, JwtProvider) y finalmente con la configuración de seguridad (SecurityConfig).

# LoginRequestDTO:
# LoginResponseDTO:
Usado para devolver un token JWT y algunos datos básicos del usuario tras el login.

Con esto tenemos los DTOs básicos para autenticación y registro.

Lo siguiente sería implementar un JwtProvider para generar/validar los tokens y el AuthService con sus métodos (login, register, validateToken).

JwtProvider

Este componente se encarga de:

Generar tokens JWT al momento de login.

Validar tokens en cada request.

Extraer el email/usuario desde el token.

Para comenzar debemos crear una nueva carpeta en nuestra estructura de paquetes,  a esta podremos llamarla Security, config.security o como prefieras y ahí dentro colocaremos la clase JwtProvide, por ejemplo, en mi caso la he llamado Security:

Posteriormente, debemos incluir la librería de JWT que no está incluida en Spring Boot por defecto. Agregaremos estas dependencias dentro de <dependencies> en nuestro POM:

¿Qué hace cada uno?

jjwt-api → La API principal de JJWT.

jjwt-impl → La implementación real (necesaria en runtime).

jjwt-jackson → Soporte para trabajar con JSON (usado al parsear tokens).

# SalesSummaryDTO en OrderRepository:
↓

# TopProductDTO en OrderDetailsRepository:
↓

# TopCustomerDTO en OrderRepository:
Nota: Como es un repositorio que ya habíamos modificado previamente los nuevos cambios los encerrare en rojo para que sea fácil de identificarlos

↓

LowStockProductDTO en ProductRepository

↓

Ahora vamos a refactorizar el DashboardController para que en lugar de devolver mocks use las consultas reales que definimos en los repositorios.

Refactor DashboardController

Explicación de los cambios:

Inyectamos los repositorios (OrderRepository, OrderDetailsRepository, ProductRepository) en el controlador.

Cada endpoint ahora llama al repositorio correspondiente en lugar de devolver datos mock.

En /sales-summary añadimos control de valores null (cuando aún no existen pedidos en la base de datos).

Todas las respuestas siguen usando ApiResponseDTO para mantener consistencia.

Adición de métricas

El siguiente paso para enriquecer el Dashboard es añadir estadísticas de ventas por mes, ya que son fundamentales para un administrador:

Permiten ver tendencias.

Se pueden graficar fácilmente (líneas, barras).

Ayudan en la toma de decisiones.

Esto lo realzaremos en 3 sencillos pasos:

# Crear un nuevo DTO MonthlySalesDTO:
En OrderRepository, añadir consulta:

# Crear el DTO MostProfitableProductDTO:
Query en OrderDetailsRepository:

# Crear el DTO NewCustomersDTO:
Query en UserRepository:

# 1. Creamos un DTO genérico para KPIs:
2. Extendemos los Repositories con KPIs:

OrderRepository

PayRepository

# Cada método representa una operación HTTP (GET, POST, PUT, DELETE). Se usan DTOs para enviar/recibir datos:
@ApiOperation(value = "Obtener todas las categorías")

@GetMapping

public ResponseEntity<List<CategoriaDto>> getAllCategorias() {

List<Categoria> categorias = categoriaService.findAll();

List<CategoriaDto> categoriasDto = categorias.stream()

.map(cat -> modelMapper.map(cat, CategoriaDto.class))

.collect(Collectors.toList());

return ResponseEntity.ok(categoriasDto);

}

InvoiceController con ModelMapper + Swagger

Los DTOs (Data Transfer Objects) son clases que representan los datos que se exponen en la API. ModelMapper se encarga de convertir entre la entidad y el DTO:

public class CategoriaDto {

private Long id;

private String nombre;

private Integer estado;

}

PayController con ModelMapper + Swagger

Se puede personalizar el mapeo si los campos no coinciden directamente:

@Bean

public ModelMapper modelMapper() {

ModelMapper mapper = new ModelMapper();

mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

return mapper;

}

Con esto ya tenemos cerradas todas las entidades principales (Product, User, Order, Invoice, Pay) bajo el mismo estándar. Para llevar a cabo la misma configuración en las secundarias debemos implementar nuevos DTOs de la siguiente manera:

CategoryDTO

CategoryRequestDTO

CategoryController con ModelMapper + Swagger

RoleDTO

RoleRequestDTO

RoleController con ModelMapper + Swagger

StoreDTO

StoreRequestDTO

StoreController con ModelMapper + Swagger

SupplierDTO

SupplierRequestDTO

SupplierController con ModelMapper + Swagger

DashboardController con ModelMapper + Swagger

En este caso no crearemos DTOs adicionales para Dashboard, ya que en nuestro DashboardController actual, cada endpoint ya devuelve su propio DTO especializado:

/sales-summary → SalesSummaryDTO

/top-products → TopProductDTO

/top-customers → TopCustomerDTO

/low-stock → LowStockProductDTO

/monthly-sales → MonthlySalesDTO

/most-profitable-products → MostProfitableProductDTO

/new-customers → NewCustomersDTO

Y por último nuestro AuthController, para este controlador tampoco necesitaremos crear DTOs adicionales, utilizaremos los previamente creados (LoginResquetDTO, LoginResponseDTO, RegisterDTO) por lo que los refactorizaremos en conjunto con el controlador.

Refactor LoginRequestDTO

Refactor LoginResponseDTO

Refactor RegisterDTO

AuthController con ModelMapper + Swagger

Con esto hemos refactorizado y agregado de manera correcta la documentación en nuestra API con Swagger/OpenApi.

Despliegue y ejecución del proyecto

