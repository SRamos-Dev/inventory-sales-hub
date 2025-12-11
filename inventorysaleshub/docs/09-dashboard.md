# En el DashboardController, nuevo endpoint:
Ahora bien, ya teniendo claros estos 3 pasos cada vez que queramos añadir una métrica adicional solo debemos seguir la misma secuencia para su inyección en nuestro código, sin que afecte su funcionamiento y sea más profesional, por ejemplo:

Productos más rentables

Nuevos clientes por mes

Etc…

Ejemplo 1:

# Endpoint en DashboardController:
Ejemplo 2:

# Endpoint en DashboardController: Para este Endpoint especifico tendremos que hacer unos ajustes adicionales:
Inyectar el repositorio: import com.github.inventorysaleshub.repository.UserRepository;

# Resumen General de Endpoints en Dashboard:
El Dashboard Administrativo permite a los administradores obtener estadísticas clave sobre ventas, productos y usuarios.

Todos los endpoints están bajo el prefijo: /dashboard

1. Resumen de ventas: GET /dashboard/sales-summary

Devuelve:

Total de pedidos (totalOrders)

Ingresos totales (totalRevenue)

Valor promedio de pedido (averageOrder)

Uso: Vista rápida del rendimiento general del negocio.

2. Productos más vendidos: GET /dashboard/top-products

Devuelve:

ID del producto

Nombre del producto

Cantidad total vendida

Uso: Identificar los artículos más demandados.

3. Clientes más activos: GET /dashboard/top-customers

Devuelve:

ID del cliente

Nombre del cliente

Cantidad de pedidos realizados

Total gastado

Uso: Reconocer a los clientes más valiosos.

4. Productos con stock bajo: GET /dashboard/low-stock

Devuelve:

ID del producto

Nombre del producto

Stock actual

Uso: Detectar productos que necesitan reposición.

5. Ventas por mes: GET /dashboard/monthly-sales

Devuelve:

Año (year)

Mes (month)

Total de ventas (totalSales)

Uso: Analizar tendencias de ventas a lo largo del tiempo.

6. Productos más rentables: GET /dashboard/most-profitable-products

Devuelve:

ID del producto

Nombre del producto

Ingresos generados

Uso: Conocer los productos que más dinero aportan.

7. Nuevos clientes por mes: GET /dashboard/new-customers

Devuelve:

Año (year)

Mes (month)

Total de usuarios registrados

Uso: Medir el crecimiento de la base de clientes.

Adiciones que pueden hacer nuestro Dashboard más dinámico (Opcional)

Filtros dinámicos

Ventas entre fechas (from, to).

Clientes top por periodo.

Productos top por periodo.

KPIs extra

Total de clientes registrados.

Número de usuarios activos (los que hicieron un pedido en el último mes).

Pedidos cancelados / pagos fallidos.

Visualización

Actualmente solo devuelves datos crudos (listas, DTOs).

Se podría agregar un endpoint que entregue los datos ya formateados para gráficas (por ejemplo, agrupados para una gráfica de barras o un gráfico circular).

Seguridad avanzada

Ya agregamos @PreAuthorize para restringir qué métricas ve un USER vs un ADMIN.

Podríamos refinar aún más (ej: permitir que un usuario vea solo sus estadísticas personales).

Filtros dinámicos: añadir queries en los repositorios y endpoints en DashboardController para poder pedir métricas entre dos fechas (from / to).

Agregar en OrderRepository:

# Agregar en DashboardController:
Agregar en TopCustomerDTO: Un constructor adicional que reciba solo (id, name, count) y deje totalSpent = 0.

KPIs extra: La idea aquí es ampliar la información estratégica más allá de lo que ya tenemos (ventas, clientes, productos, etc.). Por lo que pondremos 4 KPIs estratégicos:

Customer Retention Rate

Average Order Frequency

Gross Profit Margin

Average Payment Time

# Nuevos endpoints en DashboardController:
Inyectaremos PayRepository como lo hicimos con los anteriores

Y posteriormente los endpoints

Ahora para que todo funcione correctamente debemos asegurarnos de que los nombres de las tablas coincidan con los nombres brindados en nuestro PayRepository, por lo que para dar los nombres en la tabla que deseemos, ya que si no anotamos nuestras entidades con @Table(name = "X"), Hibernate usará el nombre de la clase en minúsculas como nombre de tabla, lo cual sonaría bien de no ser por el detalle de las palabras reservadas de SQL, que no podemos utilizar o nos genera problemas como por ejemplo order  que al ser palabra reservada no se creó a tabla correspondiente en nuestra base de datos en DBeaver.

Por lo tanto, agregaremos los @Table y así nos aseguraremos de que no choquen con palabras reservadas como pasa con order.

Pay(Tabla = Payments)

Order(Tabla = Orders)

Category(Tabla = cetegories)

Invoice(Tabla = Invoices)

OrderDetails(Tabla = order_details)

Product(Tabla = products)

ProductHistory(Tabla = producto_history)

Role(Tabla = roles)

Store(Tabla = stores)

Supplier(Tabla = suppliers)

User(Tabla = users)

Documentación de la API (Swagger/OpenAPI)

La documentación de la API con Swagger/OpenAPI es una herramienta clave en el desarrollo moderno de software, especialmente cuando se trabaja con APIs RESTful.

¿Qué es Swagger/OpenAPI?

OpenAPI Specification (OAS): Es un estándar para describir APIs REST de forma legible tanto por humanos como por máquinas. Antes se conocía como Swagger, pero desde 2016 se renombró como OpenAPI.

Swagger: Es un conjunto de herramientas que trabaja con OpenAPI para crear, visualizar, probar y mantener documentación de APIs. Incluye Swagger UI, Swagger Editor y Swagger Codegen.

¿Para qué sirve?

Documentación clara y estructurada: Define los endpoints, métodos, parámetros y respuestas de una API, facilitando su comprensión por parte de desarrolladores y usuarios técnicos.

Exploración interactiva: Con herramientas como Swagger UI, puedes probar la API directamente desde el navegador, sin necesidad de escribir código adicional.

Generación automática de código: Swagger Codegen permite crear clientes y servidores en varios lenguajes a partir de la definición de la API.

Mejora la colaboración: Facilita la comunicación entre equipos de desarrollo, QA y producto, reduciendo errores y malentendidos.

Acelera el desarrollo: Al tener una especificación clara, se puede trabajar en paralelo en el frontend y backend.

Y de acuerdo con esta información veremos en este capítulo los siguientes puntos:

1. Agregar Springdoc OpenAPI al proyecto con Maven.

2. Configurar Swagger UI para que documente automáticamente todos tus controladores.

3. Personalizar la documentación (nombre del proyecto, descripción, versión).

4. (Opcional) Proteger Swagger con roles → que solo ADMIN pueda acceder.

Springdoc OpenAPI

Agregar dependencia en Maven

Acceder a la documentación:

Cuando levantes la aplicación, podremos acceder a:

Swagger UI (interfaz interactiva): http://localhost:8080/swagger-ui.html

Esquema OpenAPI en JSON: http://localhost:8080/v3/api-docs

Configuración opcional – Info de la API:

Si queremos personalizar la documentación (nombre del proyecto, descripción, versión), será necesario crear una clase OpenApiConfig:

OpenApiConfig

Explicación

Con esto ya tendremos:

Documentación automática de todos los endpoints.

Una interfaz gráfica para probarlos sin Postman.

Información personalizada de tu API.

Por defecto, Springdoc ya documenta todos nuestros endpoints, pero si queremos que se vean más claros y profesionales, podemos añadir descripciones, parámetros y ejemplos.

Sin embargo, para mejorar aun más nuestro proyecto usaremos y no tan grande o engorroso utilizaremos un ModelMapper, que nos permitirá reducir el código y cuando lo hayamos refactorizado explicaré el uso de OpenAPI que ya tendremos incluido en cada controlador.

ModelMapper

¿Qué es?

Un ModelMapper es una biblioteca de Java diseñada para facilitar la conversión entre objetos, especialmente útil cuando trabajas con DTOs (Data Transfer Objects) y modelos de dominio.

¿Qué hace ModelMapper?

ModelMapper automatiza el proceso de mapear datos entre objetos que tienen estructuras similares, pero no necesariamente idénticas. Por ejemplo, si tienes una clase Usuario y quieres convertirla en UsuarioDTO, ModelMapper puede hacerlo sin que tengas que escribir todo el código de conversión manualmente.

Ventajas de usar ModelMapper

Menos código repetido → ya no necesitas construir entidades manualmente en cada controlador.

Centralización → la lógica de conversión DTO ↔ Entity queda fuera de los controladores.

Escalabilidad → si agregas más campos a tus entidades/DTOs, solo ajustas el mapeo en un lugar.

Legibilidad → los controladores se enfocan en la lógica de negocio, no en transformaciones de datos.

Dependencia en Maven:

Configuración de ModelMapper:

Uso en los controladores

OrderController con ModelMapper + Swagger

ProductController con ModelMapper + Swagger

Estas anotaciones permiten documentar la API para que Swagger genere una interfaz interactiva:

@Api: Describe el controlador.

@ApiOperation: Documenta cada endpoint.

@ApiResponses y @ApiResponse: Detallan posibles respuestas HTTP.

@Api(value = "Controlador de Categorías", tags = "Categorías")

@RestController

@RequestMapping("/api/categorias")

public class CategoriaController {

...

}

UserController con ModelMapper + Swagger

