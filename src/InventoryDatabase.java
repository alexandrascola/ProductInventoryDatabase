import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDatabase {
    // Class variable for the database connection
    private static final String DATABASE_URL = "jdbc:sqlite:inventory.db";

    // Main Method
    public static void main(String[] args) {
        System.out.println("Welcome to the Inventory Database");
        System.out.println();



        // Try-catch block to handle DB operations
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            System.out.println("Connected to the Inventory Database");
            System.out.println();

            // Execute Operations
            displayAllProducts(connection);
            displayLowStockProducts(connection, 10);

            System.out.println("Making Some Purchases...");
            //update some products
            updateProductQuantity(connection, 4, 10);
            updateProductQuantity(connection, 10, 15);
            updateProductQuantity(connection, 15, 20);
            System.out.println();
            System.out.println("Updated item inventory: ");

            //display the updated inventory
            displayAllProducts(connection);


        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }




    // Database Operation Methods
    // Method to display all products
    public static void displayAllProducts(Connection connection) throws SQLException {
        String sql = "SELECT * FROM products";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("All products:");
            while (resultSet.next()) {

                displayProduct(resultSet);
            }
            System.out.println();
        }
    }

    //Method to display low-stock products
    public static void displayLowStockProducts(Connection connection, int threshold) throws SQLException {
        String sql = "SELECT * FROM products" +
                " WHERE Quantity < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, threshold);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println();
                System.out.println("Low Stock Products:");

                while (resultSet.next()) {
                    displayProduct(resultSet);
                }
                System.out.println();
            }
        }
    }

    //Method to update quantity of one product
    public static void updateProductQuantity(Connection connection, int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET Quantity = ? WHERE ProductID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setInt(2, productId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0)
                System.out.println("Updated product id " + productId + " to quantity " + quantity);
                else
                    System.out.println("No product found for Product ID: " + productId);
        }
    }

    //Helper method to display products
    public static void displayProduct(ResultSet resultSet) throws SQLException {
        //Ingest the result set as variables
        int productId = resultSet.getInt("ProductID");
        String productName = resultSet.getString("ProductName");
        String category = resultSet.getString("Category");
        double price = resultSet.getDouble("Price");
        int quantity = resultSet.getInt("Quantity");


        //Format print the results
        System.out.printf(
                "%d | %s | %s | $%.2f | Quantity: %d%n",
                productId,
                productName,
                category,
                price,
                quantity
        );

    }



}
