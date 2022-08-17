package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * OutPut Product(Only one type of product)
 */
@Entity
@Table(name = "output_product")
public class OutputProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Amount of the product
     */
    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    /**
     * Price(total number of products)
     */
    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    /**
     * Output
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "warehouse", "client" }, allowSetters = true)
    private Output output;

    /**
     * Product
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "photo", "measurement", "category" }, allowSetters = true)
    private Product product;

    /**
     * Currency of product output
     */
    @ManyToOne
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OutputProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public OutputProduct amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return this.price;
    }

    public OutputProduct price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Output getOutput() {
        return this.output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public OutputProduct output(Output output) {
        this.setOutput(output);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OutputProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public OutputProduct currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutputProduct)) {
            return false;
        }
        return id != null && id.equals(((OutputProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputProduct{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            "}";
    }
}
