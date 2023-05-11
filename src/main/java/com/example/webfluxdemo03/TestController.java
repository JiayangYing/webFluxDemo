package com.example.webfluxdemo03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class TestController {


    private String createStr() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return "something";
    }

    @GetMapping("/1")
    private String get1() throws InterruptedException {
        log.info("get1 start");
        String result = createStr();
        log.info("get1 end");

        return result;
    }

    /**
     * webflux return a mono or flux
     * spring5 is based on the reactive framework
     * return the mono(stream) and we do not call stream final operation(惰性求值)
     * so it will not block the thread
     * @return
     */

    @GetMapping("/2")
    private Mono<String> get2(){

        log.info("get2 start");
        Mono<String> result = Mono.fromSupplier(()-> {
            try {
                return createStr();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        log.info("get2 end");
        return result;
    }

    /**
     * using jdk8 stream IntStream.range(1,5).mapToObj.....
     * then use Flux.fromStream to get the stream....
     * need to point produces = "text/event-stream" to tell the return type
     * @return
     */
    @GetMapping(value = "/3", produces = "text/event-stream")
    private Flux<String> flux(){
        Flux<String> result = Flux.fromStream(IntStream.range(1,5).mapToObj(i ->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "flux data--" +i;
        }));
        return result;
    }
}
