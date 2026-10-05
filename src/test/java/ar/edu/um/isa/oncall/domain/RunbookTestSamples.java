package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RunbookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Runbook getRunbookSample1() {
        return new Runbook()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .patronFingerprint("patronFingerprint1")
            .tiempoMaxMinutos(1);
    }

    public static Runbook getRunbookSample2() {
        return new Runbook()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .patronFingerprint("patronFingerprint2")
            .tiempoMaxMinutos(2);
    }

    public static Runbook getRunbookRandomSampleGenerator() {
        return new Runbook()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .patronFingerprint(UUID.randomUUID().toString())
            .tiempoMaxMinutos(intCount.incrementAndGet());
    }
}
