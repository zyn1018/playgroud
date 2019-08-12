package com.yinan.play.demo;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.yinan.play.demo.service.RetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void testRetry() {
        try {
            retryService.testOtherRetry(0.6);
        } catch (RemoteAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTemplateRetry() throws Throwable {
        SimpleRetryPolicy policy = new SimpleRetryPolicy(3, Collections.singletonMap(Exception.class, true));

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(policy);

        //normal
//        template.execute(new RetryCallback<Object, Throwable>() {
//            @Override
//            public Object doWithRetry(RetryContext retryContext) throws Throwable {
//                return retryService.testTemplateRetry(0.6);
//            }
//        }, new RecoveryCallback<Object>() {
//            @Override
//            public Object recover(RetryContext retryContext) throws Exception {
//                return retryService.recoverTemplate();
//            }
//        });

        //lambda
        template.execute(retryContext -> retryService.testTemplateRetry(0.6), retryContext -> retryService.recoverTemplate());
    }

    @Test
    public void testGuavaRetry() {
        //normal
//        Callable<Boolean> callable = new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                retryService.guavaRetry(0.6);
//                return true;
//            }
//        };

        //lambda
        Callable<Boolean> callable = () -> {
            retryService.guavaRetry(0.6);
            return true;
        };

        Retryer<Boolean> retry = RetryerBuilder.<Boolean>newBuilder().retryIfExceptionOfType(RemoteAccessException.class).withStopStrategy(StopStrategies.stopAfterAttempt(3)).build();
        try {
            retry.call(callable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            System.out.println("retry failed");
        }
    }
}
