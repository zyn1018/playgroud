/**
 * @(#)Service.java, 2019-08-12.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.service;

import javax.naming.ServiceUnavailableException;
import java.rmi.ServerException;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
public interface RetryService {

    /**
     * 注解形式的Spring-retry
     *
     * @param param
     * @throws ServiceUnavailableException > 0.5
     * @throws ServerException             <= 0.5
     */
    void retryByAnnotation(double param) throws ServiceUnavailableException, ServerException;

    /**
     * Spring-retry template
     *
     * @param param
     * @return
     */
    boolean retryByTemplate(double param);

    /**
     * spring retry template的recover方法
     *
     * @return
     */
    boolean recoverByTemplate();

    /**
     * guava-retry
     *
     * @param param
     */
    void guavaRetry(double param);
}
