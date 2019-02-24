package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_3;

import java.util.ArrayList;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_3
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/24 16:50
 * @UpdateUser:
 * @UpdateDate: 2019/2/24 16:50
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelGroupMultiplier {

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        ArrayList<Thread> threads = new ArrayList<>();

        int rows = matrix1.length;
        int processors = Runtime.getRuntime().availableProcessors();
        int startIndex, endIndex, step;
        //可以理解为每个线程要计算的矩阵行数
        step = rows / processors;
        startIndex = 0;
        endIndex = step;
        //这里的关键是要保证每个线程分配的计算行数要平均，多出的那几行（小于processors）分配到最后生成的那个线程上
        for (int i = 0; i < processors; i++) {
            GroupMultiplierTask task = new GroupMultiplierTask(matrix1, matrix2, result, startIndex, endIndex);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            startIndex = endIndex;
            endIndex = i == processors - 2 ? rows : endIndex + step;
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
