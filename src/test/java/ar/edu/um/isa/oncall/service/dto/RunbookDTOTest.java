package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RunbookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RunbookDTO.class);
        RunbookDTO runbookDTO1 = new RunbookDTO();
        runbookDTO1.setId(1L);
        RunbookDTO runbookDTO2 = new RunbookDTO();
        assertThat(runbookDTO1).isNotEqualTo(runbookDTO2);
        runbookDTO2.setId(runbookDTO1.getId());
        assertThat(runbookDTO1).isEqualTo(runbookDTO2);
        runbookDTO2.setId(2L);
        assertThat(runbookDTO1).isNotEqualTo(runbookDTO2);
        runbookDTO1.setId(null);
        assertThat(runbookDTO1).isNotEqualTo(runbookDTO2);
    }
}
