Detailed Breakdown:
@Configuration:

Marks the class as a Spring configuration class. Spring will treat this class as one that provides bean definitions to the application context.

@Autowired:

CustomUserDetailsService userDetailsService: Injects the custom user details service to load user details for authentication.

AuthEntryPointJwt unauthorizedHandler: Injects a custom entry point handler that handles unauthorized access attempts.

@Bean Methods:

authenticationJwtTokenFilter: This method creates an instance of AuthTokenFilter, which is responsible for extracting and validating JWT tokens from the request header.

authenticationManager: This method provides an instance of AuthenticationManager, which is used by Spring Security to authenticate users.

passwordEncoder: This method returns a BCryptPasswordEncoder, which is used to encode and validate passwords securely using the BCrypt hashing algorithm.

securityFilterChain: This method defines the HTTP security configuration. It includes settings for disabling CSRF and CORS, setting up exception handling, managing sessions (stateless), and configuring access rules for various API endpoints.

csrf().disable():

Disables CSRF protection, which is necessary when using stateless authentication with JWT because there is no session or cookies involved in JWT-based authentication.

cors().disable():

Disables CORS, which is typically done in API applications. If your application will handle cross-origin requests, you might want to enable and configure CORS.

Exception Handling:

exceptionHandling.authenticationEntryPoint(unauthorizedHandler): Configures the custom authentication entry point (AuthEntryPointJwt) to handle situations where users attempt to access protected resources without proper authentication.

Session Management:

sessionCreationPolicy(SessionCreationPolicy.STATELESS): Configures Spring Security to not create HTTP sessions. This is typical in stateless authentication scenarios, such as when using JWT tokens, where the server does not store any session data.

Access Rules:

requestMatchers("/api/auth/**", "/api/test/all").permitAll(): Allows unrestricted access to authentication-related endpoints (/api/auth/**) and a public test endpoint (/api/test/all).

anyRequest().authenticated(): Requires authentication for all other endpoints in the application.

JWT Token Filter:

http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class): Adds the custom JWT token filter before the UsernamePasswordAuthenticationFilter. This ensures that the JWT token is processed before any other authentication mechanisms.

http.build():

Returns the final SecurityFilterChain configuration, which Spring Security will use to secure HTTP requests.

Summary:
This configuration class is essential for setting up Spring Security with JWT-based authentication. It:

Disables CSRF and CORS (typically needed in stateless authentication systems).

Configures session management as stateless (because JWT tokens are used).

Defines access rules (e.g., allowing public access to certain endpoints like /api/auth/**).

Sets up the custom JWT filter and authentication entry point to handle token validation and unauthorized access properly.

The WebSecurityConfig class integrates JWT authentication into a Spring Boot application and ensures that only authenticated users can access protected endpoints.