# 3- Ahora tendremos todo listo para crear nuestra clase TestDataLoader:
Uso de DTOs (Opcional pero recomendado)

¿Qué es un DTO?

Es una clase o estructura que se usa para transportar datos entre diferentes capas o sistemas de una aplicación, generalmente entre la capa de presentación (como un controlador) y la capa de negocio o persistencia (servicios y base de datos).

¿Para qué sirven?

Separar la lógica interna del modelo de datos de cómo se presentan o reciben los datos.

Evitar exponer directamente las entidades o modelos de la base de datos a la capa externa (como una API).

Facilitar el envío o recepción de solo los datos necesarios (no siempre quieres enviar todo un objeto con muchos campos).

Mejorar la seguridad, evitando exponer campos sensibles.

Optimizar la comunicación, reduciendo la cantidad de datos transferidos.

Ejemplo:

Tenemos una entidad Usuario con muchos campos internos (id, nombre, email, password, fecha de registro, etc.…).

Si queremos enviar información de un usuario a través de una API, no queremos enviar la contraseña ni datos internos. Por lo tanto, para evitar esto crearemos un DTO.

Procedimiento

1. Lo primero que debemos hacer es crear un nuevo paquete dentro de nuestro proyecto (inventorysaleshub) con las clases correspondientes:

2. Y crearemos en cada una de nuestras clases sus atributos, constructor correspondiente, getters y setters, hay que tener presente que más adelante según adaptemos nuestro proyecto podremos agregar más clases DTO:

UserDTO

ProductDTO

OrderDTO

PayDTO

InvoiceDTO

3. Ahora para que se usen los DTOs debemos adaptar los controladores.

UserController

↓

Y el adaptar el siguiente método para obtener un solo usuario por ID

↓

Debemos crear la clase UserRequestDTO, de esta manera

Posteriormente inyectaremos el RoleRepository y cambiaremos nuestro método POST de la siguiente manera:

Inyección de método:

↓

Explicación de los cambios realizados:

.map(UserDTO::new): transforma cada User a UserDTO.

ResponseEntity.ok(...): devuelve una respuesta HTTP 200.

El @RequestBody ahora recibe un UserRequestDTO, que contiene solo datos necesarios para crear el usuario.

Se busca el Role usando el roleId proporcionado.

Se crea un User nuevo y se convierte la respuesta en un UserDTO.

ProductController

↓

↓

En el caso del ProductController además de adaptarlos tenemos que crear un DTO adicional llamado ProductUpdateDTO, esto debido a que actualmente:

Estamos recibiendo un Product completo desde el cliente → esto abre la puerta a que alguien mande datos indebidos (por ejemplo, asignarse otra categoría, proveedor, etc.).

Estamos devolviendo la entidad Product directamente → expones relaciones internas que no deberían mostrarse (como ProductHistory o asociaciones).

Por lo tanto, para evitar esto usaremos esta nueva clase creada y la haremos de la siguiente manera:

De esta manera podremos proseguir con la adaptación del PutMapping(“/{id}”)

↓

Hemos realizado unos cambios importantes que explicaré con un poco más de detalle:

Antes:

Entrada: Recibía directamente un objeto Product updatedProduct desde el cliente.

Riesgoso porque el cliente podía modificar campos sensibles (ID, categoría, relaciones).

Proceso:

Guardaba historial (ProductHistory) de la acción UPDATED.

Actualizaba los campos del product con los datos de updatedProduct.

Guardaba el producto en BD.

Salida: Devolvía la entidad Product completa al cliente.

Exponía atributos internos y relaciones.

Ahora:

Entrada: Recibe un ProductUpdateDTO.

Contiene solo los campos que se pueden modificar (name, price, stock).

Validación con @Valid.

Proceso:

Guarda historial (ProductHistory) igual que antes.

Actualiza los campos del product usando request (DTO), no un objeto Product crudo.

Guarda el producto en BD.

Salida: Devuelve un ProductDTO.

Muestras únicamente los datos necesarios.

Evitas exponer detalles internos de la entidad.

Ahora que ya adaptamos el PUT con un ProductUpdateDTO, también necesitaremos un ProductRequestDTO para el POST (cuando creas un producto nuevo). Esto se debe a que al crear un producto (POST) necesita más información que al actualizarlo (PUT). Por ejemplo, en la creación el cliente debe indicar a qué categoría pertenece, mientras que en la actualización normalmente no se cambia eso.

