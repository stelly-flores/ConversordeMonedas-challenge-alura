import com.google.gson.*;
import java.io.File;
import java.io.FileReader; // Para leer el archivo
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CurrencyConverter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ExchangeRateAPIService apiService = new ExchangeRateAPIService();
    private final Scanner scanner;


    public CurrencyConverter(Scanner scanner) {

        this.scanner = scanner;
    }

    // MÉTODO CORREGIDO para guardar como un array JSON válido
    private void saveConversionToFile(String fromCurrency, String toCurrency,
                                      double amount, double result, double rate) {
        JsonObject newConversionData = new JsonObject();
        newConversionData.addProperty("fecha_hora_conversion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        newConversionData.addProperty("moneda_origen", fromCurrency);
        newConversionData.addProperty("moneda_destino", toCurrency);
        newConversionData.addProperty("cantidad_original", String.format("%.2f", amount));
        newConversionData.addProperty("cantidad_convertida", String.format("%.2f", result));
        newConversionData.addProperty("tasa_de_cambio_aplicada", String.format("%.6f", rate));

        JsonArray conversionsArray = new JsonArray();
        File conversionsFile = new File("conversiones.json");

        if (conversionsFile.exists() && conversionsFile.length() > 0) {
            try (FileReader reader = new FileReader(conversionsFile)) {

                JsonElement existingElement = JsonParser.parseReader(reader);

                if (existingElement.isJsonArray()) {
                    conversionsArray = existingElement.getAsJsonArray();
                } else {

                    System.err.println("Advertencia: conversiones.json no contenía un array JSON válido. Se creará uno nuevo.");

                }
            } catch (JsonSyntaxException e) {

                System.err.println("Error al parsear conversiones.json. El archivo no es un array JSON válido o está corrupto. Se creará uno nuevo. Error: " + e.getMessage());

            } catch (IOException e) {
                System.err.println("Error de I/O al leer conversiones.json. Se creará uno nuevo. Error: " + e.getMessage());

            }
        }

        conversionsArray.add(newConversionData);


        try (FileWriter writer = new FileWriter(conversionsFile, false)) {
            gson.toJson(conversionsArray, writer);
            System.out.println("Conversión guardada correctamente en conversiones.json");
        } catch (IOException e) {
            System.err.println("Error al escribir la conversión en conversiones.json: " + e.getMessage());
        }
    }




    public void startConversion() {

        System.out.println("Puedes usar códigos de moneda como: USD, EUR, JPY, GBP, CAD, AUD");
        System.out.println("Monedas de Latinoamérica sugeridas: ARS, BOB, BRL, CLP, COP, MXN, PEN, UYU, VES");


        String fromCurrency = getInput("Ingrese la moneda de origen (ej: USD):");
        String toCurrency = getInput("Ingrese la moneda de destino (ej: MXN):");
        double amount = getAmount("Ingrese la cantidad a convertir:");

        try {
            JsonObject response = apiService.getConversionData(fromCurrency, toCurrency, amount);


            if (response != null && response.has("result") && "success".equals(response.get("result").getAsString())) {

                if (response.has("conversion_result") && response.has("conversion_rate")) {
                    double conversionResult = response.get("conversion_result").getAsDouble();
                    double conversionRate = response.get("conversion_rate").getAsDouble();

                    displayConversionResult(fromCurrency, toCurrency, amount, conversionResult, conversionRate);
                    // Asegúrate de llamar al método corregido
                    saveConversionToFile(fromCurrency, toCurrency, amount, conversionResult, conversionRate);
                } else {
                    System.out.println("Error: La respuesta de la API no contiene 'conversion_result' o 'conversion_rate'.");
                    System.out.println("Respuesta recibida: " + response.toString());
                }
            } else if (response != null && response.has("error-type")) {
                System.out.println("Error en la conversión reportado por la API: " + response.get("error-type").getAsString());


            } else {
                System.out.println("Error en la conversión: Respuesta inesperada o nula de la API.");
                if (response != null) {
                    System.out.println("Respuesta recibida: " + response.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Error de red o de entrada/salida al contactar la API: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("La conexión con la API fue interrumpida: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (JsonSyntaxException e) {
            System.out.println("Error al procesar la respuesta JSON de la API: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al realizar la conversión (desde API): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado durante la conversión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getInput(String message) {
        System.out.println(message);
        String input = "";
        while (input.isEmpty()) {
            input = scanner.nextLine().toUpperCase().trim();
            if (input.isEmpty()) {
                System.out.println("La entrada no puede estar vacía. Intente de nuevo.");
            }
            else if (!input.matches("[A-Z]{3}")) {
                System.out.println("Formato de moneda inválido. Debe ser un código de 3 letras (ej: USD). Intente de nuevo.");
                input = "";
            }
        }
        return input;
    }

    private double getAmount(String message) {
        double amount = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println(message);
            try {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()){
                    System.out.println("La cantidad no puede estar vacía. Intente de nuevo.");
                    continue;
                }
                amount = Double.parseDouble(line);
                if (amount > 0) {
                    validInput = true;
                } else {
                    System.out.println("La cantidad debe ser un número positivo. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número (ej: 100.50). Intente de nuevo.");
            }
        }
        return amount;
    }

    private void displayConversionResult(String fromCurrency, String toCurrency,
                                         double amount, double result, double rate) {
        System.out.println("\n--- Resultado de la Conversión ---");
        System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, result, toCurrency);
        System.out.printf("Tasa de cambio: 1 %s = %.6f %s%n", fromCurrency, rate, toCurrency);
        System.out.println("----------------------------------");
    }


    public Scanner getScanner() {
        return scanner;
    }
}
