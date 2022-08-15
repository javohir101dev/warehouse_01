package uz.pevops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link uz.pevops.domain.OutputProduct} entity. This class is used
 * in {@link uz.pevops.web.rest.OutputProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /output-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class OutputProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter amount;

    private LongFilter price;

    private LongFilter outputId;

    private LongFilter productId;

    private LongFilter currencyId;

    private Boolean distinct;

    public OutputProductCriteria() {}

    public OutputProductCriteria(OutputProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.outputId = other.outputId == null ? null : other.outputId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OutputProductCriteria copy() {
        return new OutputProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAmount() {
        return amount;
    }

    public LongFilter amount() {
        if (amount == null) {
            amount = new LongFilter();
        }
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public LongFilter getPrice() {
        return price;
    }

    public LongFilter price() {
        if (price == null) {
            price = new LongFilter();
        }
        return price;
    }

    public void setPrice(LongFilter price) {
        this.price = price;
    }

    public LongFilter getOutputId() {
        return outputId;
    }

    public LongFilter outputId() {
        if (outputId == null) {
            outputId = new LongFilter();
        }
        return outputId;
    }

    public void setOutputId(LongFilter outputId) {
        this.outputId = outputId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            currencyId = new LongFilter();
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OutputProductCriteria that = (OutputProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(price, that.price) &&
            Objects.equals(outputId, that.outputId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, price, outputId, productId, currencyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (outputId != null ? "outputId=" + outputId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
