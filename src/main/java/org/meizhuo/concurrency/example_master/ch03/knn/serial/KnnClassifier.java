package org.meizhuo.concurrency.example_master.ch03.knn.serial;

import java.util.*;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch03.knn.serial
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/26 14:55
 * @UpdateUser:
 * @UpdateDate: 2019/2/26 14:55
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class KnnClassifier {

    private List<? extends Sample> dataSet;

    private int k;

    public KnnClassifier(List<? extends Sample> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }

    public String classify(Sample example) {
        //计算范例和训练集所有范例之间的距离
        Distance[] distances = new Distance[dataSet.size()];
        int index=0;

        for (Sample sample : dataSet) {
            distances[index]=new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(sample, example));
            index++;
        }

        //使用Arrays.sort方法按照距离从高到低的顺序排列范式
        Arrays.sort(distances);

        //最后，在k个最邻近的范例中统计实例最多的标签
        HashMap<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag,1,(a,b) ->a+b);
        }

        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();

    }

}
