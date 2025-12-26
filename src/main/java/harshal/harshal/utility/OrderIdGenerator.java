package harshal.harshal.utility;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderIdGenerator {
    private final AtomicLong counter = new AtomicLong(1);

    public Long nextId(){
        return counter.getAndIncrement();
    }
}
