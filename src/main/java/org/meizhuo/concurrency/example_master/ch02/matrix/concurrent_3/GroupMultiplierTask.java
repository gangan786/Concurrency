package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_3;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_3
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/24 16:18
 * @UpdateUser:
 * @UpdateDate: 2019/2/24 16:18
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class GroupMultiplierTask implements Runnable {

    private double[][]matrix1;
    private double[][]matrix2;
    private double[][]result;

    private int startIndex;
    private int endIndex;

    public GroupMultiplierTask(double[][] matrix1, double[][] matrix2, double[][] result, int startIndex, int endIndex) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int k = startIndex; k < endIndex; k++) {
            for (int i = 0; i < matrix2[0].length; i++) {
                result[k][i] = 0;
                for (int j = 0; j < matrix1[0].length; j++) {
                    result[k][i] += matrix1[k][j] * matrix2[j][i];
                }
            }
        }
    }
}
