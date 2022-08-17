package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import uz.devops.domain.enumeration.Status;

/**
 * A DTO for the {@link uz.devops.domain.Users} entity.
 */
@Schema(description = "User(Manager of the warehouse)")
public class UsersDTO implements Serializable {

    private Long id;

    /**
     * First Name of User
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "First Name of User", required = true)
    private String firstName;

    /**
     * Last Name of User
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "Last Name of User", required = true)
    private String lastName;

    /**
     * Phone number of User
     */
    @NotNull
    @Size(min = 9, max = 13)
    @Pattern(regexp = "[+]?[89]{3}\\d{9}|\\d{9}")
    @Schema(description = "Phone number of User", required = true)
    private String phoneNumber;

    /**
     * Unique code of user
     */
    @NotNull
    @Schema(description = "Unique code of user", required = true)
    private String code;

    @NotNull
    @Size(min = 6)
    private String password;

    private Status status;

    private Set<WarehouseDTO> warehouses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<WarehouseDTO> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Set<WarehouseDTO> warehouses) {
        this.warehouses = warehouses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsersDTO)) {
            return false;
        }

        UsersDTO usersDTO = (UsersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", code='" + getCode() + "'" +
            ", password='" + getPassword() + "'" +
            ", status='" + getStatus() + "'" +
            ", warehouses=" + getWarehouses() +
            "}";
    }
}
