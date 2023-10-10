package kurs;

import kurs.service.Database;
import kurs.service.Service;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Database database = new Database();
        Service service = new Service(database);

        System.out.println("Lista produktów z kategorii 'Books' z ceną większą niż 100: ");
        System.out.println(service.findProductsFromCategoryWithPriceLargerThan("Books",100).size());

        System.out.println("Lista zamówień, w którym przynajmniej jeden produkt był z kategoria 'Baby': ");
        System.out.println(service.findOrdersByCategory("Baby").size());

        System.out.println("Lista produktów z kategorii 'Toys' z obniżką ceny o 10%: ");
        System.out.println(service.reduceProductPriceBy10Percent("Toys").size());

        System.out.println("Lista produktów, które były zamówione przez klientów z tier 2 pomiędzy 01.02.2021 a 01.04.2021: ");
        System.out.println(service.findProductsOrderedByTier2BetweenDates(LocalDate.of(2021,2,1),LocalDate.of(2021,4,1)).size());

        System.out.println("Najtańszy produkt z kategorii 'Books': ");
        System.out.println(service.findCheapestProduct("Books"));

        System.out.println("3 ostatnio złożone zamówienia: ");
        System.out.println(service.findLast3Orders());

        System.out.println("Lista zamówień, które zostały zamówione 15.03.2021 oraz produkty zamawiane: ");
        System.out.println(service.findProductsOrderedOnSpecificDate(LocalDate.of(2021, 3,15)).size());

        System.out.println("Suma cen wszystkich zamówień złożonych w Lutym: ");
        System.out.println(service.findTotalOrdersValueInMonth(2021,2));

        System.out.println("Średnia cena zamówień złożonych 15.03.2021");
        System.out.println(service.averageOrderValueOnSpecificDate(LocalDate.of(2021,3,15)));

        System.out.println("Mapa w postaci id zamówienia - ilość zamówionych produktów: ");
        System.out.println(service.getOrderIdToProductCount());

        System.out.println("Mapa w postaci Customer - lista zamówień: ");
        System.out.println(service.getCustomerToOrders());

        System.out.println("Mapa w postaci Nazwa Kategorii - List nazw produktów: ");
        System.out.println(service.getCategoryToProductNames());

    }
}
