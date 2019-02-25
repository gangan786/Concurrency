package org.meizhuo.concurrency.example_master.ch02.filesearch;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.filesearch
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/25 12:03
 * @UpdateUser:
 * @UpdateDate: 2019/2/25 12:03
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelGroupFileTask implements Runnable {

    private String fileName;
    private ConcurrentLinkedQueue<File> directories;
    private Result parallelResult;
    private boolean found;

    public ParallelGroupFileTask(String fileName, ConcurrentLinkedQueue<File> directories, Result parallelResult) {
        this.fileName = fileName;
        this.directories = directories;
        this.parallelResult = parallelResult;
        this.found = false;
    }

    @Override
    public void run() {
        while (directories.size() > 0) {
            File file = directories.poll();
            try {
                processDirectory(file, fileName, parallelResult);
                if (found) {
                    System.out.println(Thread.currentThread().getName() + " 此线程已发现指定文件");
                    System.out.println("并行搜索：路径 " + parallelResult.getPath());
                    return;
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 此线程被中断");
            }

        }
    }

    private void processDirectory(File file, String fileName, Result parallelResult) throws InterruptedException {
        File[] contents = file.listFiles();
        if ((contents == null) || (contents.length == 0)) {
            return;
        }

        for (File content : contents) {
            if (content.isDirectory()) {
                processDirectory(content, fileName, parallelResult);
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                if (found) {
                    return;
                }
            } else {
                processFile(content, fileName, parallelResult);
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                if (found) {
                    return;
                }
            }
        }
    }

    private void processFile(File content, String fileName, Result parallelResult) {
        if (content.getName().equals(fileName)) {
            parallelResult.setResult(true);
            parallelResult.setPath(content.getAbsolutePath());
            this.found = true;
        }
    }

    public boolean getFound() {
        return found;
    }
}
