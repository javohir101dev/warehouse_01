package uz.pevops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import uz.pevops.repository.OutputProductRepository;
import uz.pevops.service.OutputProductQueryService;
import uz.pevops.service.OutputProductService;
import uz.pevops.service.criteria.OutputProductCriteria;
import uz.pevops.service.dto.OutputProductDTO;
import uz.pevops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.pevops.domain.OutputProduct}.
 */
@RestController
@RequestMapping("/api")
public class OutputProductResource {

    private final Logger log = LoggerFactory.getLogger(OutputProductResource.class);

    private static final String ENTITY_NAME = "warehouse01OutputProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutputProductService outputProductService;

    private final OutputProductRepository outputProductRepository;

    private final OutputProductQueryService outputProductQueryService;

    public OutputProductResource(
        OutputProductService outputProductService,
        OutputProductRepository outputProductRepository,
        OutputProductQueryService outputProductQueryService
    ) {
        this.outputProductService = outputProductService;
        this.outputProductRepository = outputProductRepository;
        this.outputProductQueryService = outputProductQueryService;
    }

    /**
     * {@code POST  /output-products} : Create a new outputProduct.
     *
     * @param outputProductDTO the outputProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outputProductDTO, or with status {@code 400 (Bad Request)} if the outputProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/output-products")
    public ResponseEntity<OutputProductDTO> createOutputProduct(@RequestBody OutputProductDTO outputProductDTO) throws URISyntaxException {
        log.debug("REST request to save OutputProduct : {}", outputProductDTO);
        if (outputProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new outputProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutputProductDTO result = outputProductService.save(outputProductDTO);
        return ResponseEntity
            .created(new URI("/api/output-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /output-products/:id} : Updates an existing outputProduct.
     *
     * @param id the id of the outputProductDTO to save.
     * @param outputProductDTO the outputProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputProductDTO,
     * or with status {@code 400 (Bad Request)} if the outputProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outputProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/output-products/{id}")
    public ResponseEntity<OutputProductDTO> updateOutputProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutputProductDTO outputProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OutputProduct : {}, {}", id, outputProductDTO);
        if (outputProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutputProductDTO result = outputProductService.update(outputProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /output-products/:id} : Partial updates given fields of an existing outputProduct, field will ignore if it is null
     *
     * @param id the id of the outputProductDTO to save.
     * @param outputProductDTO the outputProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputProductDTO,
     * or with status {@code 400 (Bad Request)} if the outputProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outputProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outputProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/output-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutputProductDTO> partialUpdateOutputProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutputProductDTO outputProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutputProduct partially : {}, {}", id, outputProductDTO);
        if (outputProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutputProductDTO> result = outputProductService.partialUpdate(outputProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /output-products} : get all the outputProducts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outputProducts in body.
     */
    @GetMapping("/output-products")
    public ResponseEntity<List<OutputProductDTO>> getAllOutputProducts(
        OutputProductCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OutputProducts by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<OutputProductDTO> page = outputProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /output-products/count} : count all the outputProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/output-products/count")
    public ResponseEntity<Long> countOutputProducts(OutputProductCriteria criteria) {
        log.debug("REST request to count OutputProducts by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(outputProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /output-products/:id} : get the "id" outputProduct.
     *
     * @param id the id of the outputProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outputProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/output-products/{id}")
    public ResponseEntity<OutputProductDTO> getOutputProduct(@PathVariable Long id) {
        log.debug("REST request to get OutputProduct : {}", id);
        Optional<OutputProductDTO> outputProductDTO = outputProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outputProductDTO);
    }

    /**
     * {@code DELETE  /output-products/:id} : delete the "id" outputProduct.
     *
     * @param id the id of the outputProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/output-products/{id}")
    public ResponseEntity<Void> deleteOutputProduct(@PathVariable Long id) {
        log.debug("REST request to delete OutputProduct : {}", id);
        outputProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
