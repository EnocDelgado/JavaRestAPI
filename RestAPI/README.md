# JAVA SPRING BOOT RESTAPI

# How To Start
## 1. Set up a new Spring Boot project.
**Using Spring Initializr:**

1. Navigate to Spring Initializr
2. Project: Maven Project
3. Language: Java
4. Spring Boot: Choose the latest stable version (e.g., 2.7.x or 3.x).
5. Project Metadata:Group: com.example Artifact: jwt-demo
6. Packaging: Jar
7. Java: 11 or higher
8. Dependencies:
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - H2 Database (for in-memory testing)
    - Lombok (optional, for reducing boilerplate code)

## 2. Add the required dependencies.

Open the pom.xml file and add the following dependencies for JWT support to the existing dependencies:

<dependencies>
    <!-- Existing dependencies -->

    <!-- JWT Dependencies -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson, jjwt-orgjson -->
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>

**Note**: Adjust the versions if newer versions are available.

## 3. Configure application properties.

In src/main/resources/application.properties, configure the H2 database and JWT properties:

    # H2 Database Configuration
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
    # Hibernate Configuration
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=update
    # JWT Configuration
    jwt.secret=your_secret_key_here
    jwt.expiration=3600000

Replace your_secret_key_here with a strong secret key.
According to the JWT specification, the key size for HS256
(HMAC with SHA-256) must be at least 256 bits (32 bytes)
long and should contain only letters and numbers.

A sample secret key you can use is:

    thisIsMysecregtfrdesww233eggtffeeddgkjjhhtdhttebd54ndhdhfhhhshs8877465sbbdd

## 4. Create models and repositories.

1. Create a User model in com.example.jwt_demo.model package.

2. Create a UserRepository interface in com.example.jwt_demo.repository package.

## 5. Implement JWT utility classes.

Create a JwtUtil class in com.example.jwt_demo.security package.

## 6. Configure Spring Security.
1. **UserDetailsService Implementation:**

   Create **CustomUserDetailsService** in **com.example.jwt_demo.service** package.
2. **Authentication Entry Point:**

   Create **AuthEntryPointJwt** in **com.example.jwt_demo.security** package.
3. **JWT Authentication Filter:**

   Create **AuthTokenFilter** in **com.example.jwt_demo.security** package.
4. **Security Configuration:**

   Create **WebSecurityConfig** in **com.example.jwt_demo.security** package.

**Note:** Starting from Spring Boot 2.7.x and Spring Security 5.7.x, WebSecurityConfigurerAdapter is deprecated in favor of the component-based security configuration shown above.

## 7. Create authentication controllers.

1. Auth Controller:

   Create **AuthController** in **com.example.jwt_demo.controller** package.


## 8. Secure REST endpoints.

**Test Controller**

Create **TestController** in **com.example.jwt_demo.controller** package.

- /api/test/all: **Public endpoint**, accessible without authentication.
- /api/test/user: **Secured endpoint**, accessible only with valid JWT.

## 9. Provide GitHub Repository.



## 10. Test the application and provide Postman Collection for testing.

1. Run the Application

   Run the application from your IDE by navigating to the JwtDemoApplication class or use the command line:

         mvn spring-boot:run

2. Test Using Postman or Curl

    - **Download Postman Collection:**

   https://drive.google.com/file/d/1lM5vGsrTSeYmEOCsd2AZlcU8_Rt72tzi/view?usp=sharing

    - **Register a New User:**

      POST http://localhost:8080/api/auth/signup
      Content-Type: application/json
      {
      "username": "testuser",
      "password": "testpass"
      }

    - **Authenticate the User**

      POST http://localhost:8080/api/auth/signin
      Content-Type: application/json
      {
      "username": "testuser",
      "password": "testpass"
      }

    - **Access Public Endpoint**

      GET http://localhost:8080/api/test/all
    - **Access Secured Endpoint without Token**

      GET http://localhost:8080/api/test/user
    - **Access Secured Endpoint with Token**

      GET http://localhost:8080/api/test/user
      Authorization: Bearer eyJhbGciOiJIUzI1NiJ9

## Explanation
- **User Registration (/api/auth/signup):**
  Allows new users to register. The password is encoded using BCryptPasswordEncoder before saving to the database.
- **User Authentication (/api/auth/signin):**
  Authenticates the user credentials. If successful, generates and returns a JWT token.
- **JWT Token:**
  The token includes the username as the subject and is signed with the secret key.

- **Secured Endpoint (/api/test/user):**
  Protected by JWT authentication. Requires a valid JWT token in the Authorization header.
- **JWT Validation:**
  The AuthTokenFilter intercepts incoming requests and validates the JWT token. If valid, sets the authentication in the security context.

## Conclusion
You have successfully created a Spring Boot application with JWT authentication. This example demonstrates how to:

- Set up JWT authentication and authorization in a Spring Boot application.
- Secure REST endpoints using JWT and Spring Security.
- Handle user registration and authentication.

## **Note:**
Always keep your secret keys secure. In production, use environment variables or a secrets manager.