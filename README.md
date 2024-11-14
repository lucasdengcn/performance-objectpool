# Performance Benchmark on Object Pool

## ConcurrentLinkedQueue

NOT suitable.

given it creates Node Object every time on return Object to Object Pool.
which cause huge amount of memory allocation. as following image showing.

![Node Allocation](./images/simplepool-node-creation.png)

## LinkedTransferQueue

Suitable.

### reference implementation on Storm BlazePool (https://github.com/chrisvest/stormpot)

![Memory Allocation](./images/blaze-pool-memory.png)


### Can't directly use LinkedTransferQueue as SimpleObjectPool2 showing.

![Node Allocation](./images/simplepool2-node.png)


## Storm BlazePool

* unbound
* cache in Local Thread

