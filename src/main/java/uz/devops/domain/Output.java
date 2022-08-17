package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Output(Contains Many OutputProducts)
 */
@Entity
@Table(name = "output")
public class Output implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Name of the output(brief description)
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    /**
     * Output date
     */
    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    /**
     * Unique code of the output
     */
    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Warehouse for output
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Warehouse warehouse;

    /**
     * Client(Product recipient)
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "warehouses" }, allowSetters = true)
    private Users client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Output id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Output name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return this.date;
    }

    public Output date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getCode() {
        return this.code;
    }

    public Output code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Output warehouse(Warehouse warehouse) {
        this.setWarehouse(warehouse);
        return this;
    }

    public Users getClient() {
        return this.client;
    }

    public void setClient(Users users) {
        this.client = users;
    }

    public Output client(Users users) {
        this.setClient(users);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Output)) {
            return false;
        }
        return id != null && id.equals(((Output) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Output{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
