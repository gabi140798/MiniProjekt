package kurs.service;

import kurs.model.Customer;
import kurs.model.Order;
import kurs.model.Product;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Service {

    private final Database database;

    public Service(Database database) {
        this.database = database;
    }

    //Zwróć listę produktów z kategorii "Books" z ceną większa niż 100
    public List<Product> findProductsFromCategoryWithPriceLargerThan(String category, double price) {
        return database.getProductList()
                .stream()
                .filter(p -> p.getCategory().equals(category) && p.getPrice() > price)
                .collect(Collectors.toList());
    }

    //Zwróć listę zamówień, w którym przynajmniej jeden produkt był z kategoria "Baby"
    public List<Order> findOrdersByCategory(String category) {
        return database.getOrderList().stream()
                .filter(o -> o.getProducts().stream().anyMatch(p -> p.getCategory().equals(category)))
                .collect(Collectors.toList());
    }

    //Zwróć listę produktów z kategorii "Toys" i obniż ich cenę o 10%
    public List<Product> reduceProductPriceBy10Percent(String category) {
        return database.getProductList().stream()
                .filter(p -> p.getCategory().equals(category))
                .peek(p -> p.setPrice(p.getPrice() * 0.9))
                .collect(Collectors.toList());
    }

    //Zwróć listę produktów, które były zamówione przez klientów z tier 2 pomiędzy 01.02.2021 a 01.04.2021
    public List<Product> findProductsOrderedByTier2BetweenDates(LocalDate after, LocalDate before) {
        return database.getOrderList().stream()
                .filter(o -> o.getCustomer().getTier() == 2)
                .filter(o -> (o.getOrderDate().isAfter(after) && o.getOrderDate().isBefore(before)))
                .flatMap(o -> o.getProducts().stream())
                .collect(Collectors.toList());
    }

    //Zwróć najtańszy produkt z kategorii "Books"
    public Optional<Product> findCheapestProduct(String category) {
        return database.getProductList().stream()
                .filter(p -> p.getCategory().equals(category))
                .min(Comparator.comparingDouble(Product::getPrice));
    }

    //Zwróć 3 ostatnio złożone zamówienia (orderDate)
    public List<Order> findLast3Orders() {
        return database.getOrderList().stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    //Pobierz listę zamówień, które zostały zamówione 15.03.2021, wyświetl te zamówienia na konsoli, a następnie zwróć listę produktów, które były w tych zamówieniach
    public List<Product> findProductsOrderedOnSpecificDate(LocalDate date) {
        return database.getOrderList().stream()
                .filter(o -> o.getOrderDate().equals(date))
                .peek(System.out::println)
                .flatMap(o -> o.getProducts().stream())
                .collect(Collectors.toList());
    }

    //Zwróć sumę cen wszystkich zamówień złożonych w Lutym
    public double findTotalOrdersValueInMonth(int year, int month) {
        return database.getOrderList().stream()
                .filter(o -> o.getOrderDate().getYear() == year && o.getOrderDate().getMonthValue() == month)
                .flatMapToDouble(o -> o.getProducts().stream().mapToDouble(Product::getPrice))
                .sum();
    }

    //Zwróć średnią cenę zamówień złożonych 15.03.2021
    public double averageOrderValueOnSpecificDate(LocalDate date) {
        return database.getOrderList().stream()
                .filter(o -> o.getOrderDate().equals(date))
                .mapToDouble(o -> o.getProducts().stream().mapToDouble(Product::getPrice).sum())
                .average()
                .orElse(0);
    }

    //Zwróć mapę w postaci id zamówienia - ilość zamówionych produktów ze wszystkich zamówień
    public Map<Long, Integer> getOrderIdToProductCount() {
        return database.getOrderList().stream()
                .collect(Collectors.toMap(Order::getId, o -> o.getProducts().size()));
    }

    //Zwróć mapę w postaci Customer - lista zamówień, czyli lista wszystkich zamówień danego klienta
    public Map<Customer, List<Order>> getCustomerToOrders() {
        return database.getOrderList().stream()
                .collect(Collectors.groupingBy(Order::getCustomer));
    }

    //Zwróć mapę w postaci Nazwa Kategorii - List nazw produktów, czyli list nazw wszystkich produktów z danej kategorii
    public Map<String, List<String>> getCategoryToProductNames() {
        return database.getProductList().stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.mapping(Product::getName, Collectors.toList())));
    }
}
