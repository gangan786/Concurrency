package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1;

import java.util.ArrayList;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/23 20:24
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 20:24
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelIndividualMultiplier {
    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        ArrayList<Thread> threads = new ArrayList<>();

        //matrix1的行数
        int rows1 = matrix1.length;
        //matrix2的列数
        int columns2 = matrix2[0].length;

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                IndividualMultiplierTask task = new IndividualMultiplierTask(result, matrix1, matrix2, i, j);
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);

                if (threads.size() % 2 == 0) {
                    waitForThread(threads);
                }
            }
        }
    }

    private static void waitForThread(ArrayList<Thread> threads) {
        for (Thread thread : threads) {
            try {
                //当前线程会处于等待状态，直到thread线程执行完毕才会返回当前线程继续执行
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //清除掉已执行完毕的线程
        threads.clear();
    }
}
