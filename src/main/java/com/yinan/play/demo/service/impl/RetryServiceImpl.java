/**
 * @(#)RetryServiceImpl.java, 2019-08-12.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.service.impl;

import com.yinan.play.demo.service.RetryService;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
@Service
public class RetryServiceImpl implements RetryService {
    private static final double THRESHOLD = 0.5;

    @Override
    @Retryable(value = {RemoteAccessException.class})
    public void testRetry(double param) throws RemoteAccessException {
        System.out.println("start testing retry");
        if (param > THRESHOLD) {
            System.out.println("need retry");
            throw new RemoteAccessException("retry exception");
        }
    }

    @Override
    @Retryable(value = {RemoteAccessException.class})
    public void testOtherRetry(double otherParam) throws RemoteAccessException {
        System.out.println("start testing other retry");
        if (otherParam > THRESHOLD) {
            System.out.println("need other retry");
            throw new RemoteAccessException("retry exception");
        }
    }

    @Override
    public boolean testTemplateRetry(double param) {
        System.out.println("start testing template retry");
        if (param > THRESHOLD) {
            System.out.println("need template retry");
            throw new RemoteAccessException("retry exception");
        }
        return true;
    }

    @Override
    public boolean recoverTemplate() {
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

    @Recover
    public void retry(RemoteAccessException e, double param) {
        System.out.println("using @Recover recovering: " + param);
    }

    @Recover
    public void retryOther(RemoteAccessException e, double otherParam) {
        System.out.println("using other recovering: " + otherParam);
    }
}
