package me.potato.demoredis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class CacheService {

    HashMap<String, String> sampleMap = new HashMap<>();

    // from cache(if it possible)
    @Cacheable(cacheNames="mySuniCache",  key = "'me.potato.demoredis.CacheService'.concat(#key)")
    public String getThisFromCache(String key)  {
        log.info("Cacheable");
        return getThisFromDB(key);

    }

    // from repository
    public String getThisFromDB(String key){
        log.info("non cacheable");
        return sampleMap.get(key);
    }



    @CacheEvict(cacheNames="mySuniCache", key = "'me.potato.demoredis.CacheService'.concat(#key)")
    public boolean putThis(String key, String value){
        log.info("CacheEvict!");
        sampleMap.put(key,value);
        return true;
    }


}
