package uz.pevops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import uz.pevops.IntegrationTest;
import uz.pevops.domain.Currency;
import uz.pevops.domain.Output;
import uz.pevops.domain.OutputProduct;
import uz.pevops.domain.Product;
import uz.pevops.repository.OutputProductRepository;
import uz.pevops.service.criteria.OutputProductCriteria;
import uz.pevops.service.dto.OutputProductDTO;
import uz.pevops.service.mapper.OutputProductMapper;

/**
 * Integration tests for the {@link OutputProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OutputProductResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;
    private static final Long SMALLER_PRICE = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/output-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutputProductRepository outputProductRepository;

    @Autowired
    private OutputProductMapper outputProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutputProductMockMvc;

    private OutputProduct outputProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutputProduct createEntity(EntityManager em) {
        OutputProduct outputProduct = new OutputProduct().amount(DEFAULT_AMOUNT).price(DEFAULT_PRICE);
        return outputProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutputProduct createUpdatedEntity(EntityManager em) {
        OutputProduct outputProduct = new OutputProduct().amount(UPDATED_AMOUNT).price(UPDATED_PRICE);
        return outputProduct;
    }

    @BeforeEach
    public void initTest() {
        outputProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createOutputProduct() throws Exception {
        int databaseSizeBeforeCreate = outputProductRepository.findAll().size();
        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);
        restOutputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeCreate + 1);
        OutputProduct testOutputProduct = outputProductList.get(outputProductList.size() - 1);
        assertThat(testOutputProduct.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testOutputProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createOutputProductWithExistingId() throws Exception {
        // Create the OutputProduct with an existing ID
        outputProduct.setId(1L);
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        int databaseSizeBeforeCreate = outputProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutputProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOutputProducts() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outputProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    void getOutputProduct() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get the outputProduct
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL_ID, outputProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outputProduct.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    void getOutputProductsByIdFiltering() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        Long id = outputProduct.getId();

        defaultOutputProductShouldBeFound("id.equals=" + id);
        defaultOutputProductShouldNotBeFound("id.notEquals=" + id);

        defaultOutputProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutputProductShouldNotBeFound("id.greaterThan=" + id);

        defaultOutputProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutputProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount equals to DEFAULT_AMOUNT
        defaultOutputProductShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the outputProductList where amount equals to UPDATED_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultOutputProductShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the outputProductList where amount equals to UPDATED_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount is not null
        defaultOutputProductShouldBeFound("amount.specified=true");

        // Get all the outputProductList where amount is null
        defaultOutputProductShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultOutputProductShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the outputProductList where amount is greater than or equal to UPDATED_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount is less than or equal to DEFAULT_AMOUNT
        defaultOutputProductShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the outputProductList where amount is less than or equal to SMALLER_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount is less than DEFAULT_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the outputProductList where amount is less than UPDATED_AMOUNT
        defaultOutputProductShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where amount is greater than DEFAULT_AMOUNT
        defaultOutputProductShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the outputProductList where amount is greater than SMALLER_AMOUNT
        defaultOutputProductShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price equals to DEFAULT_PRICE
        defaultOutputProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the outputProductList where price equals to UPDATED_PRICE
        defaultOutputProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultOutputProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the outputProductList where price equals to UPDATED_PRICE
        defaultOutputProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price is not null
        defaultOutputProductShouldBeFound("price.specified=true");

        // Get all the outputProductList where price is null
        defaultOutputProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price is greater than or equal to DEFAULT_PRICE
        defaultOutputProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the outputProductList where price is greater than or equal to UPDATED_PRICE
        defaultOutputProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price is less than or equal to DEFAULT_PRICE
        defaultOutputProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the outputProductList where price is less than or equal to SMALLER_PRICE
        defaultOutputProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price is less than DEFAULT_PRICE
        defaultOutputProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the outputProductList where price is less than UPDATED_PRICE
        defaultOutputProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        // Get all the outputProductList where price is greater than DEFAULT_PRICE
        defaultOutputProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the outputProductList where price is greater than SMALLER_PRICE
        defaultOutputProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOutputProductsByOutputIsEqualToSomething() throws Exception {
        Output output;
        if (TestUtil.findAll(em, Output.class).isEmpty()) {
            outputProductRepository.saveAndFlush(outputProduct);
            output = OutputResourceIT.createEntity(em);
        } else {
            output = TestUtil.findAll(em, Output.class).get(0);
        }
        em.persist(output);
        em.flush();
        outputProduct.setOutput(output);
        outputProductRepository.saveAndFlush(outputProduct);
        Long outputId = output.getId();

        // Get all the outputProductList where output equals to outputId
        defaultOutputProductShouldBeFound("outputId.equals=" + outputId);

        // Get all the outputProductList where output equals to (outputId + 1)
        defaultOutputProductShouldNotBeFound("outputId.equals=" + (outputId + 1));
    }

    @Test
    @Transactional
    void getAllOutputProductsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            outputProductRepository.saveAndFlush(outputProduct);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        outputProduct.setProduct(product);
        outputProductRepository.saveAndFlush(outputProduct);
        Long productId = product.getId();

        // Get all the outputProductList where product equals to productId
        defaultOutputProductShouldBeFound("productId.equals=" + productId);

        // Get all the outputProductList where product equals to (productId + 1)
        defaultOutputProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllOutputProductsByCurrencyIsEqualToSomething() throws Exception {
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            outputProductRepository.saveAndFlush(outputProduct);
            currency = CurrencyResourceIT.createEntity(em);
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        em.persist(currency);
        em.flush();
        outputProduct.setCurrency(currency);
        outputProductRepository.saveAndFlush(outputProduct);
        Long currencyId = currency.getId();

        // Get all the outputProductList where currency equals to currencyId
        defaultOutputProductShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the outputProductList where currency equals to (currencyId + 1)
        defaultOutputProductShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutputProductShouldBeFound(String filter) throws Exception {
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outputProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));

        // Check, that the count call also returns 1
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutputProductShouldNotBeFound(String filter) throws Exception {
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutputProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOutputProduct() throws Exception {
        // Get the outputProduct
        restOutputProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutputProduct() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();

        // Update the outputProduct
        OutputProduct updatedOutputProduct = outputProductRepository.findById(outputProduct.getId()).get();
        // Disconnect from session so that the updates on updatedOutputProduct are not directly saved in db
        em.detach(updatedOutputProduct);
        updatedOutputProduct.amount(UPDATED_AMOUNT).price(UPDATED_PRICE);
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(updatedOutputProduct);

        restOutputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
        OutputProduct testOutputProduct = outputProductList.get(outputProductList.size() - 1);
        assertThat(testOutputProduct.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOutputProduct.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOutputProductWithPatch() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();

        // Update the outputProduct using partial update
        OutputProduct partialUpdatedOutputProduct = new OutputProduct();
        partialUpdatedOutputProduct.setId(outputProduct.getId());

        partialUpdatedOutputProduct.amount(UPDATED_AMOUNT);

        restOutputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutputProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutputProduct))
            )
            .andExpect(status().isOk());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
        OutputProduct testOutputProduct = outputProductList.get(outputProductList.size() - 1);
        assertThat(testOutputProduct.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOutputProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOutputProductWithPatch() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();

        // Update the outputProduct using partial update
        OutputProduct partialUpdatedOutputProduct = new OutputProduct();
        partialUpdatedOutputProduct.setId(outputProduct.getId());

        partialUpdatedOutputProduct.amount(UPDATED_AMOUNT).price(UPDATED_PRICE);

        restOutputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutputProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutputProduct))
            )
            .andExpect(status().isOk());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
        OutputProduct testOutputProduct = outputProductList.get(outputProductList.size() - 1);
        assertThat(testOutputProduct.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOutputProduct.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outputProductDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutputProduct() throws Exception {
        int databaseSizeBeforeUpdate = outputProductRepository.findAll().size();
        outputProduct.setId(count.incrementAndGet());

        // Create the OutputProduct
        OutputProductDTO outputProductDTO = outputProductMapper.toDto(outputProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutputProduct in the database
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOutputProduct() throws Exception {
        // Initialize the database
        outputProductRepository.saveAndFlush(outputProduct);

        int databaseSizeBeforeDelete = outputProductRepository.findAll().size();

        // Delete the outputProduct
        restOutputProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, outputProduct.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutputProduct> outputProductList = outputProductRepository.findAll();
        assertThat(outputProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
