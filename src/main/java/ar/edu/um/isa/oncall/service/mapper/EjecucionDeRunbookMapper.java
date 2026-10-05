package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.EjecucionDeRunbook;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.service.dto.EjecucionDeRunbookDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EjecucionDeRunbook} and its DTO {@link EjecucionDeRunbookDTO}.
 */
@Mapper(componentModel = "spring")
public interface EjecucionDeRunbookMapper extends EntityMapper<EjecucionDeRunbookDTO, EjecucionDeRunbook> {
    @Mapping(target = "runbook", source = "runbook", qualifiedByName = "runbookNombre")
    @Mapping(target = "incidente", source = "incidente", qualifiedByName = "incidenteTitulo")
    @Mapping(target = "ejecutor", source = "ejecutor", qualifiedByName = "userLogin")
    EjecucionDeRunbookDTO toDto(EjecucionDeRunbook s);

    @Named("runbookNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    RunbookDTO toDtoRunbookNombre(Runbook runbook);

    @Named("incidenteTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    IncidenteDTO toDtoIncidenteTitulo(Incidente incidente);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
