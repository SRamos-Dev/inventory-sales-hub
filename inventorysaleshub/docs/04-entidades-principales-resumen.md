## Entidades principales (resumen)

> Cada entidad debe llevar `@Entity`, `@Id` y `@GeneratedValue`. Relaciones con `@ManyToOne`, `@OneToMany`, `@OneToOne`, `@ManyToMany` según corresponda. (Detalles de campos y relaciones en el manual).

| Entidad | Descripción / relaciones claves |
|---:|---|
| `User` | name, email, password, role (ManyToOne Role), orders (OneToMany Order) |
| `Role` | name (ADMIN / USER), users (OneToMany User) |
| `Product` | name, description, price, stock, category, supplier, store, histories (OneToMany ProductHistory) |
| `Category` | name, products (OneToMany Product) |
| `Order` | dateCreated, status, total, user (ManyToOne User), orderDetails (OneToMany OrderDetails), invoice (OneToOne Invoice), pay (OneToOne Pay) |
| `OrderDetails` | order (ManyToOne), product (ManyToOne), quantity, unitPrice |
| `Invoice` | issueDate, totalAmount, order (OneToOne) |
| `Pay` | method, status, order (OneToOne) |
| `Supplier` | companyName, contact, products (OneToMany Product) |
| `Store` | name, location, products (OneToMany Product) |
| `ProductHistory` | product (ManyToOne), action, timestamp |