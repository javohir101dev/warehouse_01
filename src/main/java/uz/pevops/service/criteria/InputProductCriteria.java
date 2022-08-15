package uz.pevops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link uz.pevops.domain.InputProduct} entity. This class is used
 * in {@link uz.pevops.web.rest.InputProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /input-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class InputProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter amount;

    private LongFilter price;

    private InstantFilter expireDate;

    private LongFilter productId;

    private LongFilter inputId;

    private LongFilter currencyId;

    private Boolean distinct;

    public InputProductCriteria() {}

    public InputProductCriteria(InputProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.expireDate = other.expireDate == null ? null : other.expireDate.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.inputId = other.inputId == null ? null : other.inputId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InputProductCriteria copy() {
        return new InputProductCriteria(this);
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

    public InstantFilter getExpireDate() {
        return expireDate;
    }

    public InstantFilter expireDate() {
        if (expireDate == null) {
            expireDate = new InstantFilter();
        }
        return expireDate;
    }

    public void setExpireDate(InstantFilter expireDate) {
        this.expireDate = expireDate;
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

    public LongFilter getInputId() {
        return inputId;
    }

    public LongFilter inputId() {
        if (inputId == null) {
            inputId = new LongFilter();
        }
        return inputId;
    }

    public void setInputId(LongFilter inputId) {
        this.inputId = inputId;
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
        final InputProductCriteria that = (InputProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(price, that.price) &&
            Objects.equals(expireDate, that.expireDate) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(inputId, that.inputId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, price, expireDate, productId, inputId, currencyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (expireDate != null ? "expireDate=" + expireDate + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (inputId != null ? "inputId=" + inputId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
