# Multithreading Concepts

### Deadlock
- Deadlock can occur if two threads are blocked on each other.
- For example if there are two objects, both threads should have lock on both of the objects and the lock sequence in both the thread is different, in that case deadlock will occur as both thread will go in waiting state.
- Example: 


### Live Lock

### Reentrant Lock
- The type of lock which can be acquired by a thread without blocking itself is called reentrant
- If we use normal lock and same thread wants to acquire the lock, in that case the thread will be put on hold as the object is already locked. Eventhough the object is locked by the same thread.
- This issue can be resolved by Reentrant lock
- Whenever a thread tries to acquire the reentrant lock, it checks if the lock is available
  - If it is available, the lock is given to the thread
  - If not the lock checks if the lock is held by the same thread, If yes, the lock is given to the thread.
  - If the lock is acquired by other thread, the current thread is put on wait.
- This will be particularly helpful in recursive calls where lock is required.
- **Condition** are a construct which gives us functionality like wait and notify without using synchronised keyword.
- When we use **wait, notify or notifyAll**, it should be called from inside a **synchronised block/method** or else it will throw **IllegalMonitorStateException**.
- When we use reentrant lock or any other implementation of Lock interface, we don't use synchronised, so if we want behaviour like **wait , notify, notifyAll**, we can use Condition object's, **await, signal and signalAll**.
- Syntax: `Condition = loc.newCondition`