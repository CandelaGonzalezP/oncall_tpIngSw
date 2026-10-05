package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.OrigenAlerta;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.Runbook} entity.
 */
@Schema(description = "Procedimiento paso a paso asociado a una condicion de alerta.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RunbookDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    private OrigenAlerta origenAlerta;

    private Severidad severidadMinima;

    @Size(max = 120)
    private String patronFingerprint;

    @Min(value = 1)
    @Max(value = 1440)
    private Integer tiempoMaxMinutos;

    @NotNull
    private Boolean activo;

    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OrigenAlerta getOrigenAlerta() {
        return origenAlerta;
    }

    public void setOrigenAlerta(OrigenAlerta origenAlerta) {
        this.origenAlerta = origenAlerta;
    }

    public Severidad getSeveridadMinima() {
        return severidadMinima;
    }

    public void setSeveridadMinima(Severidad severidadMinima) {
        this.severidadMinima = severidadMinima;
    }

    public String getPatronFingerprint() {
        return patronFingerprint;
    }

    public void setPatronFingerprint(String patronFingerprint) {
        this.patronFingerprint = patronFingerprint;
    }

    public Integer getTiempoMaxMinutos() {
        return tiempoMaxMinutos;
    }

    public void setTiempoMaxMinutos(Integer tiempoMaxMinutos) {
        this.tiempoMaxMinutos = tiempoMaxMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RunbookDTO)) {
            return false;
        }

        RunbookDTO runbookDTO = (RunbookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, runbookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RunbookDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", origenAlerta='" + getOrigenAlerta() + "'" +
            ", severidadMinima='" + getSeveridadMinima() + "'" +
            ", patronFingerprint='" + getPatronFingerprint() + "'" +
            ", tiempoMaxMinutos=" + getTiempoMaxMinutos() +
            ", activo='" + getActivo() + "'" +
            ", servicio=" + getServicio() +
            "}";
    }
}
