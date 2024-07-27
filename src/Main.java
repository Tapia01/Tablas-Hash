import hash.HashTable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String CSV_FILE_PATH = "bussines.csv";
    private static final int TABLE_SIZE = 1000;

    public static void main(String[] args) {
        HashTable hashTable1 = new HashTable(TABLE_SIZE);
        HashTable hashTable2 = new HashTable(TABLE_SIZE);


        processFile(hashTable1, hashTable2);

        interactWithUser(hashTable1, hashTable2);
    }

    private static void processFile(HashTable hashTable1, HashTable hashTable2) {
        long totalInsertionTime1 = 0;
        long totalInsertionTime2 = 0;
        int id = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            String splitBy = ",";

            while ((line = br.readLine()) != null) {
                String[] business = line.split(splitBy);
                if (business.length >= 5) {
                    String key = business[1];
                    String value = String.format("ID=%s, Address=%s, City=%s, State=%s",
                            business[0], business[2], business[3], business[4]);

                    long time1 = hashTable1.measurePutTime(key, value, 1);
                    long time2 = hashTable2.measurePutTime(key, value, 2);

                    totalInsertionTime1 += time1;
                    totalInsertionTime2 += time2;

                    printBusinessDetails(id, business, time1, time2);
                    id++;
                } else {
                    System.err.println("Línea inválida detectada y omitida: " + line);
                }
            }

            printTotalInsertionTime(totalInsertionTime1, totalInsertionTime2);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static void printBusinessDetails(int id, String[] business, long time1, long time2) {
        System.out.printf("[%d] Datos del negocio [ID=%s, Nombre=%s, Dirección=%s, Ciudad=%s, Estado=%s]%n",
                id, business[0], business[1], business[2], business[3], business[4]);
        System.out.printf("Tiempo de inserción usando HashFunction1: %d ns%n", time1);
        System.out.printf("Tiempo de inserción usando HashFunction2: %d ns%n", time2);
    }

    private static void printTotalInsertionTime(long totalInsertionTime1, long totalInsertionTime2) {
        System.out.printf("%nTiempo total de inserción usando HashFunction1: %d ns%n", totalInsertionTime1);
        System.out.printf("Tiempo total de inserción usando HashFunction2: %d ns%n", totalInsertionTime2);
        if (totalInsertionTime1 < totalInsertionTime2) {
            System.out.println("HashFunction1 ha demostrado ser más eficiente en las inserciones.");
        } else {
            System.out.println("HashFunction2 ha demostrado ser más eficiente en las inserciones.");
        }
    }

    private static void interactWithUser(HashTable hashTable1, HashTable hashTable2) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea después del entero

            switch (option) {
                case 1:
                    searchByName(hashTable1, hashTable2, scanner);
                    break;

                case 2:
                    showDataByIndex(hashTable1, hashTable2, scanner);
                    break;

                case 3:
                    exit = true;
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, selecciona una opción válida del menú.");
                    break;
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Buscar negocio por nombre");
        System.out.println("2. Mostrar datos almacenados en un índice específico");
        System.out.println("3. Salir del programa");
        System.out.print("Selecciona una opción: ");
    }

    private static void searchByName(HashTable hashTable1, HashTable hashTable2, Scanner scanner) {
        System.out.print("Introduce el nombre del negocio que deseas buscar: ");
        String searchKey = scanner.nextLine().trim();

        try {
            long startTime1 = System.nanoTime();
            String foundValue1 = hashTable1.searchValueByKey(searchKey, 1);
            long endTime1 = System.nanoTime();
            long searchTime1 = endTime1 - startTime1;

            long startTime2 = System.nanoTime();
            String foundValue2 = hashTable2.searchValueByKey(searchKey, 2);
            long endTime2 = System.nanoTime();
            long searchTime2 = endTime2 - startTime2;

            System.out.printf("Tiempo de búsqueda con HashFunction1: %d ns%n", searchTime1);
            System.out.printf("Tiempo de búsqueda con HashFunction2: %d ns%n", searchTime2);

            printSearchResults(searchKey, foundValue1, foundValue2, searchTime1, searchTime2);
        } catch (Exception e) {
            System.err.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private static void printSearchResults(String searchKey, String foundValue1, String foundValue2, long searchTime1, long searchTime2) {
        if (foundValue1 != null || foundValue2 != null) {
            if (foundValue1 != null) {
                System.out.println("Clave '" + searchKey + "' encontrada en la Tabla Hash 1. Datos: " + foundValue1);
            }
            if (foundValue2 != null) {
                System.out.println("Clave '" + searchKey + "' encontrada en la Tabla Hash 2. Datos: " + foundValue2);
            }
        } else {
            System.out.println("Clave '" + searchKey + "' no encontrada en ninguna tabla hash.");
        }

        if (searchTime1 < searchTime2) {
            System.out.println("HashFunction1 ha sido más eficiente en la búsqueda.");
        } else {
            System.out.println("HashFunction2 ha sido más eficiente en la búsqueda.");
        }
    }

    private static void showDataByIndex(HashTable hashTable1, HashTable hashTable2, Scanner scanner) {
        System.out.print("Introduce el índice para mostrar los datos almacenados: ");
        int searchIndex = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea después del entero

        try {
            if (searchIndex < 0 || searchIndex >= hashTable1.getCapacity()) {
                System.out.println("Índice fuera de rango. El rango válido es de 0 a " + (hashTable1.getCapacity() - 1));
            } else {
                printDataAtIndex(hashTable1, hashTable2, searchIndex);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los datos del índice: " + e.getMessage());
        }
    }

    private static void printDataAtIndex(HashTable hashTable1, HashTable hashTable2, int index) {
        System.out.println("Datos en el índice " + index + " de la Tabla Hash 1:");
        List<String> data1 = hashTable1.getDataAtIndex(index);
        for (String data : data1) {
            System.out.println(data);
        }

        System.out.println("Datos en el índice " + index + " de la Tabla Hash 2:");
        List<String> data2 = hashTable2.getDataAtIndex(index);
        for (String data : data2) {
            System.out.println(data);
        }
    }
}
