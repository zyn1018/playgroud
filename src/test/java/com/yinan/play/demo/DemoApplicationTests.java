package com.yinan.play.demo;

import com.yinan.play.demo.service.SpringRetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private SpringRetryService springRetryService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testRetry() {
        try {
            springRetryService.testOtherRetry(0.6);
        } catch (RemoteAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTemplateRetry() throws Throwable {
        SimpleRetryPolicy policy = new SimpleRetryPolicy(3, Collections.singletonMap(Exception.class, true));

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(policy);

        template.execute(new RetryCallback<Object, Throwable>() {
            @Override
            public Object doWithRetry(RetryContext retryContext) throws Throwable {
                return springRetryService.testTemplateRetry(0.6);
            }
        }, new RecoveryCallback<Object>() {
            @Override
            public Object recover(RetryContext retryContext) throws Exception {
                return springRetryService.recoverTemplate();
            }
        });

        template.execute((RetryCallback<Object, Throwable>) retryContext -> springRetryService.testTemplateRetry(0.6), retryContext -> springRetryService.recoverTemplate());
    }
}
