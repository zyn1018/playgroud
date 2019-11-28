/**
 * @(#)Config.java, 2019/9/27.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
@Component
public class Config {

    private static String TEST_ENV;

    @Value("${qc-test-env}")
    public static void setTestEnv(String testEnv) {
        TEST_ENV = testEnv;
    }

    public static String getTestEnv() {
        return TEST_ENV;
    }
}
