# Business Site Server

A robust RESTful API server built with Spring Boot for managing business operations including customers, orders, items, and administrative functions.

## Overview

This is the backend server for the Default Business Site, providing comprehensive REST APIs for customer management, order processing, address management, inventory control, and administrative operations. The application features secure authentication, database migrations, and a fully tested service layer.

## Features

- ✅ **Customer Management** - Create, read, update, and manage customer profiles
- ✅ **Order Management** - Process and track customer orders
- ✅ **Inventory Management** - Manage items and stock
- ✅ **Address Management** - Handle multiple addresses per customer
- ✅ **Admin Users** - Administrative user management with role-based security
- ✅ **Security** - Spring Security with session management
- ✅ **Database Migrations** - Automated schema management with Flyway
- ✅ **Email Support** - Mail service integration
- ✅ **Comprehensive Testing** - Full unit and integration test coverage
- ✅ **Docker Support** - Docker Compose configuration included

## Technology Stack

- **Framework**: Spring Boot 4.0.2
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **Testing**: JUnit 5
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 17 or higher
- Gradle 8.0 or higher (or use the included `gradlew` wrapper)
- MySQL 8.0 or Docker

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/Business-Site-Server.git
cd Business-Site-Server
```

### 2. Configure Database
Update `src/main/resources/application.properties` with your database configuration:
```properties
spring.application.name=business
spring.datasource.url=jdbc:mysql://localhost:3306/business_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
```

### 3. Using Docker Compose (Recommended)
The project includes a `compose.yaml` for easy development setup. Simply run:
```bash
docker-compose up -d
```

This will start MySQL and prepare the database automatically.

## Running the Application

### Using Gradle
```bash
./gradlew bootRun
```

### On Windows
```bash
gradlew.bat bootRun
```

The server will start on `http://localhost:8080` by default.

## Database Migrations

Database schema is automatically managed using Flyway. Migration scripts are located in `src/main/resources/db/migration/`:

- `V1__Create_customers_table.sql`
- `V2__Create_admin_user_table.sql`
- `V3__Create_addresses_table.sql`
- `V4__Create_item_table.sql`
- `V5__Create_order_table.sql`
- `V6__Create_customer_addresses_collection_table.sql`

Migrations run automatically on application startup.

## Project Structure

```
src/main/java/com/example/demo/
├── BusinessApplication.java        # Spring Boot entry point
├── controller/                      # REST Controllers
├── service/                         # Business logic & services
├── repository/                      # Database repositories (JPA)
├── model/                           # Entity models
├── dto/                             # Data Transfer Objects
└── configs/                         # Spring configurations
```

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests AddressServiceTest
```

### View Test Reports
After running tests, view the HTML report at:
```
build/reports/tests/test/index.html
```

**Test Coverage:**
- `AddressServiceTest` - Address management tests
- `AdminUserServiceTest` - Admin user operations
- `CustomerServiceTest` - Customer CRUD operations
- `ItemServiceTest` - Inventory management
- `OrderServiceTest` - Order processing logic

## API Endpoints

The application provides REST endpoints for:

- **Customers** - `/api/customers`
- **Orders** - `/api/orders`
- **Items** - `/api/items`
- **Addresses** - `/api/addresses`
- **Admin Users** - `/api/admin/users`

(See detailed API documentation or Swagger/OpenAPI docs when available)

## Development

### Build the Project
```bash
./gradlew build
```

### Clean Build
```bash
./gradlew clean build
```

### Check for Issues
```bash
./gradlew build --info
```

## Environment Configuration

Key application properties:

| Property | Default | Description |
|----------|---------|-------------|
| `spring.application.name` | `business` | Application name |
| `server.port` | `8080` | Server port |
| `spring.jpa.show-sql` | `false` | SQL logging |

## Security

- Spring Security is configured for this application
- User authentication and authorization are implemented
- Session management via JDBC
- Admin role for administrative functions

## Support

For issues, questions, or suggestions, please open an issue on GitHub.


**Last Updated**: March 2026
