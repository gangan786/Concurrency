package org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.matrix.concurrent_1
 * @ClassName: ${TYPE_NAME}
 * @Description:将每一个元素分割成一个子任务线程
 * @Author: Gangan
 * @CreateDate: 2019/2/23 20:16
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 20:16
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class IndividualMultiplierTask implements Runnable {

    private double[][] result;
    private double[][] matrix1;
    private double[][] matrix2;

    private int row;
    private int column;

    public IndividualMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int row, int column) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
        this.column = column;
    }

    @Override
    public void run() {
        result[row][column] = 0;
        for (int i = 0; i < matrix1[row].length; i++) {
            result[row][column] += matrix1[row][i] * matrix2[i][column];
        }
    }
}
