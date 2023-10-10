package kurs.service;

import kurs.exception.NotFoundException;
import kurs.model.Customer;
import kurs.model.Order;
import kurs.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Database {

    private List<Customer> customerList;
    private List<Product> productList;
    private List<Order> orderList;

    public Database() {
        readCustomers();
        readProducts();
        readOrders();
    }

    private void readCustomers() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/data/Customers.txt"))) {
            customerList = br.lines()
                    .skip(1)
                    .map(x -> {
                        //1, Stefan Walker, 1
                        String[] tab = x.split(", ");
                        return new Customer(Long.parseLong(tab[0]), tab[1], Integer.parseInt(tab[2]));
                    })
                    .collect(Collectors.toList());
            System.out.printf("Created %s customers%n", customerList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/data/Products.txt"))) {
            productList = br.lines()
                    .skip(1)
                    .map(x -> {
                        //1, omnis quod consequatur, Games, 184.83
                        String[] tab = x.split(", ");
                        return new Product(Long.parseLong(tab[0]), tab[1], tab[2], Double.parseDouble(tab[3]));
                    })
                    .collect(Collectors.toList());
            System.out.printf("Created %s products%n", productList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readOrders() {
        //id, order_date, delivery_date, status, customer_id
        //1, 2021-02-28, 2021-03-08, NEW, 5
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/data/Orders.txt"))) {
            orderList = br.lines()
                    .skip(1)
                    .map(x -> {
                        String[] tab = x.split(", ");
                        return new Order(Long.parseLong(tab[0]),
                                LocalDate.parse(tab[1]),
                                LocalDate.parse(tab[2]),
                                tab[3],
                                findCustomerById(Long.parseLong(tab[4])));
                    })
                    .collect(Collectors.toList());
            System.out.printf("Created %s orders%n", orderList.size());
            readOrderProductsRelation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readOrderProductsRelation() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/data/Order_product_relation.txt"))) {
            br.lines()
                    .skip(1)
                    .forEach(x -> {
                        String[] tab = x.split(", ");
                        findOrderById(Long.parseLong(tab[0])).getProducts().add(findProductById(Long.parseLong(tab[1])));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findProductById(long id) {
        return productList.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", id)));
    }

    public Customer findCustomerById(long id) {
        return customerList.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Customer with id %s not found", id)));
    }

    public Order findOrderById(long id) {
        return orderList.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s not found", id)));
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }
}
