package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PasoDeRunbookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static PasoDeRunbook getPasoDeRunbookSample1() {
        return new PasoDeRunbook().id(1L).orden(1).titulo("titulo1").instrucciones("instrucciones1");
    }

    public static PasoDeRunbook getPasoDeRunbookSample2() {
        return new PasoDeRunbook().id(2L).orden(2).titulo("titulo2").instrucciones("instrucciones2");
    }

    public static PasoDeRunbook getPasoDeRunbookRandomSampleGenerator() {
        return new PasoDeRunbook()
            .id(longCount.incrementAndGet())
            .orden(intCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .instrucciones(UUID.randomUUID().toString());
    }
}
