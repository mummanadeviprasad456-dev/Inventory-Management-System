import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

/**
 * The InventoryManager class implements the business logic for the
 * Inventory Management System. It manages the product collection in memory
 * using an ArrayList and provides persistent storage using file IO.
 *
 * @author Mummana Devi Prasad
 */
public class InventoryManager {
    private final ArrayList<Product> products;
    private static final String FILE_NAME = "products.txt";

    /**
     * Constructor initializes the product list and automatically loads data from file.
     */
    public InventoryManager() {
        this.products = new ArrayList<>();
        loadProductsFromFile();
    }

    /**
     * Adds a new product to the inventory.
     *
     * @param product The product to be added
     * @return true if added successfully, false if duplicate ID exists
     */
    public boolean addProduct(Product product) {
        if (searchProductById(product.getProductId()) != null) {
            System.out.println("\n[ERROR] Product with ID " + product.getProductId() + " already exists!");
            return false;
        }
        products.add(product);
        System.out.println("\n[SUCCESS] Product '" + product.getProductName() + "' added successfully!");
        saveProductsToFile();
        return true;
    }

    /**
     * Displays all products in the inventory in a formatted table.
     */
    public void viewAllProducts() {
        if (products.isEmpty()) {
            System.out.println("\n[INFO] No products in the inventory.");
            return;
        }

        printTableHeader();
        for (Product product : products) {
            product.displayProduct();
        }
        printTableFooter();
    }

    /**
     * Searches for a product by its unique Product ID.
     *
     * @param productId The ID to search for
     * @return The Product object if found, or null otherwise
     */
    public Product searchProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equalsIgnoreCase(productId.trim())) {
                return product;
            }
        }
        return null;
    }

    /**
     * Updates an existing product's details.
     *
     * @param productId   The ID of the product to update
     * @param name        New product name
     * @param category    New product category
     * @param price       New price
     * @param quantity    New quantity
     * @return true if updated successfully, false if product not found or input is invalid
     */
    public boolean updateProduct(String productId, String name, String category, double price, int quantity) {
        Product product = searchProductById(productId);
        if (product == null) {
            System.out.println("\n[ERROR] Product with ID " + productId + " not found!");
            return false;
        }

        try {
            product.setProductName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setQuantity(quantity);
            System.out.println("\n[SUCCESS] Product with ID " + productId + " updated successfully!");
            saveProductsToFile();
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERROR] Failed to update product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a product from the inventory by its ID.
     *
     * @param productId The ID of the product to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(String productId) {
        Product product = searchProductById(productId);
        if (product == null) {
            System.out.println("\n[ERROR] Product with ID " + productId + " not found!");
            return false;
        }

        products.remove(product);
        System.out.println("\n[SUCCESS] Product '" + product.getProductName() + "' (ID: " + productId + ") deleted successfully!");
        saveProductsToFile();
        return true;
    }

    /**
     * Checks for and alerts the user about products with low stock levels (quantity < 5).
     */
    public void checkLowStock() {
        boolean hasLowStock = false;
        for (Product product : products) {
            if (product.getQuantity() < 5) {
                if (!hasLowStock) {
                    System.out.println("\n==================================================================");
                    System.out.println("[ALERT] LOW STOCK WARNING (Quantity < 5)");
                    System.out.println("==================================================================");
                    printTableHeader();
                    hasLowStock = true;
                }
                product.displayProduct();
            }
        }
        if (hasLowStock) {
            printTableFooter();
        } else {
            System.out.println("\n[INFO] All product stock levels are healthy (no items below 5 units).");
        }
    }

    /**
     * Generates a comprehensive summary report of the inventory including metrics
     * like total stock count, unique products, and valuation.
     */
    public void generateReport() {
        System.out.println("\n==================================================================");
        System.out.println("                   INVENTORY SUMMARY REPORT");
        System.out.println("==================================================================");
        
        int totalProducts = products.size();
        int totalQuantity = 0;
        double totalValuation = 0.0;
        int lowStockCount = 0;

        for (Product product : products) {
            totalQuantity += product.getQuantity();
            totalValuation += (product.getPrice() * product.getQuantity());
            if (product.getQuantity() < 5) {
                lowStockCount++;
            }
        }

        System.out.printf("Total Unique Products : %d\n", totalProducts);
        System.out.printf("Total Items in Stock  : %d\n", totalQuantity);
        System.out.printf("Total Valuation       : $%,.2f\n", totalValuation);
        System.out.printf("Low Stock Warnings    : %d item(s)\n", lowStockCount);
        System.out.println("==================================================================");

        if (lowStockCount > 0) {
            System.out.println("\n[TIP] Please select Option 4 (Update Product) to restock low-stock items.");
        }
    }

    // ==========================================
    // File Persistence Methods
    // ==========================================

    /**
     * Saves the current list of products to products.txt.
     * Demonstrates the use of FileWriter and BufferedWriter.
     */
    public void saveProductsToFile() {
        // Ensure parent directories exist (if any) or write to current dir
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Product product : products) {
                writer.write(product.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("\n[ERROR] Failed to save inventory data to file: " + e.getMessage());
        }
    }

    /**
     * Loads products from products.txt.
     * Demonstrates the use of FileReader, BufferedReader, and Exception Handling.
     */
    public void loadProductsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // File doesn't exist yet, which is expected on first run.
            return;
        }

        products.clear(); // Clear existing in-memory data before loading from disk
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Product product = Product.fromCsvString(line);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.out.println("\n[ERROR] Failed to load inventory data from file: " + e.getMessage());
        }
    }

    // ==========================================
    // Tabular Formatting Helper Methods
    // ==========================================

    private void printTableHeader() {
        System.out.println("+--------------+----------------------+-----------------+------------+----------+");
        System.out.println("| Product ID   | Product Name         | Category        | Price      | Quantity |");
        System.out.println("+--------------+----------------------+-----------------+------------+----------+");
    }

    private void printTableFooter() {
        System.out.println("+--------------+----------------------+-----------------+------------+----------+");
    }
}
