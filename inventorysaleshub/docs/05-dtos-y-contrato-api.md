## DTOs y contrato API

Se recomienda usar DTOs para **Request** y **Response** y evitar exponer entidades directamente.

### DTOs comunes (ejemplos)
- `ApiResponseDTO<T>`: `{ boolean success, String message, T data }`
- `UserDTO`, `UserRequestDTO`
- `ProductDTO`, `ProductRequestDTO`, `ProductUpdateDTO`
- `OrderRequestDTO`, `OrderItemRequestDTO`, `OrderResponseDTO`, `OrderItemDTO`
- `InvoiceDTO`, `InvoiceRequestDTO`
- `PayDTO`, `PayRequestDTO`
- `ProductHistoryDTO`

**Ventajas**: seguridad (no exponer password, etc.), control de lo que acepta la API, validaciones con `@Valid`.