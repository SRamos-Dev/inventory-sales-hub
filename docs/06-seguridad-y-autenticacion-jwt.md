## Seguridad y autenticación (JWT)

- **Dependencias**: `spring-boot-starter-security`, `jjwt` (api/impl/jackson).
- **JwtProvider**: genera y valida tokens; contiene secret y tiempos de expiración.
- **AuthService**: register, login; usa `PasswordEncoder` (BCrypt).
- **JwtAuthFilter**: extrae token `Authorization: Bearer <token>`, valida y coloca `Authentication` en contexto.
- **SecurityConfig**:
  - `csrf().disable()`
  - `permitAll()` para `/auth/**`
  - reglas: `/products/**` (ADMIN), `/orders/**` (USER, ADMIN), etc.
  - habilitar `@EnableMethodSecurity` para usar `@PreAuthorize` en métodos.

**Flujo**:
1. `POST /auth/register` -> crea usuario (password encriptada) y asigna rol.  
2. `POST /auth/login` -> valida credenciales y devuelve `LoginResponseDTO` con token.  
3. Peticiones protegidas incluyen header `Authorization: Bearer <token>`.