package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.EjecucionDeRunbookTestSamples.*;
import static ar.edu.um.isa.oncall.domain.PasoDeRunbookTestSamples.*;
import static ar.edu.um.isa.oncall.domain.RunbookTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RunbookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Runbook.class);
        Runbook runbook1 = getRunbookSample1();
        Runbook runbook2 = new Runbook();
        assertThat(runbook1).isNotEqualTo(runbook2);

        runbook2.setId(runbook1.getId());
        assertThat(runbook1).isEqualTo(runbook2);

        runbook2 = getRunbookSample2();
        assertThat(runbook1).isNotEqualTo(runbook2);
    }

    @Test
    void servicioTest() {
        Runbook runbook = getRunbookRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        runbook.setServicio(servicioBack);
        assertThat(runbook.getServicio()).isEqualTo(servicioBack);

        runbook.servicio(null);
        assertThat(runbook.getServicio()).isNull();
    }

    @Test
    void pasoTest() {
        Runbook runbook = getRunbookRandomSampleGenerator();
        PasoDeRunbook pasoDeRunbookBack = getPasoDeRunbookRandomSampleGenerator();

        runbook.addPaso(pasoDeRunbookBack);
        assertThat(runbook.getPasos()).containsOnly(pasoDeRunbookBack);
        assertThat(pasoDeRunbookBack.getRunbook()).isEqualTo(runbook);

        runbook.removePaso(pasoDeRunbookBack);
        assertThat(runbook.getPasos()).doesNotContain(pasoDeRunbookBack);
        assertThat(pasoDeRunbookBack.getRunbook()).isNull();

        runbook.pasos(new HashSet<>(Set.of(pasoDeRunbookBack)));
        assertThat(runbook.getPasos()).containsOnly(pasoDeRunbookBack);
        assertThat(pasoDeRunbookBack.getRunbook()).isEqualTo(runbook);

        runbook.setPasos(new HashSet<>());
        assertThat(runbook.getPasos()).doesNotContain(pasoDeRunbookBack);
        assertThat(pasoDeRunbookBack.getRunbook()).isNull();
    }

    @Test
    void ejecucionTest() {
        Runbook runbook = getRunbookRandomSampleGenerator();
        EjecucionDeRunbook ejecucionDeRunbookBack = getEjecucionDeRunbookRandomSampleGenerator();

        runbook.addEjecucion(ejecucionDeRunbookBack);
        assertThat(runbook.getEjecucions()).containsOnly(ejecucionDeRunbookBack);
        assertThat(ejecucionDeRunbookBack.getRunbook()).isEqualTo(runbook);

        runbook.removeEjecucion(ejecucionDeRunbookBack);
        assertThat(runbook.getEjecucions()).doesNotContain(ejecucionDeRunbookBack);
        assertThat(ejecucionDeRunbookBack.getRunbook()).isNull();

        runbook.ejecucions(new HashSet<>(Set.of(ejecucionDeRunbookBack)));
        assertThat(runbook.getEjecucions()).containsOnly(ejecucionDeRunbookBack);
        assertThat(ejecucionDeRunbookBack.getRunbook()).isEqualTo(runbook);

        runbook.setEjecucions(new HashSet<>());
        assertThat(runbook.getEjecucions()).doesNotContain(ejecucionDeRunbookBack);
        assertThat(ejecucionDeRunbookBack.getRunbook()).isNull();
    }
}
