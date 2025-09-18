import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class beginner {

    public static void main(String[] args) {
        // Create a fixed thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Submit 10 tasks to the thread pool
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("Task " + i);
            executor.execute(worker);
        }

        // Shut down the executor service
        executor.shutdown();

        // Wait until all tasks are finished
        while (!executor.isTerminated()) {
            // Do nothing, just wait
        }

        System.out.println("Finished all threads");
    }
}

class WorkerThread implements Runnable {
    private String taskName;

    public WorkerThread(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " (Start) " + taskName);
        processCommand();
        System.out.println(Thread.currentThread().getName() + " (End) " + taskName);
    }

    private void processCommand() {
        try {
            Thread.sleep(2000); // Simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
