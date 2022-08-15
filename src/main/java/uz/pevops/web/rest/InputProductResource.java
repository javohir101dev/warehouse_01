package uz.pevops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.pevops.repository.InputProductRepository;
import uz.pevops.service.InputProductQueryService;
import uz.pevops.service.InputProductService;
import uz.pevops.service.criteria.InputProductCriteria;
import uz.pevops.service.dto.InputProductDTO;
import uz.pevops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.pevops.domain.InputProduct}.
 */
@RestController
@RequestMapping("/api")
public class InputProductResource {

    private final Logger log = LoggerFactory.getLogger(InputProductResource.class);

    private static final String ENTITY_NAME = "warehouse01InputProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InputProductService inputProductService;

    private final InputProductRepository inputProductRepository;

    private final InputProductQueryService inputProductQueryService;

    public InputProductResource(
        InputProductService inputProductService,
        InputProductRepository inputProductRepository,
        InputProductQueryService inputProductQueryService
    ) {
        this.inputProductService = inputProductService;
        this.inputProductRepository = inputProductRepository;
        this.inputProductQueryService = inputProductQueryService;
    }

    /**
     * {@code POST  /input-products} : Create a new inputProduct.
     *
     * @param inputProductDTO the inputProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inputProductDTO, or with status {@code 400 (Bad Request)} if the inputProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/input-products")
    public ResponseEntity<InputProductDTO> createInputProduct(@Valid @RequestBody InputProductDTO inputProductDTO)
        throws URISyntaxException {
        log.debug("REST request to save InputProduct : {}", inputProductDTO);
        if (inputProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new inputProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InputProductDTO result = inputProductService.save(inputProductDTO);
        return ResponseEntity
            .created(new URI("/api/input-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /input-products/:id} : Updates an existing inputProduct.
     *
     * @param id the id of the inputProductDTO to save.
     * @param inputProductDTO the inputProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputProductDTO,
     * or with status {@code 400 (Bad Request)} if the inputProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inputProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/input-products/{id}")
    public ResponseEntity<InputProductDTO> updateInputProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InputProductDTO inputProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InputProduct : {}, {}", id, inputProductDTO);
        if (inputProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InputProductDTO result = inputProductService.update(inputProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /input-products/:id} : Partial updates given fields of an existing inputProduct, field will ignore if it is null
     *
     * @param id the id of the inputProductDTO to save.
     * @param inputProductDTO the inputProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputProductDTO,
     * or with status {@code 400 (Bad Request)} if the inputProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inputProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inputProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/input-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InputProductDTO> partialUpdateInputProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InputProductDTO inputProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InputProduct partially : {}, {}", id, inputProductDTO);
        if (inputProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InputProductDTO> result = inputProductService.partialUpdate(inputProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /input-products} : get all the inputProducts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inputProducts in body.
     */
    @GetMapping("/input-products")
    public ResponseEntity<List<InputProductDTO>> getAllInputProducts(
        InputProductCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InputProducts by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<InputProductDTO> page = inputProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /input-products/count} : count all the inputProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/input-products/count")
    public ResponseEntity<Long> countInputProducts(InputProductCriteria criteria) {
        log.debug("REST request to count InputProducts by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(inputProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /input-products/:id} : get the "id" inputProduct.
     *
     * @param id the id of the inputProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inputProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/input-products/{id}")
    public ResponseEntity<InputProductDTO> getInputProduct(@PathVariable Long id) {
        log.debug("REST request to get InputProduct : {}", id);
        Optional<InputProductDTO> inputProductDTO = inputProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inputProductDTO);
    }

    /**
     * {@code DELETE  /input-products/:id} : delete the "id" inputProduct.
     *
     * @param id the id of the inputProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/input-products/{id}")
    public ResponseEntity<Void> deleteInputProduct(@PathVariable Long id) {
        log.debug("REST request to delete InputProduct : {}", id);
        inputProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
