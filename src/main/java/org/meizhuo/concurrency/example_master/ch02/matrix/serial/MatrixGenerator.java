package org.meizhuo.concurrency.example_master.ch02.matrix.serial;

import java.util.Random;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02
 * @ClassName: ${TYPE_NAME}
 * @Description:  指定行列用于产生随机矩阵
 * @Author: Gangan
 * @CreateDate: 2019/2/23 15:03
 * @UpdateUser:
 * @UpdateDate: 2019/2/23 15:03
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class MatrixGenerator {

    public static double[][] generate(int rows,int columns){
        double[][] ret = new double[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                ret[i][j]=random.nextDouble()*10;
            }
        }
        return ret;
    }

}
