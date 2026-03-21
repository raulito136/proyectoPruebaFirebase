# Movie Collection Manager

Este es un proyecto de una aplicación de escritorio desarrollada en JavaFX para gestionar una colección personal de películas.

## Características

- **Autenticación de usuarios:** Sistema de login y registro.
- **Roles de usuario:**
    - **Administrador:** Puede añadir, editar y eliminar películas de la base de datos principal.
    - **Usuario:** Puede ver la lista de películas y gestionar su propia colección de copias (qué películas tiene, en qué formato, estado y cantidad).
- **Gestión de Películas (Admin):**
    - Ver un listado de todas las películas.
    - Añadir nuevas películas al catálogo.
    - Editar la información de películas existentes.
    - Eliminar películas del catálogo.
- **Gestión de Colección (Usuario):**
    - Ver la colección personal de copias de películas.
    - Añadir copias de películas existentes a su colección, especificando:
        - Formato (DVD, Blu-ray, Digital)
        - Estado (Nuevo, Usado, Coleccionista)
        - Cantidad
    - Editar el formato y estado de una copia.
    - Eliminar copias de la colección.

## Tecnologías Utilizadas

- **Lenguaje:** Java 11+
- **Framework UI:** JavaFX
- **Persistencia:** JPA (Hibernate)
- **Base de Datos:** MySQL
- **Gestión de dependencias:** Maven

## Primeros Pasos

A continuación se detallan los pasos para configurar y ejecutar el proyecto.

### Prerrequisitos

- JDK 11 o superior.
- Apache Maven.
- Un servidor de MySQL en funcionamiento.

### Configuración de la Base de Datos

1.  Crea una base de datos en tu servidor MySQL con el nombre `movie_collection`.
2.  El proyecto está configurado para conectarse a la base de datos usando el usuario `root` y la contraseña `root`. Si tus credenciales son diferentes, actualiza el fichero `src/main/resources/META-INF/persistence.xml` con tu usuario y contraseña:

    ```xml
    <property name="javax.persistence.jdbc.user" value="tu_usuario_de_bd" />
    <property name="javax.persistence.jdbc.password" value="tu_contraseña_de_bd" />
    ```

3.  La primera vez que ejecutes la aplicación, las tablas se crearán automáticamente gracias a la propiedad `hibernate.hbm2ddl.auto` con valor `update`.

### Ejecutar la Aplicación

1.  Abre un terminal en el directorio raíz del proyecto.
2.  Ejecuta el siguiente comando de Maven para compilar y lanzar la aplicación:

    ```sh
    mvn clean javafx:run
    ```

### Credenciales de Prueba

- **Administrador:**
    - **Usuario:** admin
    - **Contraseña:** admin
- **Usuario normal:** Puedes registrar un nuevo usuario desde la pantalla de login.
