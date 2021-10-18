package com.lf.srb.core;

import org.junit.Test;
import org.springframework.util.Assert;


/**以优雅的 Assert(断言) 方式来校验业务的异常情况，消除 if else
 * @author lf
 * @creat 2021-09-10 15:20
 */
public class AssertTests {
    //if else的用法
    @Test
    public void test1() {
        Object o = null;
        if (o == null) {
            throw new IllegalArgumentException("用户不存在.");
        }
    }

    //断言的用法：更为简洁
    @Test
    public void test2() {
        // 另一种写法
        Object o = null;
        Assert.notNull(o, "用户不存在.");
    }
}
