package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoEjecucion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.EjecucionDeRunbook} entity.
 */
@Schema(description = "Registro de una ejecucion del runbook sobre un incidente concreto.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EjecucionDeRunbookDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant iniciadaEn;

    private Instant finalizadaEn;

    @NotNull
    private EstadoEjecucion estado;

    @Min(value = 0)
    private Integer pasoActual;

    @Size(max = 255)
    private String motivoFalla;

    @Size(max = 1000)
    private String notas;

    @NotNull
    private RunbookDTO runbook;

    @NotNull
    private IncidenteDTO incidente;

    private UserDTO ejecutor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIniciadaEn() {
        return iniciadaEn;
    }

    public void setIniciadaEn(Instant iniciadaEn) {
        this.iniciadaEn = iniciadaEn;
    }

    public Instant getFinalizadaEn() {
        return finalizadaEn;
    }

    public void setFinalizadaEn(Instant finalizadaEn) {
        this.finalizadaEn = finalizadaEn;
    }

    public EstadoEjecucion getEstado() {
        return estado;
    }

    public void setEstado(EstadoEjecucion estado) {
        this.estado = estado;
    }

    public Integer getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(Integer pasoActual) {
        this.pasoActual = pasoActual;
    }

    public String getMotivoFalla() {
        return motivoFalla;
    }

    public void setMotivoFalla(String motivoFalla) {
        this.motivoFalla = motivoFalla;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public RunbookDTO getRunbook() {
        return runbook;
    }

    public void setRunbook(RunbookDTO runbook) {
        this.runbook = runbook;
    }

    public IncidenteDTO getIncidente() {
        return incidente;
    }

    public void setIncidente(IncidenteDTO incidente) {
        this.incidente = incidente;
    }

    public UserDTO getEjecutor() {
        return ejecutor;
    }

    public void setEjecutor(UserDTO ejecutor) {
        this.ejecutor = ejecutor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EjecucionDeRunbookDTO)) {
            return false;
        }

        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = (EjecucionDeRunbookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ejecucionDeRunbookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EjecucionDeRunbookDTO{" +
            "id=" + getId() +
            ", iniciadaEn='" + getIniciadaEn() + "'" +
            ", finalizadaEn='" + getFinalizadaEn() + "'" +
            ", estado='" + getEstado() + "'" +
            ", pasoActual=" + getPasoActual() +
            ", motivoFalla='" + getMotivoFalla() + "'" +
            ", notas='" + getNotas() + "'" +
            ", runbook=" + getRunbook() +
            ", incidente=" + getIncidente() +
            ", ejecutor=" + getEjecutor() +
            "}";
    }
}
