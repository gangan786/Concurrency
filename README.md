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





### 5-1 安全发布对象 发布与逸出

#### 问题

+ 对象逸出，对象未完成构造之前不可以将其发布（可选用工厂模式等）
+ 对象可见性，在对象可见范围之外出现该对象的引用

### 5-2 安全发布对象-四种方法

1. 在静态初始化函数中初始化一个对象应用（懒汉模式）
2. 将对象的引用保存到volatile类型域或者AtomicReference对象中（DCL中解决指令重排的措施）
3. 将对象的引用保存到一个由锁保护的域中（DCL的同步锁机制）
   - 以上三种方式都可以通过单例的各种实现加以理解
4. 将对象的引用保存到某个正确构造对象的final类型域中

然后拓展讲了各种单例的实现原理和需要注意的点

#### DCL（Double Check Lock）

其中以DCL模式的实现最能总结前三种方法的综合调用，这里我深入分析下目的

~~~java
package org.meizhuo.concurrency.example.singleton;
import org.meizhuo.concurrency.annoations.ThreadSafe;
/**
 * 懒汉模式 -》 双重同步锁单例模式
 * 单例实例在第一次使用时进行创建
 */
@ThreadSafe
public class SingletonExample5 {

    // 私有构造函数
    private SingletonExample5() {

    }

    // 1、memory = allocate() 分配对象的内存空间
    // 2、ctorInstance() 初始化对象
    // 3、instance = memory 设置instance指向刚分配的内存

    // 单例对象 volatile + 双重检测机制 -> 禁止指令重排
    private volatile static SingletonExample5 instance = null;

    // 静态的工厂方法
    public static SingletonExample5 getInstance() {
        if (instance == null) { // 双重检测机制        // B
            synchronized (SingletonExample5.class) { // 同步锁
                if (instance == null) {
                    instance = new SingletonExample5(); // A - 3
                }
            }
        }
        return instance;
    }
}

~~~

从上往下看是

1. 私有构造函数：这是肯定的，每个要实现单例的类都必须将构造函数私有化

2. instance变量的private，volatile，static属性设置的必要性：

   - private：该变量保存着唯一的实例对象，对其他类必须保持不可见性，所以定义为private是必须的

   - volatile：这里主要的作用是防止单例实例化过程中出现指令重排的情况，比如正常的顺序是：

     1. memory = allocate() 分配对象的内存空间
     2. 初始化对象
     3. instance = memory 设置instance指向刚分配的内存

     如果不用volatile修饰的话会出现的顺序为：

     1. memory = allocate() 分配对象的内存空间
     2. instance = memory 设置instance指向刚分配的内存
     3. 初始化对象

     这样就会出现一种情况（发生指令重排）：当线程A执行到步骤2却发生了中断但却有了指向地址，那么一旦另一个线程B去获取实例，那么就会获得一个未实例化的对象，这样就可能会出现问题，所以使用volatile就可以解决这个问题，因为这样就会按照正常的顺序执行

     初此之外还保持了变量的可见性

   - static：用static修饰的理由毫无疑问

3. 难点：静态工厂方法

   ~~~java
   // 静态的工厂方法
       public static SingletonExample5 getInstance() {
           if (instance == null) { // 双重检测机制  第一次判空      // B
               synchronized (SingletonExample5.class) { // 同步锁
                   if (instance == null) { //第二次判空
                       instance = new SingletonExample5(); // A - 3
                   }else{
                       //发生了第二种切换
                   }
               }
           }
           return instance;
       }	
   ~~~

   这里先分析几个会出现不安全发布对象的几种情况：

   - 多个线程同时获得instance的值为null，都尝试进行实例化。所以这边对类进行加锁，即使有多个线程同时进去了也只有一个线程能获得锁，而且只有范围代码块执行完毕之后才会释放锁。
   - 如果在获得锁之前另外其他线程有可能已经完成了初始化的操作，可是如果当前线程已经完成第一个判空进来了，那么在获得锁之后任然有可能再次实例化覆盖之前的对象，所以这也是要第二次判空的原因所在
   - 其实以上的各种情况出现的根本原因是：因为各线程发生切换的点不同
     1. 第一种发生切换的点是：第一次判空之后获得同步锁之前
     2. 第二种发生切换的点是：获得锁之后第二次判空之前





### 6-1 不可变对象

	#### 不可变对象需要满足的条件

+ 对象创建以后状态就不能修改
+ 对象所有域都是final类型
+ 对象是正确创建的（在对象创建期间，this引用没有逸出）

#### final关键字：类，方法，变量

+ 重点在于final修饰的变量特性

### 6-2不可变对象

对集合中的元素的修改和添加操作进行限制或者说禁止

+ `java.util.Collections`提供的`unmodifiable***()`方法可以实现集合里面的元素具有final的不可变性
+ `com.google.common.collect`中的各种`Immutable***`类



### 6-3,6-4 线程封闭

+ Ad-hoc线程封闭：程序控制实现，效果差，古老，不建议使用，忽略
+ 堆栈封闭：局部变量，无并发问题
+ ThreadLocal线程封闭：强烈推荐使用



### 6-5,6-6 线程不安全类与写法

+ StringBuilder （线程不安全） -->  StringBuffer （线程安全）

  两种字符串容器的使用场景，优缺点

+ SimpleDateFormat（线程不安全）--> JodaTime：当多个线程共享同一个SimpleDateFormat并对其进行操作时会抛出异常，一般的处理方法是将其定义为局部变量，利用堆栈封闭保证当前只要单个线程对其进行操作。JodaTime是一个第三方库，保证线程安全，不会出现上面这种情况。但是他们之间的关系并不是像StringBuilder和StringBuffer，因为JodaTime的实现（不仅在性能方面）比SimpleDateFormat好很多。所以推荐使用JodaTime 

  ~~~xml
  <dependency>
  	<groupId>joda-time</groupId>
  	<artifactId>joda-time</artifactId>
  	<version>2.9</version>
  </dependency>
  ~~~

+ ArrayList，HashSet，HashMap（线程不安全）等Collections

+ 先检查再执行：if(condition(a)){handle(a)}：类比DCL的判空处理导致的线程不安全



### 6-7,6-8 同步容器

+ ArrayList --> Vector,Stack

  对于Vector来说他的add等大部分方法都使用了synchronized修饰，但这并不意味着它是绝对线程安全的类，视屏中使用如下代码验证，即在两个线程中一个get元素，一个remove元素，这样会因为get到remove掉的元素而**java.lang.ArrayIndexOutOfBoundsException**异常，个人认为这种问题比较难解决，把他视为线程不安全的理由有点牵强，这更涉及到逻辑层面的问题

  ~~~java
  package org.meizhuo.concurrency.example.syncContainer;
  import org.meizhuo.concurrency.annoations.NotThreadSafe;
  import java.util.Vector;
  
  @NotThreadSafe
  public class VectorExample2 {
  
      private static Vector<Integer> vector = new Vector<>();
  
      public static void main(String[] args) {
  
          while (true) {
  
              for (int i = 0; i < 10; i++) {
                  vector.add(i);
              }
  
              Thread thread1 = new Thread() {
                  public void run() {
                      for (int i = 0; i < vector.size(); i++) {
                          vector.remove(i);
                      }
                  }
              };
  
              Thread thread2 = new Thread() {
                  public void run() {
                      for (int i = 0; i < vector.size(); i++) {
                          vector.get(i);
                      }
                  }
              };
              thread1.start();
              thread2.start();
          }
      }
  }
  
  ~~~

  关于Vector还有一个遍历问题：

+ HashMap --> HashTable(key,value不能为空)

+ Collection.synchronizedXXX(List,Set,Map)

  **出于性能与功能性的考虑，现在比较少用同步容器，转而使用并发容器进行替代**


### 6-9 并发容器  J.U.C

#### 简单使用介绍

+ ArrayList --> CopyOnWriteArrayList
+ HashSet，TreeSet --> CopyOnWriteArraySet，ConcurrentSkipListSet
+ HashMap，TreeMap --> ConcurrentHashMap，ConcurrentSkipListMap



#### 安全共享对象策略-总结

+ 线程限制：一个被线程限制的对象，有线程独占，并且只能被占有她的线程修改（ThreadLocal）
+ 共享只读：一个共享只读的对象，在没有额外同步的情况下，可以被多个线程并发访问，但是任何线程都不能修改他（不可变对象）
+ 线程安全对象：一个线程安全的对象或者容器，在内部通过同步机制来保证线程安全，所以其他线程无需额外的同步就可以通过公共接口随意访问它（同步容器）
+ 被守护对象：被守护对象只能通过获取特定的锁来访问（并发容器 ）









### 7-1  J.U.C之 AQS

AbstractQueuedSynchronizer -- AQS：J.U.C的核心



### 7-2 ,7-3,7-4,7-5,7-6 AQS同步组件

+ CountDownLatch

  适用场景：将复杂过程拆分为多个子线程任务，当所有子线程任务都完成以后才进行下一步的统计操作处理

+ Semaphore：信号量

  对指定代码做并发处理，实现让代码以指定的并发数进行执行

+ CyclicBarrier

  类似于CountDownLatch

  使用思想类似于：当线程都执行到某指定步骤后await，直到其他所有线程都在await说明ready好下一步操作，这时所有线程又开始继续下一步执行

+ ReentrantLock

  高性能要求可以使用，不过相对于synchronized来说使用稍微复杂，不利于调试检测

+ ReentrantReadWriterLock

  读写锁分离，适用于对共享数据进行同步封装

+ StampedLock