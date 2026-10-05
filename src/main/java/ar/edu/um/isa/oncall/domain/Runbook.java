package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.OrigenAlerta;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Procedimiento paso a paso asociado a una condicion de alerta.
 */
@Entity
@Table(name = "runbook")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Runbook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen_alerta")
    private OrigenAlerta origenAlerta;

    @Enumerated(EnumType.STRING)
    @Column(name = "severidad_minima")
    private Severidad severidadMinima;

    @Size(max = 120)
    @Column(name = "patron_fingerprint", length = 120)
    private String patronFingerprint;

    @Min(value = 1)
    @Max(value = 1440)
    @Column(name = "tiempo_max_minutos")
    private Integer tiempoMaxMinutos;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "runbooks", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "runbook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "runbook" }, allowSetters = true)
    private Set<PasoDeRunbook> pasos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "runbook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "runbook", "incidente", "ejecutor" }, allowSetters = true)
    private Set<EjecucionDeRunbook> ejecucions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Runbook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Runbook nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Runbook descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OrigenAlerta getOrigenAlerta() {
        return this.origenAlerta;
    }

    public Runbook origenAlerta(OrigenAlerta origenAlerta) {
        this.setOrigenAlerta(origenAlerta);
        return this;
    }

    public void setOrigenAlerta(OrigenAlerta origenAlerta) {
        this.origenAlerta = origenAlerta;
    }

    public Severidad getSeveridadMinima() {
        return this.severidadMinima;
    }

    public Runbook severidadMinima(Severidad severidadMinima) {
        this.setSeveridadMinima(severidadMinima);
        return this;
    }

    public void setSeveridadMinima(Severidad severidadMinima) {
        this.severidadMinima = severidadMinima;
    }

    public String getPatronFingerprint() {
        return this.patronFingerprint;
    }

    public Runbook patronFingerprint(String patronFingerprint) {
        this.setPatronFingerprint(patronFingerprint);
        return this;
    }

    public void setPatronFingerprint(String patronFingerprint) {
        this.patronFingerprint = patronFingerprint;
    }

    public Integer getTiempoMaxMinutos() {
        return this.tiempoMaxMinutos;
    }

    public Runbook tiempoMaxMinutos(Integer tiempoMaxMinutos) {
        this.setTiempoMaxMinutos(tiempoMaxMinutos);
        return this;
    }

    public void setTiempoMaxMinutos(Integer tiempoMaxMinutos) {
        this.tiempoMaxMinutos = tiempoMaxMinutos;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Runbook activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Runbook servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    public Set<PasoDeRunbook> getPasos() {
        return this.pasos;
    }

    public void setPasos(Set<PasoDeRunbook> pasoDeRunbooks) {
        if (this.pasos != null) {
            this.pasos.forEach(i -> i.setRunbook(null));
        }
        if (pasoDeRunbooks != null) {
            pasoDeRunbooks.forEach(i -> i.setRunbook(this));
        }
        this.pasos = pasoDeRunbooks;
    }

    public Runbook pasos(Set<PasoDeRunbook> pasoDeRunbooks) {
        this.setPasos(pasoDeRunbooks);
        return this;
    }

    public Runbook addPaso(PasoDeRunbook pasoDeRunbook) {
        this.pasos.add(pasoDeRunbook);
        pasoDeRunbook.setRunbook(this);
        return this;
    }

    public Runbook removePaso(PasoDeRunbook pasoDeRunbook) {
        this.pasos.remove(pasoDeRunbook);
        pasoDeRunbook.setRunbook(null);
        return this;
    }

    public Set<EjecucionDeRunbook> getEjecucions() {
        return this.ejecucions;
    }

    public void setEjecucions(Set<EjecucionDeRunbook> ejecucionDeRunbooks) {
        if (this.ejecucions != null) {
            this.ejecucions.forEach(i -> i.setRunbook(null));
        }
        if (ejecucionDeRunbooks != null) {
            ejecucionDeRunbooks.forEach(i -> i.setRunbook(this));
        }
        this.ejecucions = ejecucionDeRunbooks;
    }

    public Runbook ejecucions(Set<EjecucionDeRunbook> ejecucionDeRunbooks) {
        this.setEjecucions(ejecucionDeRunbooks);
        return this;
    }

    public Runbook addEjecucion(EjecucionDeRunbook ejecucionDeRunbook) {
        this.ejecucions.add(ejecucionDeRunbook);
        ejecucionDeRunbook.setRunbook(this);
        return this;
    }

    public Runbook removeEjecucion(EjecucionDeRunbook ejecucionDeRunbook) {
        this.ejecucions.remove(ejecucionDeRunbook);
        ejecucionDeRunbook.setRunbook(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Runbook)) {
            return false;
        }
        return getId() != null && getId().equals(((Runbook) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Runbook{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", origenAlerta='" + getOrigenAlerta() + "'" +
            ", severidadMinima='" + getSeveridadMinima() + "'" +
            ", patronFingerprint='" + getPatronFingerprint() + "'" +
            ", tiempoMaxMinutos=" + getTiempoMaxMinutos() +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
