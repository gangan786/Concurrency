package org.meizhuo.concurrency.example_master.ch02.matrix.serial;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/23 15:12
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 15:12
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class SerialMultiplier {

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {

        //matrix1的行数
        int rows1 = matrix1.length;
        //matrix1的列数
        int columns1 = matrix1[0].length;
        //matrix2的列数
        int columns2 = matrix2[0].length;

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                result[i][j] = 0;
                for (int k = 0; k < columns1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

    }

}
