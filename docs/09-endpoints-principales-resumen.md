## Endpoints principales (resumen)

> Los endpoints pueden variar según el paquete/implementación; aquí un resumen típico:

| Recurso | Métodos | Rutas |
|---|---:|---|
| Auth | POST | `/auth/register`, `/auth/login` |
| Users | GET, POST, PUT, DELETE | `/users`, `/users/{id}` |
| Products | GET, POST, PUT, DELETE | `/products`, `/products/{id}` |
| Categories | CRUD | `/categories` |
| Orders | POST, GET | `/orders`, `/orders/{id}`, `/orders/user/{userId}` |
| Invoices | GET, POST | `/invoices`, `/invoices/{id}` |
| Payments | GET, POST | `/payments`, `/payments/{id}` |
| Dashboard | GET | `/dashboard/metrics` |

Para detalles de request/response y validaciones, usa los DTOs del proyecto o la documentación OpenAPI/Swagger si está habilitada.