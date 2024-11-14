# Performance Benchmark on Object Pool

## ConcurrentLinkedQueue

Can't directly use for Object Pool.

given it creates Node Object every time on return Object to Object Pool.
which cause huge amount of memory allocation. as following image showing.

![Node Allocation](./images/simplepool-node-creation.png)

## LinkedTransferQueue

### reference implementation on Storm BlazePool (https://github.com/chrisvest/stormpot)

![Memory Allocation](./images/blaze-pool-memory.png)


### Can't directly use LinkedTransferQueue as following image showing.

![Node Allocation](./images/simplepool2-node.png)


## Storm BlazePool

* unbound
* cache borrowed Object in Local Thread
* not perform well in virtual thread.

## In Concurrent

### Virtual Thread 

![Virtual Thread Allocation](./images/virtual-thread.png)

### Executors

![Thread Pool Allocation](./images/thread-pool.png)

