package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EjecucionDeRunbookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EjecucionDeRunbookDTO.class);
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO1 = new EjecucionDeRunbookDTO();
        ejecucionDeRunbookDTO1.setId(1L);
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO2 = new EjecucionDeRunbookDTO();
        assertThat(ejecucionDeRunbookDTO1).isNotEqualTo(ejecucionDeRunbookDTO2);
        ejecucionDeRunbookDTO2.setId(ejecucionDeRunbookDTO1.getId());
        assertThat(ejecucionDeRunbookDTO1).isEqualTo(ejecucionDeRunbookDTO2);
        ejecucionDeRunbookDTO2.setId(2L);
        assertThat(ejecucionDeRunbookDTO1).isNotEqualTo(ejecucionDeRunbookDTO2);
        ejecucionDeRunbookDTO1.setId(null);
        assertThat(ejecucionDeRunbookDTO1).isNotEqualTo(ejecucionDeRunbookDTO2);
    }
}
