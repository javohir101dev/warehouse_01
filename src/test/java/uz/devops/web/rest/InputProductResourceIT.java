package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.IntegrationTest;
import uz.devops.domain.Currency;
import uz.devops.domain.Input;
import uz.devops.domain.InputProduct;
import uz.devops.domain.Product;
import uz.devops.repository.InputProductRepository;
import uz.devops.service.criteria.InputProductCriteria;
import uz.devops.service.dto.InputProductDTO;
import uz.devops.service.mapper.InputProductMapper;

/**
 * Integration tests for the {@link InputProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InputProductResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;
    private static final Long SMALLER_PRICE = 1L - 1L;

    private static final Instant DEFAULT_EXPIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/input-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InputProductRepository inputProductRepository;

    @Autowired
    private InputProductMapper inputProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInputProductMockMvc;

    private InputProduct inputProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InputProduct createEntity(EntityManager em) {
        InputProduct inputProduct = new InputProduct().amount(DEFAULT_AMOUNT).price(DEFAULT_PRICE).expireDate(DEFAULT_EXPIRE_DATE);
        return inputProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InputProduct createUpdatedEntity(EntityManager em) {
        InputProduct inputProduct = new InputProduct().amount(UPDATED_AMOUNT).price(UPDATED_PRICE).expireDate(UPDATED_EXPIRE_DATE);
        return inputProduct;
    }

    @BeforeEach
    public void initTest() {
        inputProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createInputProduct() throws Exception {
        int databaseSizeBeforeCreate = inputProductRepository.findAll().size();
        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);
        restInputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeCreate + 1);
        InputProduct testInputProduct = inputProductList.get(inputProductList.size() - 1);
        assertThat(testInputProduct.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInputProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testInputProduct.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void createInputProductWithExistingId() throws Exception {
        // Create the InputProduct with an existing ID
        inputProduct.setId(1L);
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        int databaseSizeBeforeCreate = inputProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = inputProductRepository.findAll().size();
        // set the field null
        inputProduct.setAmount(null);

        // Create the InputProduct, which fails.
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        restInputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = inputProductRepository.findAll().size();
        // set the field null
        inputProduct.setPrice(null);

        // Create the InputProduct, which fails.
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        restInputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInputProducts() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())));
    }

    @Test
    @Transactional
    void getInputProduct() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get the inputProduct
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL_ID, inputProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inputProduct.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()));
    }

    @Test
    @Transactional
    void getInputProductsByIdFiltering() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        Long id = inputProduct.getId();

        defaultInputProductShouldBeFound("id.equals=" + id);
        defaultInputProductShouldNotBeFound("id.notEquals=" + id);

        defaultInputProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInputProductShouldNotBeFound("id.greaterThan=" + id);

        defaultInputProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInputProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount equals to DEFAULT_AMOUNT
        defaultInputProductShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the inputProductList where amount equals to UPDATED_AMOUNT
        defaultInputProductShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultInputProductShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the inputProductList where amount equals to UPDATED_AMOUNT
        defaultInputProductShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount is not null
        defaultInputProductShouldBeFound("amount.specified=true");

        // Get all the inputProductList where amount is null
        defaultInputProductShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultInputProductShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the inputProductList where amount is greater than or equal to UPDATED_AMOUNT
        defaultInputProductShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount is less than or equal to DEFAULT_AMOUNT
        defaultInputProductShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the inputProductList where amount is less than or equal to SMALLER_AMOUNT
        defaultInputProductShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount is less than DEFAULT_AMOUNT
        defaultInputProductShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the inputProductList where amount is less than UPDATED_AMOUNT
        defaultInputProductShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where amount is greater than DEFAULT_AMOUNT
        defaultInputProductShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the inputProductList where amount is greater than SMALLER_AMOUNT
        defaultInputProductShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price equals to DEFAULT_PRICE
        defaultInputProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the inputProductList where price equals to UPDATED_PRICE
        defaultInputProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultInputProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the inputProductList where price equals to UPDATED_PRICE
        defaultInputProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price is not null
        defaultInputProductShouldBeFound("price.specified=true");

        // Get all the inputProductList where price is null
        defaultInputProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price is greater than or equal to DEFAULT_PRICE
        defaultInputProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the inputProductList where price is greater than or equal to UPDATED_PRICE
        defaultInputProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price is less than or equal to DEFAULT_PRICE
        defaultInputProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the inputProductList where price is less than or equal to SMALLER_PRICE
        defaultInputProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price is less than DEFAULT_PRICE
        defaultInputProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the inputProductList where price is less than UPDATED_PRICE
        defaultInputProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where price is greater than DEFAULT_PRICE
        defaultInputProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the inputProductList where price is greater than SMALLER_PRICE
        defaultInputProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllInputProductsByExpireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where expireDate equals to DEFAULT_EXPIRE_DATE
        defaultInputProductShouldBeFound("expireDate.equals=" + DEFAULT_EXPIRE_DATE);

        // Get all the inputProductList where expireDate equals to UPDATED_EXPIRE_DATE
        defaultInputProductShouldNotBeFound("expireDate.equals=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllInputProductsByExpireDateIsInShouldWork() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where expireDate in DEFAULT_EXPIRE_DATE or UPDATED_EXPIRE_DATE
        defaultInputProductShouldBeFound("expireDate.in=" + DEFAULT_EXPIRE_DATE + "," + UPDATED_EXPIRE_DATE);

        // Get all the inputProductList where expireDate equals to UPDATED_EXPIRE_DATE
        defaultInputProductShouldNotBeFound("expireDate.in=" + UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void getAllInputProductsByExpireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        // Get all the inputProductList where expireDate is not null
        defaultInputProductShouldBeFound("expireDate.specified=true");

        // Get all the inputProductList where expireDate is null
        defaultInputProductShouldNotBeFound("expireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInputProductsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inputProductRepository.saveAndFlush(inputProduct);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        inputProduct.setProduct(product);
        inputProductRepository.saveAndFlush(inputProduct);
        Long productId = product.getId();

        // Get all the inputProductList where product equals to productId
        defaultInputProductShouldBeFound("productId.equals=" + productId);

        // Get all the inputProductList where product equals to (productId + 1)
        defaultInputProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllInputProductsByInputIsEqualToSomething() throws Exception {
        Input input;
        if (TestUtil.findAll(em, Input.class).isEmpty()) {
            inputProductRepository.saveAndFlush(inputProduct);
            input = InputResourceIT.createEntity(em);
        } else {
            input = TestUtil.findAll(em, Input.class).get(0);
        }
        em.persist(input);
        em.flush();
        inputProduct.setInput(input);
        inputProductRepository.saveAndFlush(inputProduct);
        Long inputId = input.getId();

        // Get all the inputProductList where input equals to inputId
        defaultInputProductShouldBeFound("inputId.equals=" + inputId);

        // Get all the inputProductList where input equals to (inputId + 1)
        defaultInputProductShouldNotBeFound("inputId.equals=" + (inputId + 1));
    }

    @Test
    @Transactional
    void getAllInputProductsByCurrencyIsEqualToSomething() throws Exception {
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            inputProductRepository.saveAndFlush(inputProduct);
            currency = CurrencyResourceIT.createEntity(em);
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        em.persist(currency);
        em.flush();
        inputProduct.setCurrency(currency);
        inputProductRepository.saveAndFlush(inputProduct);
        Long currencyId = currency.getId();

        // Get all the inputProductList where currency equals to currencyId
        defaultInputProductShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the inputProductList where currency equals to (currencyId + 1)
        defaultInputProductShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInputProductShouldBeFound(String filter) throws Exception {
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inputProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())));

        // Check, that the count call also returns 1
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInputProductShouldNotBeFound(String filter) throws Exception {
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInputProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInputProduct() throws Exception {
        // Get the inputProduct
        restInputProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInputProduct() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();

        // Update the inputProduct
        InputProduct updatedInputProduct = inputProductRepository.findById(inputProduct.getId()).get();
        // Disconnect from session so that the updates on updatedInputProduct are not directly saved in db
        em.detach(updatedInputProduct);
        updatedInputProduct.amount(UPDATED_AMOUNT).price(UPDATED_PRICE).expireDate(UPDATED_EXPIRE_DATE);
        InputProductDTO inputProductDTO = inputProductMapper.toDto(updatedInputProduct);

        restInputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
        InputProduct testInputProduct = inputProductList.get(inputProductList.size() - 1);
        assertThat(testInputProduct.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInputProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInputProduct.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInputProductWithPatch() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();

        // Update the inputProduct using partial update
        InputProduct partialUpdatedInputProduct = new InputProduct();
        partialUpdatedInputProduct.setId(inputProduct.getId());

        partialUpdatedInputProduct.price(UPDATED_PRICE).expireDate(UPDATED_EXPIRE_DATE);

        restInputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInputProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInputProduct))
            )
            .andExpect(status().isOk());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
        InputProduct testInputProduct = inputProductList.get(inputProductList.size() - 1);
        assertThat(testInputProduct.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInputProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInputProduct.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInputProductWithPatch() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();

        // Update the inputProduct using partial update
        InputProduct partialUpdatedInputProduct = new InputProduct();
        partialUpdatedInputProduct.setId(inputProduct.getId());

        partialUpdatedInputProduct.amount(UPDATED_AMOUNT).price(UPDATED_PRICE).expireDate(UPDATED_EXPIRE_DATE);

        restInputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInputProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInputProduct))
            )
            .andExpect(status().isOk());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
        InputProduct testInputProduct = inputProductList.get(inputProductList.size() - 1);
        assertThat(testInputProduct.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInputProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInputProduct.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inputProductDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInputProduct() throws Exception {
        int databaseSizeBeforeUpdate = inputProductRepository.findAll().size();
        inputProduct.setId(count.incrementAndGet());

        // Create the InputProduct
        InputProductDTO inputProductDTO = inputProductMapper.toDto(inputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InputProduct in the database
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInputProduct() throws Exception {
        // Initialize the database
        inputProductRepository.saveAndFlush(inputProduct);

        int databaseSizeBeforeDelete = inputProductRepository.findAll().size();

        // Delete the inputProduct
        restInputProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, inputProduct.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InputProduct> inputProductList = inputProductRepository.findAll();
        assertThat(inputProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
