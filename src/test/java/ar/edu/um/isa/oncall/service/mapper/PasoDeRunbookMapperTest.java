package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.PasoDeRunbookAsserts.*;
import static ar.edu.um.isa.oncall.domain.PasoDeRunbookTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasoDeRunbookMapperTest {

    private PasoDeRunbookMapper pasoDeRunbookMapper;

    @BeforeEach
    void setUp() {
        pasoDeRunbookMapper = new PasoDeRunbookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPasoDeRunbookSample1();
        var actual = pasoDeRunbookMapper.toEntity(pasoDeRunbookMapper.toDto(expected));
        assertPasoDeRunbookAllPropertiesEquals(expected, actual);
    }
}
