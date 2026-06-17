/**
 * The Product class represents an item in the inventory.
 * It demonstrates encapsulation by keeping fields private and
 * exposing them through public getters and setters with validation logic.
 *
 * @author Mummana Devi Prasad
 */
public class Product {
    // Encapsulated fields
    private String productId;
    private String productName;
    private String category;
    private double price;
    private int quantity;

    /**
     * Parameterized Constructor to initialize a Product.
     *
     * @param productId   Unique identifier of the product
     * @param productName Name of the product
     * @param category    Category of the product
     * @param price       Unit price of the product
     * @param quantity    Stock quantity of the product
     */
    public Product(String productId, String productName, String category, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        setPrice(price); // Use setter to enforce validation rules
        setQuantity(quantity); // Use setter to enforce validation rules
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Sets the product price. Validates that the price is not negative.
     *
     * @param price Unit price
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the product quantity. Validates that quantity is not negative.
     *
     * @param quantity Stock level
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // ==========================================
    // Display and Helper Methods
    // ==========================================

    /**
     * Displays the product details in a structured tabular row format.
     */
    public void displayProduct() {
        System.out.printf("| %-12s | %-20s | %-15s | $%9.2f | %8d |\n",
                productId, productName, category, price, quantity);
    }

    /**
     * Serializes product data to a comma-separated format for saving to a file.
     *
     * @return CSV-compatible string
     */
    public String toCsvString() {
        return productId + "," + productName + "," + category + "," + price + "," + quantity;
    }

    /**
     * Deserializes product data from a comma-separated string.
     *
     * @param csvLine A line of text containing comma-separated product fields
     * @return Product object or null if parsed incorrectly
     */
    public static Product fromCsvString(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length == 5) {
                String id = parts[0].trim();
                String name = parts[1].trim();
                String category = parts[2].trim();
                double price = Double.parseDouble(parts[3].trim());
                int quantity = Integer.parseInt(parts[4].trim());
                return new Product(id, name, category, price, quantity);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing product line from file: " + csvLine);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
