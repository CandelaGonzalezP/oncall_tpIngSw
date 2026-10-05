package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.PasoDeRunbook;
import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.service.dto.PasoDeRunbookDTO;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PasoDeRunbook} and its DTO {@link PasoDeRunbookDTO}.
 */
@Mapper(componentModel = "spring")
public interface PasoDeRunbookMapper extends EntityMapper<PasoDeRunbookDTO, PasoDeRunbook> {
    @Mapping(target = "runbook", source = "runbook", qualifiedByName = "runbookNombre")
    PasoDeRunbookDTO toDto(PasoDeRunbook s);

    @Named("runbookNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    RunbookDTO toDtoRunbookNombre(Runbook runbook);
}
