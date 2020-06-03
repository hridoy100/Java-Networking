package io.github.hridoy100;

class BarberShop {
    void getHairCut(String style){
        System.out.print("Started cutting [ " + style);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" ]");
    }
}


class Person implements Runnable {

    String style;
    final BarberShop shop;
    Thread t;

    Person(BarberShop shop, String style){
        this.shop = shop;
        this.style = style;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        synchronized (shop) {
            shop.getHairCut(style);
        }
    }
}


public class Synchronization {
    public static void main(String[] args) {
        BarberShop shop = new BarberShop();
        Person person1 = new Person(shop, "Style1");
        Person person2 = new Person(shop, "Style2");
        Person person3 = new Person(shop, "Style3");
    }
}
