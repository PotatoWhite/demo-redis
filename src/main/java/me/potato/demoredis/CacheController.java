package me.potato.demoredis;


import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class CacheController {

    final CacheService cacheService;
    final CircuitService circuitService;

    public CacheController(CacheService cacheService, CircuitService circuitService) {
        this.cacheService = cacheService;
        this.circuitService = circuitService;
    }


    @GetMapping("/get")
    public ResponseEntity getSample(String key) {
        return ResponseEntity.status(HttpStatus.OK).body(circuitService.getThis(key));
    }



    @GetMapping("/put")
    public ResponseEntity putSample(String key, String value) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cacheService.putThis(key, value));
        }catch (RedisConnectionException ex){
            // 처리는 완료되었지만 Cache 적재 실패 한 내용
            // 따라서 DB 상에 반영되었을 것으로 예상됨
            return ResponseEntity.ok().build();
        }

    }


}
