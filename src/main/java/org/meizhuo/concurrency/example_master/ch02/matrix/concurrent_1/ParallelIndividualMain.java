package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1;

import org.meizhuo.concurrency.example_master.ch02.matrix.serial.MatrixGenerator;
import org.meizhuo.concurrency.example_master.ch02.matrix.serial.SerialMultiplier;

import java.util.Date;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/23 20:47
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 20:47
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelIndividualMain {
    public static void main(String[] args) {
        double[][] matrix1 = MatrixGenerator.generate(2000, 2000);
        double[][] matrix2 = MatrixGenerator.generate(2000, 2000);

        double[][] result = new double[matrix1.length][matrix2[0].length];
        Date start = new Date();
        ParallelIndividualMultiplier.multiply(matrix1,matrix1,result);
        Date end = new Date();
        System.out.println("（每个元素对应一个线程执行）并行耗时为：" + (end.getTime() - start.getTime()));
        //输出结果为：（每个元素对应一个线程执行）并行耗时为：847256
    }
}
