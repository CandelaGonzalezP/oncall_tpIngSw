package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Un paso del runbook. El orden importa.
 */
@Entity
@Table(name = "paso_de_runbook")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasoDeRunbook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @NotNull
    @Size(max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Size(max = 1000)
    @Column(name = "instrucciones", length = 1000, nullable = false)
    private String instrucciones;

    @NotNull
    @Column(name = "obligatorio", nullable = false)
    private Boolean obligatorio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicio", "pasos", "ejecucions" }, allowSetters = true)
    private Runbook runbook;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PasoDeRunbook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public PasoDeRunbook orden(Integer orden) {
        this.setOrden(orden);
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public PasoDeRunbook titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInstrucciones() {
        return this.instrucciones;
    }

    public PasoDeRunbook instrucciones(String instrucciones) {
        this.setInstrucciones(instrucciones);
        return this;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Boolean getObligatorio() {
        return this.obligatorio;
    }

    public PasoDeRunbook obligatorio(Boolean obligatorio) {
        this.setObligatorio(obligatorio);
        return this;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Runbook getRunbook() {
        return this.runbook;
    }

    public void setRunbook(Runbook runbook) {
        this.runbook = runbook;
    }

    public PasoDeRunbook runbook(Runbook runbook) {
        this.setRunbook(runbook);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasoDeRunbook)) {
            return false;
        }
        return getId() != null && getId().equals(((PasoDeRunbook) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasoDeRunbook{" +
            "id=" + getId() +
            ", orden=" + getOrden() +
            ", titulo='" + getTitulo() + "'" +
            ", instrucciones='" + getInstrucciones() + "'" +
            ", obligatorio='" + getObligatorio() + "'" +
            "}";
    }
}
