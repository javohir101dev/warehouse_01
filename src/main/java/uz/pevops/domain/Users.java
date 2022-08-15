package uz.pevops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import uz.pevops.domain.enumeration.Status;

/**
 * User(Manager of the warehouse)
 */
@Entity
@Table(name = "users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * First Name of User
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "first_name", length = 30, nullable = false)
    private String firstName;

    /**
     * Last Name of User
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "last_name", length = 30, nullable = false)
    private String lastName;

    /**
     * Phone number of User
     */
    @NotNull
    @Size(min = 9, max = 13)
    @Pattern(regexp = "[+]?[89]{3}\\d{9}|\\d{9}")
    @Column(name = "phone_number", length = 13, nullable = false, unique = true)
    private String phoneNumber;

    /**
     * Unique code of user
     */
    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(min = 6)
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    /**
     * User warehouses
     */
    @ManyToMany
    @JoinTable(
        name = "rel_users__warehouse",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "warehouse_id")
    )
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Set<Warehouse> warehouses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Users firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Users lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Users phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return this.code;
    }

    public Users code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return this.password;
    }

    public Users password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return this.status;
    }

    public Users status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Warehouse> getWarehouses() {
        return this.warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Users warehouses(Set<Warehouse> warehouses) {
        this.setWarehouses(warehouses);
        return this;
    }

    public Users addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
        warehouse.getUsers().add(this);
        return this;
    }

    public Users removeWarehouse(Warehouse warehouse) {
        this.warehouses.remove(warehouse);
        warehouse.getUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return id != null && id.equals(((Users) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", code='" + getCode() + "'" +
            ", password='" + getPassword() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
