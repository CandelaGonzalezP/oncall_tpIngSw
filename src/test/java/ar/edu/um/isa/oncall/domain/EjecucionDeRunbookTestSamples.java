package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EjecucionDeRunbookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static EjecucionDeRunbook getEjecucionDeRunbookSample1() {
        return new EjecucionDeRunbook().id(1L).pasoActual(1).motivoFalla("motivoFalla1").notas("notas1");
    }

    public static EjecucionDeRunbook getEjecucionDeRunbookSample2() {
        return new EjecucionDeRunbook().id(2L).pasoActual(2).motivoFalla("motivoFalla2").notas("notas2");
    }

    public static EjecucionDeRunbook getEjecucionDeRunbookRandomSampleGenerator() {
        return new EjecucionDeRunbook()
            .id(longCount.incrementAndGet())
            .pasoActual(intCount.incrementAndGet())
            .motivoFalla(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}
