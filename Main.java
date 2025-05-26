import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CurrencyConverter converter = new CurrencyConverter(scanner);


        displayWelcomeMessage();

        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();

            int opcionPrincipal = obtenerOpcion(scanner, 1, 2);

            switch (opcionPrincipal) {
                case 1:
                    realizarUnaConversion(converter, scanner);
                    break;
                case 2:
                    continuar = false;
                    displayGoodbyeMessage();
                    break;
            }
        }
        scanner.close();
    }

    private static void displayWelcomeMessage() {
        System.out.println("\n*************************************************");
        System.out.println("*                                               *");
        System.out.println("*      BIENVENIDO AL CONVERSOR DE MONEDAS       *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=========== MENÚ PRINCIPAL ===========");
        System.out.println("1. Realizar una conversión");
        System.out.println("2. Salir del programa");

    }

    private static int obtenerOpcion(Scanner scanner, int min, int max) {
        while (true) {
            System.out.print("Seleccione una opción (" + min + "-" + max + "): -> ");
            String linea = scanner.nextLine().trim();
            try {
                int opcion = Integer.parseInt(linea);
                if (opcion >= min && opcion <= max) {
                    return opcion;
                } else {
                    System.out.println("Opción inválida. Por favor ingrese un número entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
            }
        }
    }


    private static void realizarUnaConversion(CurrencyConverter converter, Scanner scanner) {
        System.out.println("\n-------- INICIANDO CONVERSIÓN --------");
        converter.startConversion();
        System.out.println("--------------------------------------");
        System.out.println("\nPresione Enter para volver al menú principal...");
        scanner.nextLine();
    }

    private static void displayGoodbyeMessage() {
        System.out.println("\n*************************************************");
        System.out.println("*                                               *");
        System.out.println("*   Gracias por usar nuestro servicio.          *");
        System.out.println("*            ¡Vuelva pronto!                    *");
        System.out.println("*                                               *");
        System.out.println("*************************************************");
    }
}

