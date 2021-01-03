package com.ricehuang.journey.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.http.HttpServletResponse;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    private static JedisPool jedisPool = null;
    /**
     *
     * 方法描述 构建redis连接池
     *
     * @return
     *
     * @author yaomy
     * @date 2018年1月11日 下午4:53:07
     */
    static {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(50);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
            //小于零:阻塞不确定的时间,  默认-1
            config.setMaxWaitMillis(1000*100);
            //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
            config.setTestOnReturn(true);
            //connectionTimeout 连接超时（默认2000ms）
            //soTimeout 响应超时（默认2000ms）
            jedisPool = new JedisPool(config, "127.0.0.1", 6379,  2000);
        }
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format("Hello, %s!", name));
    }

    @GetMapping("/redisWindowLimiter")
    public void redisWindowLimitor(HttpServletResponse response) {
        if (redisWindowLimitor("ABCSoftTokenPay", 5, 10)) {
            System.out.println("达到阈值，拒绝访问");
            response.setStatus(500);
            return;
        }
        System.out.println("执行业务逻辑");
        response.setStatus(200);
        return;
    }

    @GetMapping("/redisPoolWindowLimiter")
    public void redisPoolWindowLimitor(HttpServletResponse response) {
        if (redisPoolWindowLimitor("ABCSoftTokenPayPool", 5, 10)) {
            System.out.println("达到阈值，拒绝访问");
            response.setStatus(500);
            return;
        }
        System.out.println("执行业务逻辑");
        response.setStatus(200);
        return;
    }

    public boolean redisWindowLimitor(String key, int period, int maxCount) {
        Long nowTs = System.currentTimeMillis();
        jedis.zadd(key, nowTs, "" + nowTs);
        jedis.zremrangeByScore(key, 0, nowTs - period * 1000);
        Long count = jedis.zcard(key);
        jedis.expire(key, period + 1);
        jedis.close();
        return count > maxCount;
    }

    public boolean redisPoolWindowLimitor(String key, int period, int maxCount) {
        Jedis jedisFromPool = jedisPool.getResource();
        Long nowTs = System.currentTimeMillis();
        jedisFromPool.zadd(key, nowTs, "" + nowTs);
        jedisFromPool.zremrangeByScore(key, 0, nowTs - period * 1000);
        Long count = jedisFromPool.zcard(key);
        jedisFromPool.expire(key, period + 1);
        jedisFromPool.close();
        return count > maxCount;
    }
}
