package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.devops.domain.InputProduct} entity.
 */
@Schema(description = "InputProduct(OnlyOne type of the product)")
public class InputProductDTO implements Serializable {

    private Long id;

    /**
     * Amount of the product
     */
    @NotNull
    @Schema(description = "Amount of the product", required = true)
    private Long amount;

    /**
     * Total price of the product input
     */
    @NotNull
    @Schema(description = "Total price of the product input", required = true)
    private Long price;

    /**
     * Expire date of the product
     */
    @Schema(description = "Expire date of the product")
    private Instant expireDate;

    private ProductDTO product;

    private InputDTO input;

    private CurrencyDTO currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public InputDTO getInput() {
        return input;
    }

    public void setInput(InputDTO input) {
        this.input = input;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InputProductDTO)) {
            return false;
        }

        InputProductDTO inputProductDTO = (InputProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inputProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputProductDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            ", expireDate='" + getExpireDate() + "'" +
            ", product=" + getProduct() +
            ", input=" + getInput() +
            ", currency=" + getCurrency() +
            "}";
    }
}
