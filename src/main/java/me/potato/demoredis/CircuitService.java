package me.potato.demoredis;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;

@Component
public class CircuitService {
    final CacheService cacheService;

    public CircuitService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    // it will fallback when some exception occur(or timeout(timeoutInMilliseconds)
    // * cachable and hystrixcommand annotation highly recommended not to use in same class.
    @HystrixCommand(fallbackMethod = "getThis_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
    },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1")
            })
    public String getThis(String key){
        return cacheService.getThisFromCache(key);
    }

    // fallback for getThis Method(fallback method must be same definition as original method(in/out param))
    public String getThis_fallback(String key){
        return cacheService.getThisFromDB(key);
    }

}
