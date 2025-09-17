package io.github.hridoy100;


/**
 * This class represents a shared resource (a printer) that multiple threads
 * (Person objects) will try to access. The {@code printAssignment} method
 * is synchronized to ensure that only one thread can print at a time,
 * preventing race conditions.
 */
class Printer {
    /**
     * Prints an assignment for a given person. This method is synchronized
     * to ensure thread safety, meaning only one thread can execute this
     * method at any given time.
     *
     * @param personName The name of the person whose assignment is being printed.
     * @param numberOfPages The number of pages in the assignment.
     */
    public synchronized void printAssignment(String personName, int numberOfPages) {
        System.out.println(personName + "--> Printing started...");
        for (int i = 0; i < numberOfPages; i++) {
            System.out.println(personName + "--> printed page #" + (i + 1));
            try {
                // Simulate time taken to print a page
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(personName + "--> Printing interrupted.");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break;
            }
        }
        System.out.println(personName + "--> Completed printing.");
    }
}

/**
 * This class represents a person who needs to print an assignment.
 * Each person runs as a separate thread and uses a shared {@link Printer} object.
 */
class Person implements Runnable {

    private final String name; // The name of the person
    private final Printer printer; // The shared printer resource
    private final int pages; // The number of pages the person needs to print
    private final Thread thread; // The thread associated with this person

    /**
     * Constructs a new Person.
     *
     * @param name The name of the person.
     * @param printer The {@link Printer} instance to use.
     * @param pages The number of pages to print.
     */
    Person(String name, Printer printer, int pages) {
        this.name = name;
        this.printer = printer;
        this.pages = pages;
        this.thread = new Thread(this, name);
        this.thread.start(); // Start the thread upon construction
    }

    /**
     * Returns the {@link Thread} object associated with this person.
     *
     * @return The {@link Thread} object.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * The main execution logic for the person thread.
     * It calls the synchronized {@code printAssignment} method of the {@link Printer}.
     */
    @Override
    public void run() {
        // The synchronization is now handled within the Printer class's method.
        // This thread simply calls the method.
        printer.printAssignment(this.name, this.pages);
    }
}

/**
 * This class demonstrates thread synchronization using a shared {@link Printer} resource.
 * Multiple {@link Person} threads attempt to use the same printer, and synchronization
 * ensures that printing jobs are completed one after another without interleaving.
 */
public class Synchronization {
    public static void main(String[] args) {
        System.out.println("Main Thread Started: Demonstrating Thread Synchronization.");

        // Create a single shared Printer instance
        Printer sharedPrinter = new Printer();

        // Create multiple Person threads, all sharing the same printer
        Person hridoy = new Person("Hridoy", sharedPrinter, 5);
        Person mahim = new Person("Mahim", sharedPrinter, 10);
        Person rayhan = new Person("Rayhan", sharedPrinter, 8);

        System.out.println("All Person threads created and started. Waiting for them to finish...");

        try {
            // Wait for all Person threads to complete their printing jobs
            hridoy.getThread().join();
            mahim.getThread().join();
            rayhan.getThread().join();
        } catch (InterruptedException e) {
            System.err.println("Main Thread interrupted while waiting for Person threads to finish.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        System.out.println("All Person threads have completed their tasks.");
        System.out.println("Main Thread Exited.");
    }
}