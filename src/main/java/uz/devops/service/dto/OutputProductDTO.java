package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.devops.domain.OutputProduct} entity.
 */
@Schema(description = "OutPut Product(Only one type of product)")
public class OutputProductDTO implements Serializable {

    private Long id;

    /**
     * Amount of the product
     */
    @NotNull
    @Schema(description = "Amount of the product", required = true)
    private Long amount;

    /**
     * Price(total number of products)
     */
    @NotNull
    @Schema(description = "Price(total number of products)", required = true)
    private Long price;

    private OutputDTO output;

    private ProductDTO product;

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

    public OutputDTO getOutput() {
        return output;
    }

    public void setOutput(OutputDTO output) {
        this.output = output;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
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
        if (!(o instanceof OutputProductDTO)) {
            return false;
        }

        OutputProductDTO outputProductDTO = (OutputProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outputProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputProductDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            ", output=" + getOutput() +
            ", product=" + getProduct() +
            ", currency=" + getCurrency() +
            "}";
    }
}
