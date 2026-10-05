package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.PasoDeRunbookTestSamples.*;
import static ar.edu.um.isa.oncall.domain.RunbookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasoDeRunbookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasoDeRunbook.class);
        PasoDeRunbook pasoDeRunbook1 = getPasoDeRunbookSample1();
        PasoDeRunbook pasoDeRunbook2 = new PasoDeRunbook();
        assertThat(pasoDeRunbook1).isNotEqualTo(pasoDeRunbook2);

        pasoDeRunbook2.setId(pasoDeRunbook1.getId());
        assertThat(pasoDeRunbook1).isEqualTo(pasoDeRunbook2);

        pasoDeRunbook2 = getPasoDeRunbookSample2();
        assertThat(pasoDeRunbook1).isNotEqualTo(pasoDeRunbook2);
    }

    @Test
    void runbookTest() {
        PasoDeRunbook pasoDeRunbook = getPasoDeRunbookRandomSampleGenerator();
        Runbook runbookBack = getRunbookRandomSampleGenerator();

        pasoDeRunbook.setRunbook(runbookBack);
        assertThat(pasoDeRunbook.getRunbook()).isEqualTo(runbookBack);

        pasoDeRunbook.runbook(null);
        assertThat(pasoDeRunbook.getRunbook()).isNull();
    }
}
