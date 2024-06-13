# Stores List App

Esta aplicación muestra una lista de tiendas obtenidas desde una API remota. La aplicación está construida en Kotlin y utiliza una arquitectura basada en MVVM y Clean Architecture. Además, utiliza Realm DB para el respaldo de datos en local y muestra errores a través de Snackbars.


## Estructura del Proyecto

El proyecto sigue la arquitectura MVVM y Clean Architecture. A continuación se presenta una breve descripción de los principales paquetes y sus responsabilidades:

- data: Contiene las implementaciones de las fuentes de datos remotas y locales, así como los modelos de datos.
- di: Contiene los módulos para inyección de dependencias.
- domain: Contiene las entidades del dominio y los casos de uso.
- presentation: Contiene las clases relacionadas con la presentación (ViewModels, Fragments, Adapters, etc).

## Funcionalidades Principales

- Lista de Tiendas: Despliega una lista de tiendas mostrando datos como Nombre, Código y Dirección.
- Manejo de Errores HTTP: La aplicación maneja errores HTTP (4XX, 5XX) mostrando mensajes de error en un Snackbar para notificar al usuario sobre los problemas de red.
- StateFlow y LiveData: Se utilizan StateFlow y LiveData para el manejo reactivo de datos y su observación en la UI.
- Almacenamiento Local: Utilización de Realm para el almacenamiento local de datos.
- Snackbars para Errores: Muestra mensajes de error al usuario utilizando Snackbars.
- Ícono Personalizado: La aplicación utiliza un ícono personalizado.

## Dependencias Principales

- Retrofit: Para la comunicación con la API remota.
- OkHttp: Para las solicitudes HTTP.
- Realm: Para el almacenamiento local de datos.
- Dagger Hilt: Para la inyección de dependencias.
- Coroutines: Para el manejo de operaciones asíncronas.
- MockK: Para las pruebas unitarias.
- MockWebServer: Para simular respuestas de servidor en las pruebas unitarias.

## Configuración Inicial

1. Clona el repositorio:
    ```sh
    git clone https://github.com/tu-usuario/stores-list-app.git
    cd stores-list-app
    ```

2. Configura los secretos de la API en el archivo `local.properties`. Este archivo debe estar en la raíz del proyecto (junto a `build.gradle`).

    ```properties
    # local.properties
    BEARER_TOKEN=your_token_here
    COMPANY_UUID=your_company_uuid_here
    ```

    Asegúrate de reemplazar `your_token_here` y `your_company_uuid_here` con los valores proporcionados para la API.

## Ejecución de la Aplicación

1. Abre el proyecto en Android Studio.

2. Sincroniza el proyecto con Gradle haciendo clic en `Sync Project with Gradle Files`.

3. Conecta un dispositivo Android o configura un emulador.

4. Ejecuta la aplicación haciendo clic en el botón `Run` de Android Studio o usando el atajo `Shift + F10`.

## Ejecución de Pruebas Unitarias

Las pruebas unitarias se han escrito utilizando JUnit4 y MockK. Para ejecutar las pruebas unitarias:

1. Abre el proyecto en Android Studio.

2. Abre el panel `Project` y navega a `app/src/test/java/com/example/storeslist`.

3. Haz clic derecho en el paquete de pruebas y selecciona `Run 'All Tests'` o usa el atajo `Ctrl + Shift + F10` para ejecutar todas las pruebas.
