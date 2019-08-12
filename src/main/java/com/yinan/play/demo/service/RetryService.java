/**
 * @(#)Service.java, 2019-08-12.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.service;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
public interface RetryService {

    /**
     * 测试Spring-retry
     *
     * @param param
     */
    void testRetry(double param);

    /**
     * 测试Spring-retry
     *
     * @param param
     */
    void testOtherRetry(double param);

    /**
     * 测试Spring-retry
     *
     * @param param
     */
    boolean testTemplateRetry(double param);

    /**
     * 恢复template
     * @return
     */
    boolean recoverTemplate();
}
