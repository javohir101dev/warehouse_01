package uz.devops.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.devops.repository.CurrencyRepositoryCustom;
import uz.devops.service.CurrencyQueryService;
import uz.devops.service.CurrencyServiceCustom;
import uz.devops.service.criteria.CurrencyCriteria;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link uz.devops.domain.Currency}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResourceCustom {

    private final Logger log = LoggerFactory.getLogger(CurrencyResourceCustom.class);

    private static final String ENTITY_NAME = "warehouse01Currency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyServiceCustom currencyService;

    private final CurrencyRepositoryCustom currencyRepository;

    private final CurrencyQueryService currencyQueryService;

    public CurrencyResourceCustom(
        CurrencyServiceCustom currencyService,
        CurrencyRepositoryCustom currencyRepository,
        CurrencyQueryService currencyQueryService
    ) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
        this.currencyQueryService = currencyQueryService;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyDTO, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currencies")
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {

        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (currencyRepository.existsByName(currencyDTO.getName())){
            throw new BadRequestAlertException("Currency with this name: " + currencyDTO.getName() + "  is already exists", ENTITY_NAME, "nameExists");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity
            .created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
