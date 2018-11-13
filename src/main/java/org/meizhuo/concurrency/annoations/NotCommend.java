package org.meizhuo.concurrency.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.annoations
 * @ClassName: ${TYPE_NAME}
 * @Description: 标记【不推荐】的写法
 * @Author: Gangan
 * @CreateDate: 2018/11/13 20:56
 * @UpdateUser:
 * @UpdateDate: 2018/11/13 20:56
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2018</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface NotCommend {

    String value() default "";

}
