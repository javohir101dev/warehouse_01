package uz.devops.service.criteria;

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
import uz.devops.domain.enumeration.Status;

/**
 * Criteria class for the {@link uz.devops.domain.Product} entity. This class is used
 * in {@link uz.devops.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StatusFilter status;

    private StringFilter code;

    private LongFilter amount;

    private LongFilter photoId;

    private LongFilter measurementId;

    private LongFilter categoryId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
        this.measurementId = other.measurementId == null ? null : other.measurementId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public LongFilter getPhotoId() {
        return photoId;
    }

    public LongFilter photoId() {
        if (photoId == null) {
            photoId = new LongFilter();
        }
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }

    public LongFilter getMeasurementId() {
        return measurementId;
    }

    public LongFilter measurementId() {
        if (measurementId == null) {
            measurementId = new LongFilter();
        }
        return measurementId;
    }

    public void setMeasurementId(LongFilter measurementId) {
        this.measurementId = measurementId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(code, that.code) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(measurementId, that.measurementId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, code, amount, photoId, measurementId, categoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (photoId != null ? "photoId=" + photoId + ", " : "") +
            (measurementId != null ? "measurementId=" + measurementId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
