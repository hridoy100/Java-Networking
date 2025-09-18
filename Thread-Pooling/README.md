# Thread Pooling Examples

This directory contains Java examples demonstrating different levels of thread pooling concepts.

## beginner.java

This file shows a basic implementation of a fixed-size thread pool using `Executors.newFixedThreadPool()`. It's a simple way to get started with managing a pool of worker threads to execute tasks.

## intermediate.java

This example delves deeper into thread pooling by using `ThreadPoolExecutor`. It demonstrates how to configure core pool size, maximum pool size, keep-alive time, and a `BlockingQueue` to manage tasks, offering more control over thread pool behavior.

## expert.java

This advanced example showcases a custom `RejectedExecutionHandler` for managing tasks that cannot be immediately executed by the thread pool. It also demonstrates the use of `Callable` tasks and `Future` to retrieve results from asynchronous computations, providing a more robust and flexible approach to thread pooling.
