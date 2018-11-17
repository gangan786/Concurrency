package org.meizhuo.concurrency.example.singleton;


import org.meizhuo.concurrency.annoations.NotThreadSafe;

/**
 * 懒汉模式
 * 单例实例在第一次使用时进行创建
 */
@NotThreadSafe
public class SingletonExample1 {

    // 私有构造函数
    private SingletonExample1() {

    }

    // 单例对象
    private static SingletonExample1 instance = null;

    // 静态的工厂方法
    public static SingletonExample1 getInstance() {
        //如果有多个线程同时访问这段代码，获得为null的信号，
        //那么必然会产生多个实例，违背了单例
        if (instance == null) {
            instance = new SingletonExample1();
        }
        return instance;
    }
}
