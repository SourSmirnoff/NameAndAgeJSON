import com.google.gson.*;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static final String FILENAME = "output.json";
    private static JsonArray jsonArray;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        jsonArray = loadJsonFile();

        while (true) {
            System.out.println("\nChoose an action: \n1. Add Name and Age \n2. Read Names and Ages \n3. Delete Name and Age \n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
                case 1:
                    addPerson(scanner);
                    break;
                case 2:
                    readPeople();
                    break;
                case 3:
                    deletePerson(scanner);
                    break;
                case 4:
                    scanner.close();
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static JsonArray loadJsonFile() {
        File file = new File(FILENAME);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                return JsonParser.parseReader(reader).getAsJsonArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JsonArray();
    }

    private static void saveJsonFile() {
        try (FileWriter file = new FileWriter(FILENAME)) {
            file.write(gson.toJson(jsonArray));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addPerson(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("age", age);

        jsonArray.add(jsonObject);
        saveJsonFile();
        System.out.println("Person added successfully.");
    }

    private static void readPeople() {
        if (jsonArray.size() == 0) {
            System.out.println("No data available.");
            return;
        }
        for (JsonElement personElement : jsonArray) {
            JsonObject personObject = personElement.getAsJsonObject();
            System.out.println("Name: " + personObject.get("name").getAsString() + ", Age: " + personObject.get("age").getAsInt());
        }
    }

    private static void deletePerson(Scanner scanner) {
        System.out.print("Enter name of person to delete: ");
        String nameToDelete = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject personObject = jsonArray.get(i).getAsJsonObject();
            if (personObject.get("name").getAsString().equalsIgnoreCase(nameToDelete)) {
                jsonArray.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            saveJsonFile();
            System.out.println("Person deleted successfully.");
        } else {
            System.out.println("Person not found.");
        }
    }
}
