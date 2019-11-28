package com.yinan.play.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.yinan.play.demo.meta.Item;
import com.yinan.play.demo.service.RetryService;
import org.assertj.core.util.Lists;
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
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    @Test
    public void testBigDecimal() {
        BigDecimal totalPrice= new BigDecimal("10");
        BigDecimal division = totalPrice.divide(new BigDecimal("1"), 6, BigDecimal.ROUND_HALF_UP);
        System.out.println(division);
    }

    @Test
    public void testStringContains() {
        String s = "[op:getNoPassSkuProcessList] 获取sku可预约质检量失败, purchaseOrder=YC16050002-20170307-1 ,supplierId=YX0001";

        System.out.println(s.contains("[op:getNoPassSkuProcessList] 获取sku可预约质检量失败, purchaseOrder=YC"));
    }

    @Test
    public void testParseObject() {
        String s = "[op:getNoPassSkuProcessList] 获取sku可预约质检量失败, purchaseOrder=YC16050002-20170307-1 ,supplierId=YX0001";
        try {
            Map map = JSON.parseObject(s, Map.class);
            System.out.println(map);
        } catch (JSONException e) {
            System.out.println(s);
        }
    }

    @Test
    public void outputMapString() {
       Map<String, String> map = Maps.newHashMap();
       map.put("test", "http://127.0.0.1:8550/proxy/test.yanxuan-qc-indicator-core.service.mailsaas");
       map.put("test2", "http://127.0.0.1:8550/proxy/test2.yanxuan-qc-indicator-core.service.mailsaas");

        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void instance() {
        List<Integer> list = null;
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }

    @Test
    public void collectEmptyListToMap() {

        List<Item> result = Lists.newArrayList();

        Map<Long, Item> itemMap = result.stream().collect(Collectors.toMap(Item::getItemId, i -> i));

        System.out.println(JSON.toJSONString(itemMap));
    }

    @Test
    public void boxing() {

        Boolean check = true;
        System.out.println(check);
        changeBoolean(check);
        System.out.println(check);
    }

    private void changeBoolean(Boolean check) {
        check = false;
    }

    @Test
    public void testRetainList() {
        List<Long> longList = Lists.newArrayList(1L,2L,3L);
        List<Long> longSecondList = Lists.newArrayList();
        longList.retainAll(longSecondList);
        System.out.println(longList);

    }
}
