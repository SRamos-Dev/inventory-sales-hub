# Los controladores permiten:
Centralizan el manejo de las solicitudes HTTP y rutas de la aplicación.

Recibir peticiones de los usuarios (como solicitudes GET, POST, PUT y DELETE).

Permiten estructurar el código de forma clara, separando la lógica de negocio de la capa de presentación.

Facilitan la gestión de datos al comunicarse con los modelos y repositorios.

Retornar respuestas al usuario (como JSON o una vista HTML).

Anteriormente, hemos creado un ejemplo de controlador de la clase “Product”, ahora bien, continuaremos el proceso de creación para las demás clases que lo requieran y mejoraremos el ejemplo básico realizado anteriormente para que tenga el uso que deseamos.

Lo primero que haremos es agregar el siguiente fragmento de código con su import correspondiente en la clase Product (import org.springframework.http.ResponseEntity;):

Este lo agregaremos después de nuestra “public List<Product> getProducts()”.

¿Qué hace cada parte del código?

@GetMapping("/{id}"): Indica que este método manejará solicitudes GET con una URL que incluya un ID de producto como, por ejemplo: GET /productos/5 (donde 5 sería el ID de un producto específico).

ResponseEntity<Product>: Define que este método devolverá una respuesta HTTP, que puede incluir un producto si se encuentra, o un estado de error si no existe.

@PathVariable Long id: Indica que el id en la URL (/productos/{id}) se asigna a la variable id dentro del método. Por ejemplo, si el usuario accede a /productos/7, el valor 7 se pasa como argumento.

productRepository.findById(id): Busca en la base de datos un producto con el ID proporcionado. Este método devuelve un objeto de tipo Optional<Product> (puede contener un producto o estar vacío si no se encuentra).

.map(product -> ResponseEntity.ok(product)): Si el producto existe, se transforma (map()) en una respuesta HTTP 200 OK, con el producto en el cuerpo de la respuesta.

.orElse(ResponseEntity.notFound().build());: Si el producto no existe, se devuelve una respuesta HTTP 404 Not Found, indicando que no se encontró el recurso solicitado.

Ejemplo de flujo de respuesta

1️. El usuario hace una solicitud: GET /productos/3

2. El sistema busca el producto con ID 3.

Si el producto existe -> Se devuelve 200 OK con los datos en JSON.

Si el producto no existe -> Se devuelve 404 Not Found.

Este código es muy útil para manejar búsquedas de datos en una API REST sin que el sistema falle cuando un producto no se encuentra.

Ahora bien, podemos manejar una estructura similar para nuestros controladores faltantes de acuerdo con lo que deseamos para cada uno.

¿Cuándo es necesario crear un controlador por clase?

Si una entidad se expone como un recurso independiente:Por ejemplo, en este proyecto, Product, User y Order son entidades clave que requieren operaciones CRUD (Crear, Leer, Actualizar, Eliminar). En estos casos, es lógico tener controladores separados como ProductController, UserController y OrderController.

Cuando la lógica de negocio lo requiereSi una entidad tiene reglas específicas, como validaciones avanzadas, cálculos o filtros especiales, conviene tener un controlador dedicado para manejar esa lógica.

Si la entidad tiene relaciones complejasPor ejemplo, un OrderController que gestione pedidos podría incluir endpoints para obtener los detalles de cada pedido (OrderDetails). Si hay muchas relaciones, separar los controladores facilita la gestión.

¿Cuándo NO es necesario un controlador por cada clase?

Cuando la entidad solo es parte de otra entidad más grandePor ejemplo, ProductHistory almacena cambios en precios o movimientos de stock. En lugar de tener un controlador exclusivo, podrías incluir métodos dentro de ProductController para acceder a ese historial.

Si la entidad no se expone directamente a los usuariosSi Role solo define permisos internos y no necesita endpoints públicos, no es necesario un RoleController. En su lugar, se puede manejar desde el UserController.

Enfoque recomendado

Lo mejor es crear controladores para las entidades principales, y agrupar en ellos las funciones relacionadas con otras entidades cuando sea conveniente. Esto evita sobrecargar la API con demasiados controladores innecesarios.

Implementación de funcionalidades (ProductController)

Como ya hemos aclarado el uso de los controladores y que algunos no deberían tenerlos, podemos realizar las siguientes acciones en nuestro código, en este caso dentro de “ProductController” para incorporar las características necesarias para su uso conjunto con “ProductHistory”, ya que podremos omitir la creación de un “ProductHistoryController” debido a que podemos implementarlos juntos.

Lo primero que deberemos hacer es inyectar el repositorio correctamente, con el siguiente código:

Recuerda que @Autowired lo que hace es inyectar automáticamente ProductHistoryRepository, lo que permite interactuar con la base de datos sin necesidad de instanciarlo manualmente, que es lo que necesitaremos principalmente.

Luego realizaremos el siguiente código:

¿Qué hace cada parte del código?

@PutMapping("/{id}") -> Esta anotación indica que el método responderá a solicitudes HTTP PUT a la ruta /productos/{id}.

public ResponseEntity<Product> updateProduct -> Es el método que se ejecutará cuando llegue una solicitud PUT.

@PathVariable Long id: Extrae el ID desde la URL.

@RequestBody Product updatedProduct: Toma los datos del producto enviado en el cuerpo de la solicitud en formato JSON y los convierte en un objeto Product.

return productRepository.findById(id): Busca el producto en la base de datos con el ID recibido.

Si lo encuentra, lo pasa al bloque del .map(...).

Si no lo encuentra, salta al .orElse(...).

ProductHistory history = new ProductHistory();: Crea una nueva instancia de ProductHistory para registrar el cambio.

history.setProduct(product);: Asocia el historial al producto original que se va a modificar.

history.setAction("Update");: Define el tipo de acción registrada (en este caso, una actualización).

history.setTimestamp(LocalDateTime.now());: Guarda la fecha y hora exacta en que se está haciendo la modificación.

productHistoryRepository.save(history);: Guarda el registro de historial en la base de datos antes de cambiar el producto.

product.setName(updatedProduct.getName()); product.setPrice(updatedProduct.getPrice()); y product.setStock(updatedProduct.getStock());: Actualiza los campos del producto existente con los nuevos valores recibidos en la solicitud.

return ResponseEntity.ok(productRepository.save(product));: Guarda el producto actualizado en la base de datos. Devuelve una respuesta HTTP 200 (OK) con el objeto actualizado.

}).orElse(ResponseEntity.notFound().build());: Si productRepository.findById(id) no encuentra ningún producto con ese ID, se devuelve una respuesta HTTP 404 (Not Found).

Cuando escribamos este código saldrán una serie de errores ya que no hemos definido los “Getters” y “Setters” previamente en nuestras clases. Lo cual haremos en este momento:

Product (Getters y Setters)

ProductHistory (Getters y Setters)

Crear un nuevo producto (POST)

POST es el método HTTP que se usa para crear un nuevo recurso (Este endpoint sirve para crear un nuevo producto en la base de datos) y lo implementaremos en nuestro código dentro de nuestra clase ProductController de la siguiente manera:

¿Qué hace cada parte del código?

ResponseEntity<Product>: la respuesta incluirá el producto creado y un código de estado HTTP apropiado.

@PostMapping: indica que este método responderá a solicitudes POST a la ruta /productos.

@RequestBody Product newProduct: Spring toma el JSON enviado por el cliente y lo convierte en un objeto Product.

@Valid: activa las validaciones de tu entidad Product (por ejemplo, que name no esté en blanco o que price sea mayor a cero).

Product savedProduct = productRepository.save(newProduct);: Guarda el nuevo producto en la base de datos.

Codigo similar al anterior: Guarda un registro en la tabla ProductHistory indicando que se creó un nuevo producto.

return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);: Devuelve el producto creado junto con el código 201 Created, que es el estándar cuando se crea algo nuevo en REST.

Eliminar un producto (DELETE)

El método DELETE sirve para eliminar un recurso existente de la base de datos, en este caso, un producto (El método DELETE se usa para eliminar un Product por su id.). Lo realizaremos de la siguiente manera:

¿Qué hace cada parte del código?

@DeleteMapping("/{id}"): Este endpoint escucha peticiones HTTP DELETE en la URL /productos/{id}. El {id} es una variable de ruta (path variable).

ResponseEntity<Void>: Devuelve una respuesta HTTP sin cuerpo (Void), pero con un código de estado adecuado (204 si se eliminó, 404 si no se encontró).

@PathVariable Long id: El id capturado de la URL se pasa como argumento.

Codigo similar al anterior: Se crea un objeto ProductHistory para registrar la acción de borrado, el momento exacto y guarda el historial en la base de datos.

productRepository.delete(product);: Se elimina el producto de la base de datos.

return ResponseEntity.noContent().<Void>build();: Devuelve una respuesta HTTP 204 No Content indicando que la operación fue exitosa, pero sin cuerpo.

Listar productos por ID de categoría (GET)

Para listar productos por categoría (útil para la vista o filtrado en el frontend) debemos agregar este código a nuestro ProductController:

Y este método en nuestro ProductRepository para que funcione correctamente:

Nota: En este punto ya no explicaré tan detalladamente cada parte del código ya que en el mayor de los casos será reutilizado de los anteriores, solo daré explicaciones de partes nuevas que sean usadas.

Buscar productos por nombre (o parte del nombre, insensible a mayúsculas) (GET)

@RequestParam: Se usa para obtener valores enviados en la URL como parámetros de consulta (también llamados query parameters).

Y este método en nuestro ProductRepository para que funcione correctamente:

Obtener el historial de acciones de un producto (GET)

Y este método en nuestro ProductHistoryRepository para que funcione correctamente:

Listar productos con bajo stock  (GET)

Y este método en nuestro ProductRepository para que funcione correctamente:

Paginación de productos para vistas largas (GET)

Ordenar productos por campos específicos (nombre, precio, stock...) (GET)

Estos dos últimos códigos no nos pedirán un método adicional, pero si tener los imports bien definidos, por lo que debemos mantener los siguientes:

Como buena práctica en proyectos de Java con Spring, podemos realizarlo con la estructura de la imagen, ya que así cumpliríamos con los siguientes puntos:

Imports explícitos en lugar de *, lo que mejora la claridad y evita posibles conflictos de nombres.

Organización lógica: primero los paquetes propios del proyecto, luego los externos de Jakarta y Spring, y finalmente los de Java estándar.

Mejor legibilidad: es más fácil identificar qué clases estás usando en tu controlador.

Con esto ya hemos finalizado nuestro controlador de producto (ProductController) y su integración con el historial de productos (ProductHistory), de esta manera tendremos un código que permite buscar productos por categoría, buscar productos por nombre (parcial y sin importar mayúsculas), consultar historial de un producto, obtener productos con stock bajo, paginación de productos y ordenar productos por campo (nombre, precio, stock).

# Ahora deberemos generar los controladores restantes:
1. UserController

Registro y gestión de usuarios

Consulta de perfil, historial de pedidos

Acceso por rol (cliente o administrador)

2. OrderController

Crear nuevos pedidos

Obtener pedidos por usuario

Cancelar o ver detalles de un pedido

3. OrderDetailsController (opcional, puede ir dentro de OrderController)

Consultar detalles individuales de un pedido

Editar líneas de pedido (si aplica)

4. InvoiceController

Generar facturas asociadas a un pedido

Consultar facturas emitidas

5. PayController

Registrar pagos simulados

Consultar estado de pago de un pedido

6. CategoryController

Crear, editar, listar y eliminar categorías de productos

Usado principalmente por el administrador

7. StoreController

Crear y consultar sucursales o almacenes

Asociar productos a una tienda

8. SupplierController

Registrar y gestionar proveedores

Asociarlos con productos

9. RoleController (opcional, si gestionas roles desde el backend)

Crear o editar roles (admin, cliente etc.)

Aunque no siempre se expone como API publica

UserController

Recuerda que al interactuar con la interfaz de un repositorio como en este caso la línea “return ResponseEntity.ok(orderRepository.findByUserId(id));” con “orderRepository” debemos definir en dicha interfaz la siguiente línea de código:

De esta manera estará buscando órdenes en la base de datos que pertenezcan al usuario con el ID proporcionado.

OrderController

En la línea “return ResponseEntity.noContent().<Void>build();” ponemos un <Void> ya que es como decirle al compilador "Voy a construir una ResponseEntity vacía, y el tipo genérico que estoy usando explícitamente es Void". Así podremos resolver errores de tipos sin hacer cast y es 100% seguro y limpio.

InvoiceController

Normalmente en este tipo de código cuando tenemos el “return invoiceRepository.findByOrderId(orderId)” lo que haríamos es agregar en la interfaz “InvoiceRepository” el código “List<Invoice>…” pero este caso es diferente ya que agregaremos en la interfaz:

¿Por qué Optional y no List?

Es una clase de Java que representa un valor que puede estar presente o no. Se introdujo en Java 8 para evitar errores como NullPointerException y hacer que el código que maneja valores nulos sea más seguro y expresivo.

¿Para que la usaremos en nuestro InvoiceController?

Como solo esperamos una factura por producto, este nos permitirá buscar a factura (Invoice) que tenga ese orderId, y si no la encuentra, devuelve un objeto vacío (Optional.empty()) en vez de null, para así evitar tener que hacer una verificación nula y posibles errores adicionales.

PayController

Igual como en el anterior en interfaz “PayRepository” agregaremos:

Como una buena opción podemos ir construyendo los Getters y Setters a medida que vayamos realizando cada controlador para que no olvidemos ninguno, algo bueno que tiene VS Code es que te permite crear dando clic derecho en la opción:

Y posteriormente en:

Y seleccionaremos los atributos a los que queramos generarle sus Getters y Setters.

Nota: Se debe generar getters y setters para TODAS las propiedades privadas, sin embargo, para las que representan relaciones entre entidades es recomendable EVITAR setters al igual que en las listas, SOLO getters.

CategoryController

StoreController

SupplierController

RoleController

Implementación de usuario y administrador (Modo prueba)

¿Qué es el modo prueba?

Para optimizar la fase de desarrollo y pruebas, esta sección introduce una nueva opción que habilita un modo prueba (entorno test). Este modo precarga automáticamente datos simulados, permitiendo evaluar distintas funcionalidades sin afectar la información real del sistema.

¿Para qué sirve?

Al iniciar la aplicación con el perfil de prueba, se generarán automáticamente los siguientes roles:

Usuario (ModoPrueba)

Administrador (ModoPrueba)

Según la opción elegida, el entorno permitirá ejecutar flujos completos:

Modo Usuario: Simulación de compra, consulta y administración de perfil sobre datos y productos ficticios, garantizando un entorno seguro.

Modo Administrador: Simulación de gestión de inventario, administración de permisos, modificación de costos, productos y promociones, replicando la operativa real en un contexto de prueba.

Para ello, la aplicación contará con una base de datos separada (inventory_test_db), donde los datos de prueba se generarán y gestionarás a través de la clase TestDataLoader. Esta funcionalidad proporciona un entorno controlado para validar procesos sin riesgo de alterar información real.

Desarrollo

Base de datos de prueba

1. Abre DBeaver o tu herramienta de gestión SQL.

