package me.potato.demoredis.config;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;



@Slf4j
@Configuration
public class CacheConfig extends CachingConfigurerSupport {
    private final Environment env;
    public CacheConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        LettuceClientConfiguration clientConf = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)    // read from replication is first
                .build();

        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(env.getProperty("spring.redis.host"));
        redisConf.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));

        return new LettuceConnectionFactory(redisConf, clientConf);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(Integer.parseInt(env.getProperty("spring.cache.redis.time-to-live"))))
                .disableCachingNullValues();
        return cacheConfig;
    }
}