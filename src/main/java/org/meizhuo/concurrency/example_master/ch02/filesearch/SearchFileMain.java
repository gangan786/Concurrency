package org.meizhuo.concurrency.example_master.ch02.filesearch;

import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.filesearch
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/25 14:13
 * @UpdateUser:
 * @UpdateDate: 2019/2/25 14:13
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class SearchFileMain {

    @Test
    public void serialTest(){
        File file = new File("C:\\Windows\\");
        String regex = "hosts";
        Date start, end;

        Result result = new Result();
        start = new Date();
        SerialFileSearch.searchFile(file, regex, result);
        end = new Date();


        System.out.printf("Serial Search: Execution Time: %d%n", end.getTime() - start.getTime());

        /**
         * 输出结果为：
         * 串行搜索：路径 C:\Windows\System32\drivers\etc\hosts
         * Serial Search: Execution Time: 24315
         */
    }

    @Test
    public void parallelTest(){
        File file = new File("C:\\Windows\\");
        String regex = "hosts";
        Date start, end;
        Result parallelResult = new Result();
        start = new Date();
        ParallelGroupFileSearch.searchFile(file, regex, parallelResult);
        end = new Date();
        System.out.printf("Parallel Group Search: Execution Time: %d%n", end.getTime() - start.getTime());

        /**
         * 输出结果：
         * Thread-0 此线程已发现指定文件
         * 并行搜索：路径 C:\Windows\System32\drivers\etc\hosts
         * Parallel Group Search: Execution Time: 12538
         * Thread-3 此线程被中断
         * Thread-1 此线程被中断
         * Thread-2 此线程被中断
         * Thread-1 此线程被中断
         * Thread-2 此线程被中断
         * Thread-3 此线程被中断
         * Thread-2 此线程被中断
         * Thread-1 此线程被中断
         * Thread-3 此线程被中断
         * Thread-3 此线程被中断
         * Thread-2 此线程被中断
         */
    }

}
