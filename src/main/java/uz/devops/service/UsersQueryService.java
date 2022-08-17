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
import uz.devops.domain.Users;
import uz.devops.repository.UsersRepository;
import uz.devops.service.criteria.UsersCriteria;
import uz.devops.service.dto.UsersDTO;
import uz.devops.service.mapper.UsersMapper;

/**
 * Service for executing complex queries for {@link Users} entities in the database.
 * The main input is a {@link UsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsersDTO} or a {@link Page} of {@link UsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsersQueryService extends QueryService<Users> {

    private final Logger log = LoggerFactory.getLogger(UsersQueryService.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersQueryService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Return a {@link List} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsersDTO> findByCriteria(UsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Users> specification = createSpecification(criteria);
        return usersMapper.toDto(usersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsersDTO> findByCriteria(UsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.findAll(specification, page).map(usersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.count(specification);
    }

    /**
     * Function to convert {@link UsersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Users> createSpecification(UsersCriteria criteria) {
        Specification<Users> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Users_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Users_.lastName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Users_.phoneNumber));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Users_.code));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Users_.password));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Users_.status));
            }
            if (criteria.getWarehouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWarehouseId(),
                            root -> root.join(Users_.warehouses, JoinType.LEFT).get(Warehouse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
