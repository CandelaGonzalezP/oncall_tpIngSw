package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.PasoDeRunbook} entity.
 */
@Schema(description = "Un paso del runbook. El orden importa.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasoDeRunbookDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    private Integer orden;

    @NotNull
    @Size(max = 100)
    private String titulo;

    @NotNull
    @Size(max = 1000)
    private String instrucciones;

    @NotNull
    private Boolean obligatorio;

    @NotNull
    private RunbookDTO runbook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public RunbookDTO getRunbook() {
        return runbook;
    }

    public void setRunbook(RunbookDTO runbook) {
        this.runbook = runbook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasoDeRunbookDTO)) {
            return false;
        }

        PasoDeRunbookDTO pasoDeRunbookDTO = (PasoDeRunbookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pasoDeRunbookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasoDeRunbookDTO{" +
            "id=" + getId() +
            ", orden=" + getOrden() +
            ", titulo='" + getTitulo() + "'" +
            ", instrucciones='" + getInstrucciones() + "'" +
            ", obligatorio='" + getObligatorio() + "'" +
            ", runbook=" + getRunbook() +
            "}";
    }
}
