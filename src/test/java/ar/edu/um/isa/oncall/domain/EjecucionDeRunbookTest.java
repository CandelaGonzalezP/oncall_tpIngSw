package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.EjecucionDeRunbookTestSamples.*;
import static ar.edu.um.isa.oncall.domain.IncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.RunbookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EjecucionDeRunbookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EjecucionDeRunbook.class);
        EjecucionDeRunbook ejecucionDeRunbook1 = getEjecucionDeRunbookSample1();
        EjecucionDeRunbook ejecucionDeRunbook2 = new EjecucionDeRunbook();
        assertThat(ejecucionDeRunbook1).isNotEqualTo(ejecucionDeRunbook2);

        ejecucionDeRunbook2.setId(ejecucionDeRunbook1.getId());
        assertThat(ejecucionDeRunbook1).isEqualTo(ejecucionDeRunbook2);

        ejecucionDeRunbook2 = getEjecucionDeRunbookSample2();
        assertThat(ejecucionDeRunbook1).isNotEqualTo(ejecucionDeRunbook2);
    }

    @Test
    void runbookTest() {
        EjecucionDeRunbook ejecucionDeRunbook = getEjecucionDeRunbookRandomSampleGenerator();
        Runbook runbookBack = getRunbookRandomSampleGenerator();

        ejecucionDeRunbook.setRunbook(runbookBack);
        assertThat(ejecucionDeRunbook.getRunbook()).isEqualTo(runbookBack);

        ejecucionDeRunbook.runbook(null);
        assertThat(ejecucionDeRunbook.getRunbook()).isNull();
    }

    @Test
    void incidenteTest() {
        EjecucionDeRunbook ejecucionDeRunbook = getEjecucionDeRunbookRandomSampleGenerator();
        Incidente incidenteBack = getIncidenteRandomSampleGenerator();

        ejecucionDeRunbook.setIncidente(incidenteBack);
        assertThat(ejecucionDeRunbook.getIncidente()).isEqualTo(incidenteBack);

        ejecucionDeRunbook.incidente(null);
        assertThat(ejecucionDeRunbook.getIncidente()).isNull();
    }
}
