package org.meizhuo.concurrency.example_master.ch02.filesearch;

import java.io.File;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.filesearch
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/25 11:47
 * @UpdateUser:
 * @UpdateDate: 2019/2/25 11:47
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class SerialFileSearch {

    public static void searchFile(File file, String name, Result result) {
        File[] contents;
        contents=file.listFiles();
        if (contents == null || contents.length == 0) {
            return;
        }
        for (File content : contents) {
            if (content.isDirectory()) {
                searchFile(content, name, result);
            } else {
                if (content.getName().equals(name)) {
                    result.setPath(content.getAbsolutePath());
                    result.setResult(true);
                    System.out.println("串行搜索：路径 " + content.getAbsolutePath());
                    return;
                }
            }
            if (result.isResult()) {
                return;
            }
        }
    }

}
