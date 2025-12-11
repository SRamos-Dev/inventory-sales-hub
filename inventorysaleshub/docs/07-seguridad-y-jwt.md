# Ya con nuestras dependencias incluidas podremos codificar nuestro JwtProvider, de la siguiente manera:
Ahora podemos implementar el AuthService, que se encargará de:

Login:

Recibe LoginRequestDTO (email + password).

Verifica credenciales en la base de datos.

Si son correctas → genera un token JWT con JwtProvider.

Devuelve un LoginResponseDTO con token + email + rol.

Register:

Recibe RegisterDTO.

Crea un nuevo User con rol asociado y contraseña encriptada.

Guarda el usuario en la base de datos.

Devuelve un UserDTO o un ApiResponseDTO con mensaje de éxito.

Para esto, lo primero que haremos es agregar en nuestro User(MODEL) un campo password con sus getters y setters correspondientes:

Y en nuestro UserRepository agregaremos el método findByEmail con su respectivo import de la siguiente manera:

¿Para qué sirve?

Optional<User> → lo usamos para evitar NullPointerException.

findByEmail → Spring automáticamente interpreta esto como: SELECT * FROM user WHERE email = ?;

AuthService

Con estos pequeños cambios podremos crear nuestro AuthService de la siguiente manera:

Crearemos una carpeta en nuestro paquete llamada service y dentro nuestra clase

AuthService

Comenzaron a codificar nuestro servicio:

Cambios importantes

Contraseñas encriptadasUsamos PasswordEncoder de Spring Security para no guardar contraseñas en texto plano.

Validación en login

Busca al usuario por email.

Compara la contraseña encriptada con la recibida.

Generación de tokenCon JwtProvider, agregando email y rol dentro del JWT.

Ahora que tenemos el AuthService, el siguiente paso es el AuthController, que expondrá los endpoints:

POST /auth/register

POST /auth/login

AuthController:

¿Qué hemos hecho?

/auth/register

Recibe un RegisterDTO.

Llama a authService.register().

Devuelve un UserDTO envuelto en ApiResponseDTO.

/auth/login

Recibe un LoginRequestDTO (email + password).

Llama a authService.login().

Devuelve un LoginResponseDTO con el JWT token, email y rol.

Validaciones

Usamos @Valid para que se apliquen las validaciones definidas en los DTOs (ejemplo: email válido, password mínimo 6 caracteres, etc.).

Ahora para conectar todo vamos a cerrar el circuito configurando el SecurityConfig, esta clase la crearemos en nuestra carpeta security:

Explicación de la configuración:

Password encoder

Usamos BCryptPasswordEncoder → así las contraseñas se guardan encriptadas.

AuthenticationManager

Necesario para que Spring maneje autenticación con UserDetailsService (lo configuraremos más adelante si necesitas).

SecurityFilterChain

csrf().disable() → no usamos CSRF en APIs REST.

permitAll() para /auth/** → el login y registro deben ser accesibles sin token.

hasAnyAuthority("ADMIN") o ("USER", "ADMIN") → restringe endpoints según rol.

anyRequest().authenticated() → todo lo demás requiere un token válido.

Al realizar esta configuración podemos continuar con el último componente clave: el filtro JWT. Este filtro se ejecuta en cada request y se encarga de:

Leer el token JWT del encabezado Authorization.

Validar el token con JwtProvider.

Extraer el email y el rol.

Cargar esa información en el contexto de seguridad de Spring para que funcione la autorización.

Nota: Señalaré los cambios dentro de nuestro código donde hemos agregado información dentro de nuestro SecurityConfig.

Flujo de Autenticación con JWT

El sistema de seguridad implementado en nuestra aplicación sigue el patrón Login → Token → Acceso a recursos protegidos usando Spring Security + JWT.

1. Registro de usuario

Endpoint: POST /auth/register

El cliente envía un RegisterDTO con name, email, password y roleId.

El backend encripta la contraseña con BCryptPasswordEncoder y guarda el usuario en la base de datos.

Respuesta: un UserDTO con los datos básicos del usuario (sin contraseña).

Ejemplo:

Solicitud:

{

"name": "Juan Pérez",

"email": "juan@example.com",

"password": "123456",

"roleId": 1

}

Respuesta:

{

"success": true,

"message": "User registered successfully",

"data": {

"id": 1,

"name": "Juan Pérez",

"email": "juan@example.com",

"roleName": "USER"

}

}

2. Login

Endpoint: POST /auth/login

El cliente envía un LoginRequestDTO con email y password.

El backend valida credenciales y, si son correctas, genera un JWT con JwtProvider.

El token incluye el email y el role del usuario.

Respuesta: LoginResponseDTO con el token, email y rol.

Ejemplo de respuesta:

{

"success": true,

"message": "Login successful",

"data": {

"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",

"email": "juan@example.com",

"role": "USER"

}

}

3. Acceso a recursos protegidos

Una vez autenticado, el cliente debe enviar el token en cada request mediante el header Authorization:

Authorization: Bearer <token>

# El filtro JwtAuthFilter se encarga de:
Leer el token.

Validarlo con JwtProvider.

Extraer email y rol.

Colocar la autenticación en el contexto de Spring Security.

Según la configuración en SecurityConfig, los endpoints se autorizan por rol:

/products/** → solo ADMIN.

/orders/** → USER o ADMIN.

/auth/** → libre.

4. Ejemplo de acceso protegido

Solicitud:

GET http://localhost:8080/products

Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Si el token es válido y el rol tiene permisos, la API devuelve la lista de productos.Si no, responde con 403 Forbidden.

Gestión de roles y protección de endpoints (Opcional pero recomendable)

¿Para qué sirve?

Manejo de excepciones centralizado → ahora mismo si un login falla devuelve un IllegalArgumentException con un stacktrace feo. Lo ideal es crear un GlobalExceptionHandler para devolver respuestas limpias en el mismo formato ApiResponseDTO.

Por ejemplo:

{

"success": false,

"message": "Invalid email or password",

"data": null

}

UserDetailsService personalizado → Spring Security normalmente carga usuarios desde UserDetailsService. Nosotros estamos usando directamente el repo + JWT, pero si quieres un nivel extra de integración con Spring, se podría implementar.

GlobalExceptionHandler:

Explicación:

MethodArgumentNotValidException

Se dispara cuando un DTO falla en validación (@Valid).

Devuelve un mapa con los campos y sus mensajes de error.

Por ejemplo:

{

"success": false,

"message": "Validation error",

"data": {

"email": "Must be a valid email address",

"password": "Password must be at least 6 characters long"

}

}

IllegalArgumentException

Se dispara en casos como login inválido o rol inexistente.

Devuelve un mensaje claro.

Ejemplo de respuesta:

{

"success": false,

"message": "Invalid email or password",

"data": null

}

Exception (catch-all)

Cubre cualquier otro error no controlado.

Devuelve un mensaje genérico con 500 Internal Server Error.

Hasta ahora:

Ya tenemos JWT funcionando.

El filtro (JwtAuthFilter) valida tokens en cada request.

La seguridad está configurada en SecurityConfig.

Tenemos roles definidos en tu entidad Role.

Ahora toca:

Asegurarnos de que los roles están bien cargados desde la base de datos.

Usarlos en SecurityConfig para proteger endpoints.

Crear un seeder inicial de roles (ADMIN, USER) para no tener que meterlos a mano en la base.

Sembrar roles iniciales:  Crearemos un paquete nuevo llamado config, donde incluiremos la clase RoleDataLoader, que cree roles básicos en la BD al arrancar la app. Esto asegura que siempre tengamos al menos ADMIN y USER disponibles.

SecurityConfig: Ya lo teníamos, pero repasaremos con más detalle y pulimos las reglas de acceso.

↓

En este caso como podremos observar solo hemos especificado los accesos adicionales incluyendo Invoice y Payments.

Validación en controladores (Opcional)

Además de SecurityConfig, Spring nos permite usar anotaciones directamente en los métodos de los controladores, pero para esto necesitamos habilitar @EnableMethodSecurity en nuestro SecurityConfig.

Por ejemplo:

Si queremos aún más control digamos (dentro del mismo controlador algunos métodos accesibles solo por ADMIN y otros por USER), se pueden usar anotaciones como:

@PreAuthorize("hasAuthority('ADMIN')")

@GetMapping("/admin-only")

public ResponseEntity<String> adminOnlyEndpoint() {

return ResponseEntity.ok("Only admins can see this!");

}

Solo tendremos que agregar en nuestro SecurityConfing:

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration

@EnableMethodSecurity

public class SecurityConfig {

...

}

Luego, en cualquier método de controlador, podremos usar:

@PreAuthorize("hasAuthority('ADMIN')") → solo ADMIN.

@PreAuthorize("hasAnyAuthority('ADMIN','USER')") → ADMIN y USER.

@PreAuthorize("isAuthenticated()") → cualquier usuario logueado.

Nota:

No necesitamos poner esas anotaciones en todas partes.

Solo las usamos cuando queramos reglas más específicas dentro de un mismo controlador.

Si basta con las reglas globales en SecurityConfig, podemos dejarlas y listo.

Configuración adicional de seguridad

En este proyecto específico podemos combinar la configuración global en SecurityConfig + @PreAuthorize en métodos clave, para tener una seguridad:

Clara y centralizada en lo general.

Granular y flexible en casos específicos.

¿Qué es @PreAuthorize?

El @PreAuthorize es una anotación de Spring Security que te permite proteger métodos (en controladores o servicios) con reglas basadas en roles o expresiones de seguridad.

Ventajas de @PreAuthorize

Permite protección a nivel de método, no solo en configuración global.

Hace el código más declarativo y fácil de leer (ves directamente qué rol puede acceder).

Flexible: puedes usar expresiones SpEL (Spring Expression Language).

Configuracion Global SecurityConfig

↓

Seguridad granular con @PreAuthorize

Ahora, dentro de nuestros controladores, todo dependerá de a quienes queramos darle permisos sobre cada uno de nuestros endpoints, por ejemplo:

Solo agregamos @PreAuthorize y asignamos el rol que deseamos que tenga acceso con: (“hasRole(‘ADMIN’)”), ("hasRole('USER')") o si es para ambos ("hasAnyRole('USER','ADMIN')").

Más adelante en nuestro proyecto refactorizaremos los controladores para pasar de un sistema simple a uno más avanzado y limpio mostrando nuestro código con el uso del @PreAutorize más a fondo.

Estadísticas y Panel Administrativo (Dashboard)

Aquí el objetivo es darles a los administradores una vista general del negocio, con métricas clave y reportes, como, por ejemplo:

Total de ventas en un rango de fechas.

Número de pedidos realizados.

Productos más vendidos.

Clientes con más compras.

Niveles de stock críticos.

Por lo que en primer lugar vamos a definir 4 grandes bloques que normalmente se muestran en un dashboard administrativo y de los cuales es necesario generar DTOs:

Creación de DTOs necesarios

SalesSummaryDTO

TopProductDTO

TopCustomerDTO

LowStockProductDTO

El siguiente paso sería crear el DashboardController con endpoints que devuelvan datos mock (simulados). Esto nos servirá para probar la estructura del Dashboard antes de conectarlo con las consultas reales a la base de datos.

Ten presente que los datos mockeados que pondremos en el DashboardController son solo valores “quemados” en el código (hardcoded).

Eso significa:

No afectan la base de datos en absoluto.

No cambian la lógica de negocio ya implementada.

Sirven únicamente como ejemplo de estructura de respuesta, para que podamos probar en Postman y ver cómo sería el JSON real.

Sabiendo esto podremos avanzar con la configuración necesaria:

DashboardController

Explicación

Endpoint /dashboard/sales-summary

Devuelve un resumen con: número de pedidos, ingresos totales y ticket promedio.

Endpoint /dashboard/top-products

Lista los productos más vendidos con su cantidad.

Endpoint /dashboard/top-customers

Lista los clientes con más compras y lo que gastaron.

Endpoint /dashboard/low-stock

Lista productos con stock bajo.

En la siguiente fase, lo que haremos será reemplazar esos datos mock con consultas reales a la base de datos (OrderRepository, ProductRepository, etc.), de modo que el dashboard siempre muestre datos actualizados. Para eso necesitamos crear métodos personalizados en los repositorios (OrderRepository, ProductRepository, etc.) usando Spring Data JPA.

Configuración esencial

