package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Input(Contains Many InputProducts)
 */
@Entity
@Table(name = "input")
public class Input implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Name of the input(brief description)
     */
    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * Input date
     */
    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    /**
     * Unique code of the input
     */
    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Warehouse for input
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Warehouse warehouse;

    /**
     * Supplier(provider)
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "warehouses" }, allowSetters = true)
    private Users supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Input id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Input name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return this.date;
    }

    public Input date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getCode() {
        return this.code;
    }

    public Input code(String code) {
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

    public Input warehouse(Warehouse warehouse) {
        this.setWarehouse(warehouse);
        return this;
    }

    public Users getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Users users) {
        this.supplier = users;
    }

    public Input supplier(Users users) {
        this.setSupplier(users);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Input)) {
            return false;
        }
        return id != null && id.equals(((Input) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Input{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
