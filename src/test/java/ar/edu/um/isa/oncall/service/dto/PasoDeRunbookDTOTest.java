package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasoDeRunbookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasoDeRunbookDTO.class);
        PasoDeRunbookDTO pasoDeRunbookDTO1 = new PasoDeRunbookDTO();
        pasoDeRunbookDTO1.setId(1L);
        PasoDeRunbookDTO pasoDeRunbookDTO2 = new PasoDeRunbookDTO();
        assertThat(pasoDeRunbookDTO1).isNotEqualTo(pasoDeRunbookDTO2);
        pasoDeRunbookDTO2.setId(pasoDeRunbookDTO1.getId());
        assertThat(pasoDeRunbookDTO1).isEqualTo(pasoDeRunbookDTO2);
        pasoDeRunbookDTO2.setId(2L);
        assertThat(pasoDeRunbookDTO1).isNotEqualTo(pasoDeRunbookDTO2);
        pasoDeRunbookDTO1.setId(null);
        assertThat(pasoDeRunbookDTO1).isNotEqualTo(pasoDeRunbookDTO2);
    }
}
