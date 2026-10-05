package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.RunbookAsserts.*;
import static ar.edu.um.isa.oncall.domain.RunbookTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RunbookMapperTest {

    private RunbookMapper runbookMapper;

    @BeforeEach
    void setUp() {
        runbookMapper = new RunbookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRunbookSample1();
        var actual = runbookMapper.toEntity(runbookMapper.toDto(expected));
        assertRunbookAllPropertiesEquals(expected, actual);
    }
}
