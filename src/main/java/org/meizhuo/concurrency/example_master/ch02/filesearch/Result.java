package org.meizhuo.concurrency.example_master.ch02.filesearch;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.filesearch
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/25 11:45
 * @UpdateUser:
 * @UpdateDate: 2019/2/25 11:45
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class Result {

    private boolean result;

    private String path;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
