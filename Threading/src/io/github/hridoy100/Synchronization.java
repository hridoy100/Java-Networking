package io.github.hridoy100;


class Printer {
    void printAssignment(Person p){
        System.out.println(p.name+"-->Printing started...");
        for(int i=0; i<p.pages; i++){
            System.out.println(p.name + "--> printed page#"+(i+1));
        }
        System.out.println(p.name+"-->Completed...");
    }
}

class Person implements Runnable {

    String name;
    final Printer printer;
    int pages;
    Thread t;

    Person(String name, Printer p, int pages){
        this.name= name;
        this.printer = p;
        this.pages = pages;
        t= new Thread(this, name);
        t.start();
    }


    @Override
    public void run() {
        synchronized (printer) {
            printer.printAssignment(this);
        }
    }
}

public class Synchronization {
    public static void main(String[] args) {
        Printer printer = new Printer();
        Person hridoy = new Person("hridoy", printer, 5);
        Person mahim = new Person("mahim", printer, 10);
        Person rayhan = new Person("rayhan", printer, 8);
    }
}
