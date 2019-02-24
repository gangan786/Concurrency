package org.meizhuo.concurrency.example_master.ch02.matrix.serial;

import java.util.Date;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/23 15:43
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 15:43
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class SerialMain {
    public static void main(String[] args) {
        double[][] matrix1 = MatrixGenerator.generate(2000, 2000);
        double[][] matrix2 = MatrixGenerator.generate(2000, 2000);

        double[][] result = new double[matrix1.length][matrix2[0].length];
        Date start = new Date();
        SerialMultiplier.multiply(matrix1,matrix1,result);
        Date end = new Date();
        System.out.println("串行耗时为：" + (end.getTime() - start.getTime()));
        //串行耗时为：117307
    }
}
