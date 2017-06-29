package com.dzr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author dingzr
 * @Description
 * @ClassName SomeSuite
 * @since 2017/6/29 15:23
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({UserTest.class, CityTest.class})
public class SomeSuite {
    // 类中不需要编写代码
}
