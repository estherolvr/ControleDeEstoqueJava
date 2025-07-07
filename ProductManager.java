package product_management_gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductManager {
    private List<Product> products;
    private int nextId;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.nextId = 1;
    }

    public void addProduct(String name, double price, int quantity) {
        Product newProduct = new Product(nextId++, name, price, quantity);
        products.add(newProduct);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Optional<Product> getProductById(int id) {
        return products.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst();
    }

    public boolean updateProduct(int id, String name, double price, int quantity) {
        Optional<Product> productOpt = getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }
}

