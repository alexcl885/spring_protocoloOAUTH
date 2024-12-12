# Ejemplo sencillo de OAuth2 con Spring Boot

Este proyecto es un ejemplo de cómo usar OAuth2 con Spring Boot para permitir que los usuarios inicien sesión con servicios como GitHub. Aquí te explicamos paso a paso cómo configurarlo.

## 1. Agregar dependencias en `pom.xml`

Para que tu proyecto funcione con OAuth2, necesitas agregar esta dependencia en el archivo `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

Esto le dice a Spring Boot que use las herramientas necesarias para trabajar con OAuth2.

## 2. Configurar `application.properties`

El archivo `application.properties` debe tener la configuración básica para conectar tu aplicación con GitHub. Además, vamos a habilitar el registro en la consola para depurar problemas si los hay.

### Datos de GitHub

Reemplaza `<clave pública>` y `<clave secreta>` con los valores que GitHub te proporciona cuando registras tu aplicación:

```properties
spring.security.oauth2.client.registration.github.client-id=<clave pública>
spring.security.oauth2.client.registration.github.client-secret=<clave secreta>
```

### Activar los mensajes de depuración

Para que puedas ver información detallada sobre el funcionamiento de seguridad en la consola, agrega esto:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 3. Configurar el inicio de sesión OAuth2

En el código de configuración de seguridad, agrega lo siguiente para configurar el inicio de sesión con OAuth2 y proteger ciertas rutas:

```java
.oauth2Login((oauthlogin) -> oauthlogin
    .loginPage("/login")             // Página de inicio de sesión personalizada
    .defaultSuccessUrl("/")         // Redirige al inicio tras un login exitoso
    .permitAll())                     // Permite a cualquiera acceder a la página de login
.requestMatchers("/horario", "/instalacion")
    .authenticated();                 // Protege estas rutas, se necesita iniciar sesión
```

### Explicación simple del código:

1. **`loginPage("/login")`**: Define una página personalizada donde los usuarios inician sesión.
2. **`defaultSuccessUrl("/")`**: Después de iniciar sesión, el usuario es llevado a la página principal (`/`).
3. **`permitAll()`**: Cualquier usuario puede acceder a la página de inicio de sesión.
4. **`requestMatchers("/horario", "/instalacion").authenticated()`**: Protege las rutas `/horario` y `/instalacion`, requiriendo que el usuario esté autenticado para acceder a ellas.

## 4. Cambiar la vista

Para agregar un botón que permita iniciar sesión con GitHub, agrega este código en tu página HTML:

```html
<a th:href="@{/oauth2/authorization/github}">
    <i class="fa-brands fa-github fa-3x text-primary"></i>
</a>
```

### ¿Por qué usamos `th:href`?

- `th:href` es una función de Thymeleaf que genera dinámicamente las URL. Esto asegura que la ruta se configure correctamente según el servidor.
- Si usáramos `href` sin `th:`, la URL sería fija y podría no funcionar correctamente en todas las configuraciones.

Con esto tendrás un proyecto funcional que usa OAuth2 con Spring Boot de forma sencilla.
