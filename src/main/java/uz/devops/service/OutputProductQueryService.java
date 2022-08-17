package uz.devops.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.OutputProduct;
import uz.devops.repository.OutputProductRepository;
import uz.devops.service.criteria.OutputProductCriteria;
import uz.devops.service.dto.OutputProductDTO;
import uz.devops.service.mapper.OutputProductMapper;

/**
 * Service for executing complex queries for {@link OutputProduct} entities in the database.
 * The main input is a {@link OutputProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutputProductDTO} or a {@link Page} of {@link OutputProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutputProductQueryService extends QueryService<OutputProduct> {

    private final Logger log = LoggerFactory.getLogger(OutputProductQueryService.class);

    private final OutputProductRepository outputProductRepository;

    private final OutputProductMapper outputProductMapper;

    public OutputProductQueryService(OutputProductRepository outputProductRepository, OutputProductMapper outputProductMapper) {
        this.outputProductRepository = outputProductRepository;
        this.outputProductMapper = outputProductMapper;
    }

    /**
     * Return a {@link List} of {@link OutputProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutputProductDTO> findByCriteria(OutputProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<OutputProduct> specification = createSpecification(criteria);
        return outputProductMapper.toDto(outputProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutputProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutputProductDTO> findByCriteria(OutputProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<OutputProduct> specification = createSpecification(criteria);
        return outputProductRepository.findAll(specification, page).map(outputProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutputProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<OutputProduct> specification = createSpecification(criteria);
        return outputProductRepository.count(specification);
    }

    /**
     * Function to convert {@link OutputProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutputProduct> createSpecification(OutputProductCriteria criteria) {
        Specification<OutputProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutputProduct_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), OutputProduct_.amount));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), OutputProduct_.price));
            }
            if (criteria.getOutputId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOutputId(), root -> root.join(OutputProduct_.output, JoinType.LEFT).get(Output_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(OutputProduct_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyId(),
                            root -> root.join(OutputProduct_.currency, JoinType.LEFT).get(Currency_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
