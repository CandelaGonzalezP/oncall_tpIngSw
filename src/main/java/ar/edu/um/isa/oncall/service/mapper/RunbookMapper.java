package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Runbook} and its DTO {@link RunbookDTO}.
 */
@Mapper(componentModel = "spring")
public interface RunbookMapper extends EntityMapper<RunbookDTO, Runbook> {
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioNombre")
    RunbookDTO toDto(Runbook s);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);
}
