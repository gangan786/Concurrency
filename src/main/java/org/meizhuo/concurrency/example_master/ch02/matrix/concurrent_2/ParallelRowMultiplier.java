package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_2;

import java.util.ArrayList;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_2
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/24 15:33
 * @UpdateUser:
 * @UpdateDate: 2019/2/24 15:33
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelRowMultiplier {

    public static void multiply(double[][] matrix1, double matrix2[][], double[][] result) {
        ArrayList<Thread> threads = new ArrayList<>();
        int rows = matrix1.length;

        for (int i = 0; i < rows; i++) {
            RowMultiplierTask task = new RowMultiplierTask(result, matrix1, matrix2, i);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            if (threads.size() % 2 == 0) {
                waitForThread(threads);
            }
        }
    }

    private static void waitForThread(ArrayList<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }

}
