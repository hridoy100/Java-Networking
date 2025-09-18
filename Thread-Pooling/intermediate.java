import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class intermediate {

    public static void main(String[] args) {
        // Core pool size: 2, Maximum pool size: 4, Keep-alive time: 10 seconds
        // Work queue: ArrayBlockingQueue with capacity 2
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, // corePoolSize
                4, // maximumPoolSize
                10, // keepAliveTime
                TimeUnit.SECONDS, // unit
                workQueue // workQueue
        );

        // Submit 10 tasks
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("Task " + i);
            executor.execute(worker);
            System.out.println("Task " + i + " submitted. Pool size: " + executor.getPoolSize() + ", Queue size: " + workQueue.size());
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

// Reusing WorkerThread from beginner.java for simplicity
// class WorkerThread implements Runnable {
//     private String taskName;
//
//     public WorkerThread(String taskName) {
//         this.taskName = taskName;
//     }
//
//     @Override
//     public void run() {
//         System.out.println(Thread.currentThread().getName() + " (Start) " + taskName);
//         processCommand();
//         System.out.println(Thread.currentThread().getName() + " (End) " + taskName);
//     }
//
//     private void processCommand() {
//         try {
//             Thread.sleep(2000); // Simulate some work
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
// }
