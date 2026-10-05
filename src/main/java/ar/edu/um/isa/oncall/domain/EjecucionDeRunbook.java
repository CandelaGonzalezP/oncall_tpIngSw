package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoEjecucion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Registro de una ejecucion del runbook sobre un incidente concreto.
 */
@Entity
@Table(name = "ejecucion_de_runbook")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EjecucionDeRunbook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "iniciada_en", nullable = false)
    private Instant iniciadaEn;

    @Column(name = "finalizada_en")
    private Instant finalizadaEn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEjecucion estado;

    @Min(value = 0)
    @Column(name = "paso_actual")
    private Integer pasoActual;

    @Size(max = 255)
    @Column(name = "motivo_falla", length = 255)
    private String motivoFalla;

    @Size(max = 1000)
    @Column(name = "notas", length = 1000)
    private String notas;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicio", "pasos", "ejecucions" }, allowSetters = true)
    private Runbook runbook;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "comandante", "servicios", "postmortem", "alertas", "eventos", "notificacions", "ejecucions" },
        allowSetters = true
    )
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY)
    private User ejecutor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EjecucionDeRunbook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIniciadaEn() {
        return this.iniciadaEn;
    }

    public EjecucionDeRunbook iniciadaEn(Instant iniciadaEn) {
        this.setIniciadaEn(iniciadaEn);
        return this;
    }

    public void setIniciadaEn(Instant iniciadaEn) {
        this.iniciadaEn = iniciadaEn;
    }

    public Instant getFinalizadaEn() {
        return this.finalizadaEn;
    }

    public EjecucionDeRunbook finalizadaEn(Instant finalizadaEn) {
        this.setFinalizadaEn(finalizadaEn);
        return this;
    }

    public void setFinalizadaEn(Instant finalizadaEn) {
        this.finalizadaEn = finalizadaEn;
    }

    public EstadoEjecucion getEstado() {
        return this.estado;
    }

    public EjecucionDeRunbook estado(EstadoEjecucion estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoEjecucion estado) {
        this.estado = estado;
    }

    public Integer getPasoActual() {
        return this.pasoActual;
    }

    public EjecucionDeRunbook pasoActual(Integer pasoActual) {
        this.setPasoActual(pasoActual);
        return this;
    }

    public void setPasoActual(Integer pasoActual) {
        this.pasoActual = pasoActual;
    }

    public String getMotivoFalla() {
        return this.motivoFalla;
    }

    public EjecucionDeRunbook motivoFalla(String motivoFalla) {
        this.setMotivoFalla(motivoFalla);
        return this;
    }

    public void setMotivoFalla(String motivoFalla) {
        this.motivoFalla = motivoFalla;
    }

    public String getNotas() {
        return this.notas;
    }

    public EjecucionDeRunbook notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Runbook getRunbook() {
        return this.runbook;
    }

    public void setRunbook(Runbook runbook) {
        this.runbook = runbook;
    }

    public EjecucionDeRunbook runbook(Runbook runbook) {
        this.setRunbook(runbook);
        return this;
    }

    public Incidente getIncidente() {
        return this.incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public EjecucionDeRunbook incidente(Incidente incidente) {
        this.setIncidente(incidente);
        return this;
    }

    public User getEjecutor() {
        return this.ejecutor;
    }

    public void setEjecutor(User user) {
        this.ejecutor = user;
    }

    public EjecucionDeRunbook ejecutor(User user) {
        this.setEjecutor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EjecucionDeRunbook)) {
            return false;
        }
        return getId() != null && getId().equals(((EjecucionDeRunbook) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EjecucionDeRunbook{" +
            "id=" + getId() +
            ", iniciadaEn='" + getIniciadaEn() + "'" +
            ", finalizadaEn='" + getFinalizadaEn() + "'" +
            ", estado='" + getEstado() + "'" +
            ", pasoActual=" + getPasoActual() +
            ", motivoFalla='" + getMotivoFalla() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
