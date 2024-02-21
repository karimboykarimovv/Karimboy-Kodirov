package org.example.db;

import org.example.entitiy.Category;
import org.example.entitiy.Product;
import org.example.entitiy.TelegramUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface DB {
    List<TelegramUser> TELEGRAM_USERS = new ArrayList<>();
    List<Category> CATEGORIES = new ArrayList<>();
    List<Product> PRODUCTS = new ArrayList<>();

     static void generate(){
                 Category category1 = new Category("Yegulik",null);
                 Category category2 = new Category("Ichimliklar",null);
                 Category category3 = new Category("Sovg'alar",null);
                CATEGORIES.add(category3);
         Product cocaCola = new Product(
                 "Coca Cola",16000,"fiels/image/cola.jpg",category2.getId());

         Product pepsi = new Product(
                 "Pepsi",15000,"fiels/image/pepsi.jpg",category2.getId());
         PRODUCTS.add(pepsi);
         PRODUCTS.add(cocaCola);

         Product joynomoz = new Product(
                 "Joynamoz",70000,"fiels/image/joynamoz.jpg",category3.getId());

         Product gul = new Product(
                 "Gul",25000,"fiels/image/gul.jpg",category3.getId());
         PRODUCTS.add(joynomoz);
         PRODUCTS.add(gul);
                 CATEGORIES.add(category1);
         Category category4 = new Category("Mevalar",category1.getId());
                 CATEGORIES.add(category4);
                 Category category7 = new Category("Quruq mevalar",category4.getId());
                 Category category8 = new Category("Ho'l mevalar",category4.getId());
         Product product1 = new Product(
                 "Olma",20000,"fiels/image/olma.jpg",category8.getId());

         Product product2 = new Product(
                 "Anor",25000,"fiels/image/anor.jpg",category8.getId());

         Product product3 = new Product(
                 "Banan",21000,"fiels/image/banan.jpg",category8.getId());

         Product product4 = new Product(
                 "Nok",21000,"fiels/image/nok.jpg",category8.getId());
         PRODUCTS.add(product1);
         PRODUCTS.add(product2);
         PRODUCTS.add(product3);
         PRODUCTS.add(product4);
         CATEGORIES.add(category2);
     //mevalar
         Category category5 = new Category("Sabzavotalr",category1.getId());

         Product kartoshka = new Product(
                 "Kartoshka",5000,"fiels/image/kartoshka.jpg",category5.getId());

         Product piyoz = new Product(
                 "Piyoz",4000,"fiels/image/piyoz.jpg",category5.getId());
         PRODUCTS.add(piyoz);
         PRODUCTS.add(kartoshka);
        //sabzavotlar
         Category category6 = new Category("Shirinliklar",category1.getId());
         CATEGORIES.add(category6);
         Product pahlava = new Product(
                 "Pahlava",20000,"fiels/image/pahlava.jpg",category6.getId());

         Product tort = new Product(
                 "Tort",70000,"fiels/image/tort.jpg",category6.getId());
         PRODUCTS.add(pahlava);
         PRODUCTS.add(tort);
         // shirinliklar
         CATEGORIES.add(category5);

         CATEGORIES.add(category7);
         CATEGORIES.add(category8);

     }

    static Product findProductById(UUID selectedProdactId) {
       return   PRODUCTS.stream().filter(item -> item.getId().equals(selectedProdactId)).findFirst().get();
    }

    static Category findCategoryById(UUID categoryId) {
         return CATEGORIES.stream().filter(item -> item.getId().equals(categoryId)).findFirst().get();
    }
}
