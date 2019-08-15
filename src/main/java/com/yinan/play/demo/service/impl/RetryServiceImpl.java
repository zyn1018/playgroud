/**
 * @(#)RetryServiceImpl.java, 2019-08-12.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.service.impl;

import com.yinan.play.demo.service.RetryService;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.rmi.ServerException;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
@Service
public class RetryServiceImpl implements RetryService {

    private static final double THRESHOLD = 0.5;

    /**
     * @param param
     * @throws ServiceUnavailableException 参数大于0.5时抛出
     * @throws ServerException             参数小于等于0.5时抛出
     */
    @Override
    @Retryable(include = {ServiceUnavailableException.class}, exceptionExpression = "#{message.contains('server exception retry')}", maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public void retryByAnnotation(double param) throws ServiceUnavailableException, ServerException {
        System.out.println("start testing retry");
        if (param > THRESHOLD) {
            System.out.println("greater than threshold retry");
            throw new ServiceUnavailableException("server exception retry");
        } else {
            System.out.println("less or equal than threshold retry");
            throw new ServerException("server exception retry");
        }
    }

    @Recover
    public void annotationRecover(Exception e, double param) {
        if (e instanceof ServiceUnavailableException) {
            System.out.println("service unavailable exception recovering, message: " + e.getMessage() + ", param: " + param);
        } else if (e instanceof ServerException) {
            System.out.println("server exception recovering, message: " + e.getMessage() + ", param: " + param);
        }
    }

    @Override
    public boolean retryByTemplate(double param) {
        System.out.println("start testing template retry");
        if (param > THRESHOLD) {
            System.out.println("need template retry");
            throw new RemoteAccessException("retry exception");
        }
        return true;
    }

    @Override
    public boolean recoverByTemplate() {
        System.out.println("recovering template");
        return true;
    }

    @Override
    public void guavaRetry(double param) {
        System.out.println("start guava retry");
        if (param > THRESHOLD) {
            System.out.println("need guava retry");
            throw new RemoteAccessException("retry exception");
        }
    }
}
