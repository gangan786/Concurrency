# Concurrency

用于并发知识学习

知识点总结

### 4-1 原子性

#### AtomicXXX：CAS Unsafe.compareAndSwapInt

- AtomicInteger的incrementAndGet()方法实际调用了Unsafe的getAndAddInt()的方法，原理上是利用CAS（Compare And Swap）算法，工作内存与主内存的值进行对比（不一定完全一样），如果相等就更新

#### AutomicLong , LongAdder

- 区别：

### 4-2 原子性

#### AtomicReference，AtomicReferenceFieldUpdater

- Atomicreference 使用

```java
@Slf4j
@ThreadSafe
public class AtomicExample4 {
    private static AtomicReference<Integer> count = new AtomicReference<>(0);
    public static void main(String[] args) {
        count.compareAndSet(0, 2); // 2
        count.compareAndSet(0, 1); // no
        count.compareAndSet(1, 3); // no
        count.compareAndSet(2, 4); // 4
        count.compareAndSet(3, 5); // no
        log.info("count:{}", count.get());//输出4
    }
}
```

- AtomicReferenceFieldUpdater 使用

为指定中的对象中的属性提供原子操作，该属性必须满足 `volatile`声明和非`state`

```java
@Slf4j
@ThreadSafe
public class AtomicExample5 {

    private static AtomicIntegerFieldUpdater<AtomicExample5> updater =
            AtomicIntegerFieldUpdater.newUpdater(AtomicExample5.class, "count");

    @Getter
    public volatile int count = 100;

    public static void main(String[] args) {

        AtomicExample5 example5 = new AtomicExample5();

        if (updater.compareAndSet(example5, 100, 120)) {
            log.info("update success 1, {}", example5.getCount());
        }

        if (updater.compareAndSet(example5, 100, 120)) {
            log.info("update success 2, {}", example5.getCount());
        } else {
            log.info("update failed, {}", example5.getCount());
        }
    }
}
```

#### AtomicStampReference:CAS的ABA问题

- 对变量增加版本号的属性（stamp）

#### AtomicBoolean

- 可以让某段代码只执行一次不会重复（多线程，工作内存与主内存不一致的环境）

### 4-3 锁

#### synchronized

- 修饰代码块
- 修饰方法（子类不会从父类中继承synchronized的属性，因为synchronized不属于方法声明的一部分）
- 修饰静态方法
- 修饰类（以下这种方式可以看做是修饰类的一种）

~~~java
 // 修饰一个类
    public static void test1(int j) {
        synchronized (SynchronizedExample2.class) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }
~~~



#### 对比

+ synchronized：不可中断锁，适合竞争不激烈，可读性好
+ Lock：可中断锁，多样化同步，竞争激烈时能维持常态
+ Atomic：竞争激烈时能维持常态，比Lock性能好，但是只能同步一个值



### 4-4 线程安全性-可见性

#### volatile

+ volatile适用于Boolean这种具有标记作用的修饰
+ DCL（double check lock）





### 4-5线程安全性-有序性

+ 先行发生原则

#### 总结

+ 原子性：Atomic包，CAS算法，synchronized，Lock
+ 可见性：synchronized，volatile
+ 有序性：happens-before

