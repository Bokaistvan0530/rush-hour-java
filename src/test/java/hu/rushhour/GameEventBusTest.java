package hu.rushhour;

import hu.rushhour.event.GameEvent;
import hu.rushhour.event.GameEventBus;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameEventBusTest {
    @Test
    void broadcastsToListeners() {
        GameEventBus bus = new GameEventBus();
        AtomicInteger count = new AtomicInteger();
        bus.subscribe(e -> {
            assertEquals("move", e.type());
            assertEquals("A 1", e.message());
            count.incrementAndGet();
        });
        bus.subscribe(e -> count.incrementAndGet());
        bus.publish(new GameEvent("move", "A 1"));
        assertEquals(2, count.get());
    }
}
