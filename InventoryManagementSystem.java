import java.util.Scanner;

/**
 * The InventoryManagementSystem class is the entry point for the application.
 * It provides a command-line interface (CLI) to interact with the InventoryManager.
 * It features robust input validation, ANSI-styled console messages, and
 * prevents common Scanner buffer bugs by using nextLine() parsing.
 *
 * @author Mummana Devi Prasad
 */
public class InventoryManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager manager = new InventoryManager();

    // ANSI Escape codes for color-coded console output
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        System.out.println(CYAN + BOLD + "==================================================");
        System.out.println("   Welcome to the Inventory Management System");
        System.out.println("==================================================" + RESET);

        // Run low-stock check at startup to alert the manager immediately
        manager.checkLowStock();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = promptString("Select an option (1-7): ");
            switch (choice) {
                case "1":
                    addNewProductFlow();
                    break;
                case "2":
                    viewProductsFlow();
                    break;
                case "3":
                    searchProductFlow();
                    break;
                case "4":
                    updateProductFlow();
                    break;
                case "5":
                    deleteProductFlow();
                    break;
                case "6":
                    generateReportFlow();
                    break;
                case "7":
                    System.out.println(GREEN + BOLD + "\nThank you for using the Inventory Management System. Exiting... Goodbye!" + RESET);
                    running = false;
                    break;
                default:
                    System.out.println(RED + "\n[ERROR] Invalid option! Please select a number between 1 and 7." + RESET);
            }
        }
        scanner.close();
    }

    /**
     * Prints the primary application menu.
     */
    private static void printMenu() {
        System.out.println("\n" + BLUE + BOLD + "--- MAIN MENU ---" + RESET);
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Search Product by ID");
        System.out.println("4. Update Product Details");
        System.out.println("5. Delete Product");
        System.out.println("6. Generate Inventory Report");
        System.out.println("7. Exit");
    }

    // ==========================================
    // Interactive User Flows
    // ==========================================

    /**
     * Interactive flow for adding a new product.
     */
    private static void addNewProductFlow() {
        System.out.println(CYAN + BOLD + "\n--- Add New Product ---" + RESET);

        String id = promptString("Enter Product ID (e.g. PRD01): ");
        if (id.isEmpty()) {
            System.out.println(RED + "[ERROR] Product ID cannot be empty!" + RESET);
            return;
        }

        // Check duplicate before taking other inputs
        if (manager.searchProductById(id) != null) {
            System.out.println(RED + "[ERROR] A product with ID '" + id + "' already exists." + RESET);
            return;
        }

        String name = promptString("Enter Product Name: ");
        if (name.isEmpty()) {
            System.out.println(RED + "[ERROR] Product Name cannot be empty!" + RESET);
            return;
        }

        String category = promptString("Enter Category: ");
        if (category.isEmpty()) {
            System.out.println(RED + "[ERROR] Category cannot be empty!" + RESET);
            return;
        }

        double price = promptDouble("Enter Price: ");
        if (price < 0) {
            System.out.println(RED + "[ERROR] Price cannot be negative. Operation cancelled." + RESET);
            return;
        }

        int quantity = promptInt("Enter Quantity: ");
        if (quantity < 0) {
            System.out.println(RED + "[ERROR] Quantity cannot be negative. Operation cancelled." + RESET);
            return;
        }

        Product newProduct = new Product(id, name, category, price, quantity);
        boolean success = manager.addProduct(newProduct);
        if (success) {
            // Check if this newly added product triggers low stock alert
            if (quantity < 5) {
                System.out.println(YELLOW + "[WARNING] The added product quantity is less than 5 units. It is marked as low stock." + RESET);
            }
        }
    }

    /**
     * Flow for viewing all items, and displaying low stock alerts immediately after.
     */
    private static void viewProductsFlow() {
        System.out.println(CYAN + BOLD + "\n--- Current Inventory ---" + RESET);
        manager.viewAllProducts();
        
        // Also show a quick check for low stock
        manager.checkLowStock();
    }

    /**
     * Flow to search for a product and display its details.
     */
    private static void searchProductFlow() {
        System.out.println(CYAN + BOLD + "\n--- Search Product ---" + RESET);
        String id = promptString("Enter Product ID to Search: ");
        if (id.isEmpty()) {
            System.out.println(RED + "[ERROR] Product ID cannot be empty!" + RESET);
            return;
        }

        Product product = manager.searchProductById(id);
        if (product != null) {
            System.out.println(GREEN + "\n[FOUND] Product Details:" + RESET);
            System.out.println("------------------------------------");
            System.out.println("ID          : " + product.getProductId());
            System.out.println("Name        : " + product.getProductName());
            System.out.println("Category    : " + product.getCategory());
            System.out.printf("Price       : $%.2f\n", product.getPrice());
            System.out.println("Quantity    : " + product.getQuantity());
            System.out.println("------------------------------------");
            if (product.getQuantity() < 5) {
                System.out.println(YELLOW + "Status      : Low Stock (" + product.getQuantity() + " left) [ALERT]" + RESET);
            } else {
                System.out.println(GREEN + "Status      : Healthy Stock" + RESET);
            }
        } else {
            System.out.println(RED + "\n[ERROR] Product with ID '" + id + "' not found." + RESET);
        }
    }

    /**
     * Interactive flow for updating product fields, supporting "press enter to keep current value".
     */
    private static void updateProductFlow() {
        System.out.println(CYAN + BOLD + "\n--- Update Product Details ---" + RESET);
        String id = promptString("Enter Product ID to Update: ");
        if (id.isEmpty()) {
            System.out.println(RED + "[ERROR] Product ID cannot be empty!" + RESET);
            return;
        }

        Product product = manager.searchProductById(id);
        if (product == null) {
            System.out.println(RED + "[ERROR] Product with ID '" + id + "' not found." + RESET);
            return;
        }

        System.out.println("\nUpdating Product [" + product.getProductName() + "]. Press Enter to keep current values.");

        // Name Update
        System.out.print("New Product Name (Current: " + product.getProductName() + "): ");
        String nameInput = scanner.nextLine().trim();
        String name = nameInput.isEmpty() ? product.getProductName() : nameInput;

        // Category Update
        System.out.print("New Category (Current: " + product.getCategory() + "): ");
        String catInput = scanner.nextLine().trim();
        String category = catInput.isEmpty() ? product.getCategory() : catInput;

        // Price Update
        double price = product.getPrice();
        System.out.print("New Price (Current: $" + product.getPrice() + "): ");
        String priceInput = scanner.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                price = Double.parseDouble(priceInput);
                if (price < 0) {
                    System.out.println(RED + "[ERROR] Price cannot be negative. Update aborted." + RESET);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "[ERROR] Invalid numeric price. Update aborted." + RESET);
                return;
            }
        }

        // Quantity Update
        int quantity = product.getQuantity();
        System.out.print("New Quantity (Current: " + product.getQuantity() + "): ");
        String qtyInput = scanner.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                quantity = Integer.parseInt(qtyInput);
                if (quantity < 0) {
                    System.out.println(RED + "[ERROR] Quantity cannot be negative. Update aborted." + RESET);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "[ERROR] Invalid integer quantity. Update aborted." + RESET);
                return;
            }
        }

        // Perform Update
        manager.updateProduct(id, name, category, price, quantity);
    }

    /**
     * Flow for deleting a product with confirmation prompt.
     */
    private static void deleteProductFlow() {
        System.out.println(CYAN + BOLD + "\n--- Delete Product ---" + RESET);
        String id = promptString("Enter Product ID to Delete: ");
        if (id.isEmpty()) {
            System.out.println(RED + "[ERROR] Product ID cannot be empty!" + RESET);
            return;
        }

        Product product = manager.searchProductById(id);
        if (product == null) {
            System.out.println(RED + "[ERROR] Product with ID '" + id + "' not found." + RESET);
            return;
        }

        System.out.print(YELLOW + "Are you sure you want to delete '" + product.getProductName() + "'? (Y/N): " + RESET);
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            manager.deleteProduct(id);
        } else {
            System.out.println("[INFO] Delete operation cancelled.");
        }
    }

    /**
     * Flow for generating reports.
     */
    private static void generateReportFlow() {
        manager.generateReport();
    }

    // ==========================================
    // Robust Console Input Helper Methods
    // ==========================================

    /**
     * Prompts the user with a message and reads a string.
     */
    private static String promptString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts user and returns a validated double. If invalid, returns -1.0.
     */
    private static double promptDouble(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println(RED + "[ERROR] Input is not a valid decimal number!" + RESET);
            return -1.0;
        }
    }

    /**
     * Prompts user and returns a validated integer. If invalid, returns -1.
     */
    private static int promptInt(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(RED + "[ERROR] Input is not a valid integer!" + RESET);
            return -1;
        }
    }
}
