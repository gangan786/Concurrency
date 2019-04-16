package org.meizhuo.concurrency.art;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Classname Mutex
 * @Description 使用AQS实现独占锁
 * @Date 2019/4/16 10:23
 * @Created by Gangan
 */
public class Mutex implements Lock {

    private static class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalStateException("此线程未持有该锁导致释放异常");
            }
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final Sync sync = new Sync();


    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public static int test = 0;

    public static void main(String[] args) {

        Mutex mutex = new Mutex();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,
                5,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1024)
        );

        for (int i = 0; i < 20; i++) {
            //构建测试用例
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mutex.lock();
                    try {
                        test++;
                    } finally {
                        mutex.unlock();
                    }


                }
            };
            //提交执行
            executor.submit(runnable);

        }

        try {
            Thread.sleep(500);
            System.out.println(test);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
