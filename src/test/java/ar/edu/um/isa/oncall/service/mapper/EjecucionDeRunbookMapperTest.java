package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.EjecucionDeRunbookAsserts.*;
import static ar.edu.um.isa.oncall.domain.EjecucionDeRunbookTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EjecucionDeRunbookMapperTest {

    private EjecucionDeRunbookMapper ejecucionDeRunbookMapper;

    @BeforeEach
    void setUp() {
        ejecucionDeRunbookMapper = new EjecucionDeRunbookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEjecucionDeRunbookSample1();
        var actual = ejecucionDeRunbookMapper.toEntity(ejecucionDeRunbookMapper.toDto(expected));
        assertEjecucionDeRunbookAllPropertiesEquals(expected, actual);
    }
}
