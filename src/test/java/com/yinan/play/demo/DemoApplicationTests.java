package com.yinan.play.demo;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicates;
import com.yinan.play.demo.service.RetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.ServiceUnavailableException;
import java.rmi.ServerException;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private RetryService retryService;

    @Test
    public void contextLoads() {
    }

    /**
     * 测试注解形式的spring-retry
     */
    @Test
    public void testRetry() {
        try {
            retryService.retryByAnnotation(0.6);
        } catch (RemoteAccessException | ServiceUnavailableException | ServerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试spring retry template
     *
     * @throws Throwable
     */
    @Test
    public void testTemplateRetry() throws Throwable {
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, Collections.singletonMap(Exception.class, true));

        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(3000L);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(simpleRetryPolicy);

        template.execute(retryContext -> retryService.retryByTemplate(0.6), retryContext -> retryService.recoverByTemplate());
    }

    /**
     * 测试guava retry
     */
    @Test
    public void testGuavaRetry() {
        //lambda
        Callable<Boolean> callable = () -> {
            retryService.guavaRetry(0.6);
            return true;
        };

        Retryer<Boolean> retry = RetryerBuilder.<Boolean>newBuilder()
                .retryIfResult(Predicates.isNull())
                .retryIfExceptionOfType(RemoteAccessException.class)
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();

        try {
            retry.call(callable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            System.out.println("retry failed");
        }
    }
}
