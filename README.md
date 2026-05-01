This is an exercise on implementing my own cache with various eviction policies which includes:
 * LRU (Least Recent Used) ----- remove item that hasnt been accessed in the longest time (period bw last accessed till now)
 * [pending] FIFO (First In, First Out) -- remove item that first arrive regardless of the last accessed time. (queue)
 * [pending] TTL (Time To Live) ---------- remove item that have expired based on age. i.e age > 5 hours, 5 day etc
 * [pending] LFU (Least Frequently Used) - remove item that used least often overall (total time accessed)


Features
* User can select from a list of eviction policy that best suit his use case.
* User can define the maximum size of his cache.
* User can evaluate the effectiveness of the cache by hit ratio.


Lacking
* Current cache is not thread-safe. 


How to use
* 