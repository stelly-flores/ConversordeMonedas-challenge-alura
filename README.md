# Conversor de Monedas Challenge

## Descripción
Este es un proyecto de consola en Java que permite a los usuarios convertir montos entre diferentes monedas. La aplicación utiliza una interfaz de menú interactiva y guarda un historial de todas las conversiones realizadas en un archivo JSON (`conversiones.json`).

## Características
- Menú interactivo en la línea de comandos para una fácil navegación.
- Validación de entrada para códigos de moneda (deben ser de 3 letras, por ejemplo, USD, EUR, MXN).
- Validación de entrada para montos (deben ser números positivos).
- Guarda un historial detallado de cada conversión:
    - Fecha y hora de la conversión.
    - Moneda de origen.
    - Moneda de destino.
    - Monto original.
    - Monto convertido.
    - Tasa de cambio aplicada.
- El historial se almacena en el archivo `conversiones.json` en formato JSON.
- Sugiere códigos de moneda comunes, incluyendo varias de Latinoamérica (ARS, BOB, BRL, CLP, COP, MXN, PEN, UYU, VES).

## Cómo Ejecutar

### Prerrequisitos
- Java Development Kit (JDK) instalado (versión 8 o superior recomendada).
- Biblioteca Google Gson: Necesaria para el procesamiento de JSON. Puedes descargar el archivo `.jar` desde [Maven Central](https://search.maven.org/artifact/com.google.code.gson/gson/2.10.1/jar).

### Pasos para Ejecutar
1.  **Descargar Gson:**
    *   Descarga el archivo `gson-2.10.1.jar` (o la versión más reciente) del enlace proporcionado.
    *   Coloca el archivo `gson-2.10.1.jar` en el directorio raíz de este proyecto.

2.  **Compilar:**
    *   Abre una terminal o línea de comandos en el directorio raíz del proyecto.
    *   Ejecuta el siguiente comando para compilar los archivos Java (asegúrate de que `gson-2.10.1.jar` esté en el mismo directorio):
        ```bash
        javac -cp gson-2.10.1.jar *.java
        ```
        (En algunos sistemas, si los archivos están en un paquete como `com.alura.challenge`, necesitarás ajustar la compilación a `javac -cp gson-2.10.1.jar com/alura/challenge/*.java Main.java` o compilar desde el directorio que contiene `com`.)

3.  **Ejecutar:**
    *   Después de una compilación exitosa, ejecuta la aplicación con el siguiente comando:
        *   En Linux/macOS:
            ```bash
            java -cp ".:gson-2.10.1.jar" Main
            ```
        *   En Windows:
            ```bash
            java -cp ".;gson-2.10.1.jar" Main
            ```

### **Nota Importante sobre el Servicio API (`ExchangeRateAPIService.java`)**
La clase `ExchangeRateAPIService.java` en este proyecto actualmente contiene una implementación **placeholder** (de marcador de posición) para el método `getConversionData`. Esto significa que **NO realizará conversiones de moneda reales** con tasas de cambio en tiempo real.

Para que la aplicación funcione con tasas de cambio reales, necesitarás:
1.  Elegir un proveedor de API de tasas de cambio (ejemplos: [ExchangeRate-API](https://www.exchangerate-api.com/), Open Exchange Rates, Fixer.io, etc.). Muchos ofrecen planes gratuitos con limitaciones.
2.  Registrarte para obtener una clave de API (API Key).
3.  Modificar el método `getConversionData` en `ExchangeRateAPIService.java` para:
    *   Realizar una solicitud HTTP GET a la API elegida, incluyendo tu clave de API y las monedas de origen/destino.
    *   Analizar la respuesta JSON de la API para extraer la tasa de cambio y el resultado de la conversión.
    *   Devolver un `JsonObject` que contenga `conversion_result` y `conversion_rate`.

## Dependencias
-   **Google Gson (v2.10.1 o similar):** Utilizada para la serialización y deserialización de objetos Java a JSON (y viceversa), especialmente para manejar el archivo `conversiones.json` y procesar las respuestas de la API.
    *   [Descargar Gson desde Maven Central](https://search.maven.org/artifact/com.google.code.gson/gson/2.10.1/jar)

## Estructura del Proyecto
-   `Main.java`: Punto de entrada de la aplicación. Maneja el menú principal y la interacción con el usuario.
-   `CurrencyConverter.java`: Contiene la lógica principal para solicitar datos al usuario, realizar la conversión (llamando al servicio API) y guardar los resultados.
-   `ExchangeRateAPIService.java`: Clase destinada a comunicarse con una API externa para obtener las tasas de cambio. **(Actualmente es un placeholder)**.
-   `conversiones.json`: Archivo donde se guarda el historial de todas las conversiones realizadas por el usuario.
-   `README.md`: Este archivo.
-   `conversorchallenge.iml`: Archivo de configuración del proyecto para IntelliJ IDEA.
