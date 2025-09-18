import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class expert {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Custom RejectedExecutionHandler
        RejectedExecutionHandler rejectionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString() + " is rejected");
            }
        };

        // ThreadPoolExecutor with custom parameters and rejection handler
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, // corePoolSize
                4, // maximumPoolSize
                1, // keepAliveTime
                TimeUnit.MINUTES, // unit
                new ArrayBlockingQueue<>(2), // workQueue
                Executors.defaultThreadFactory(), // threadFactory
                rejectionHandler // rejectedExecutionHandler
        );

        List<Future<Long>> resultList = new ArrayList<>();

        // Submit Callable tasks
        for (int i = 0; i < 10; i++) {
            Callable<Long> worker = new FactorialCalculator("Task " + i, i + 1);
            Future<Long> future = executor.submit(worker);
            resultList.add(future);
        }

        // Shut down the executor service
        executor.shutdown();

        // Wait until all tasks are finished
        while (!executor.isTerminated()) {
            // Do nothing, just wait
        }

        System.out.println("\nResults:");
        for (Future<Long> future : resultList) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished all threads");
    }
}

class FactorialCalculator implements Callable<Long> {
    private String taskName;
    private int number;

    public FactorialCalculator(String taskName, int number) {
        this.taskName = taskName;
        this.number = number;
    }

    @Override
    public Long call() throws Exception {
        long result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
            Thread.sleep(100); // Simulate some work
        }
        System.out.println(Thread.currentThread().getName() + " (Calculated Factorial of " + number + "): " + result);
        return result;
    }
}