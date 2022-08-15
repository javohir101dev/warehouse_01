package uz.pevops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * InputProduct(OnlyOne type of the product)
 */
@Entity
@Table(name = "input_product")
public class InputProduct implements Serializable {

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
     * Total price of the product input
     */
    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    /**
     * Expire date of the product
     */
    @Column(name = "expire_date")
    private Instant expireDate;

    /**
     * Product
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "photo", "measurement", "category" }, allowSetters = true)
    private Product product;

    /**
     * Input
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "warehouse", "supplier" }, allowSetters = true)
    private Input input;

    /**
     * Currency of input
     */
    @ManyToOne
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InputProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public InputProduct amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return this.price;
    }

    public InputProduct price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Instant getExpireDate() {
        return this.expireDate;
    }

    public InputProduct expireDate(Instant expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InputProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Input getInput() {
        return this.input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public InputProduct input(Input input) {
        this.setInput(input);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InputProduct currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InputProduct)) {
            return false;
        }
        return id != null && id.equals(((InputProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputProduct{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
