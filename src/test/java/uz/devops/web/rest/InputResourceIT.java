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
import uz.devops.domain.Input;
import uz.devops.domain.Users;
import uz.devops.domain.Warehouse;
import uz.devops.repository.InputRepository;
import uz.devops.service.criteria.InputCriteria;
import uz.devops.service.dto.InputDTO;
import uz.devops.service.mapper.InputMapper;

/**
 * Integration tests for the {@link InputResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InputResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inputs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private InputMapper inputMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInputMockMvc;

    private Input input;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Input createEntity(EntityManager em) {
        Input input = new Input().name(DEFAULT_NAME).date(DEFAULT_DATE).code(DEFAULT_CODE);
        return input;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Input createUpdatedEntity(EntityManager em) {
        Input input = new Input().name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);
        return input;
    }

    @BeforeEach
    public void initTest() {
        input = createEntity(em);
    }

    @Test
    @Transactional
    void createInput() throws Exception {
        int databaseSizeBeforeCreate = inputRepository.findAll().size();
        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);
        restInputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeCreate + 1);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInput.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInput.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createInputWithExistingId() throws Exception {
        // Create the Input with an existing ID
        input.setId(1L);
        InputDTO inputDTO = inputMapper.toDto(input);

        int databaseSizeBeforeCreate = inputRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = inputRepository.findAll().size();
        // set the field null
        input.setName(null);

        // Create the Input, which fails.
        InputDTO inputDTO = inputMapper.toDto(input);

        restInputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = inputRepository.findAll().size();
        // set the field null
        input.setDate(null);

        // Create the Input, which fails.
        InputDTO inputDTO = inputMapper.toDto(input);

        restInputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = inputRepository.findAll().size();
        // set the field null
        input.setCode(null);

        // Create the Input, which fails.
        InputDTO inputDTO = inputMapper.toDto(input);

        restInputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInputs() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList
        restInputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(input.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get the input
        restInputMockMvc
            .perform(get(ENTITY_API_URL_ID, input.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(input.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getInputsByIdFiltering() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        Long id = input.getId();

        defaultInputShouldBeFound("id.equals=" + id);
        defaultInputShouldNotBeFound("id.notEquals=" + id);

        defaultInputShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInputShouldNotBeFound("id.greaterThan=" + id);

        defaultInputShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInputShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInputsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where name equals to DEFAULT_NAME
        defaultInputShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the inputList where name equals to UPDATED_NAME
        defaultInputShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInputsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInputShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the inputList where name equals to UPDATED_NAME
        defaultInputShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInputsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where name is not null
        defaultInputShouldBeFound("name.specified=true");

        // Get all the inputList where name is null
        defaultInputShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllInputsByNameContainsSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where name contains DEFAULT_NAME
        defaultInputShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the inputList where name contains UPDATED_NAME
        defaultInputShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInputsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where name does not contain DEFAULT_NAME
        defaultInputShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the inputList where name does not contain UPDATED_NAME
        defaultInputShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInputsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where date equals to DEFAULT_DATE
        defaultInputShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the inputList where date equals to UPDATED_DATE
        defaultInputShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInputsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where date in DEFAULT_DATE or UPDATED_DATE
        defaultInputShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the inputList where date equals to UPDATED_DATE
        defaultInputShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllInputsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where date is not null
        defaultInputShouldBeFound("date.specified=true");

        // Get all the inputList where date is null
        defaultInputShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllInputsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where code equals to DEFAULT_CODE
        defaultInputShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the inputList where code equals to UPDATED_CODE
        defaultInputShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInputsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where code in DEFAULT_CODE or UPDATED_CODE
        defaultInputShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the inputList where code equals to UPDATED_CODE
        defaultInputShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInputsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where code is not null
        defaultInputShouldBeFound("code.specified=true");

        // Get all the inputList where code is null
        defaultInputShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllInputsByCodeContainsSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where code contains DEFAULT_CODE
        defaultInputShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the inputList where code contains UPDATED_CODE
        defaultInputShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInputsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        // Get all the inputList where code does not contain DEFAULT_CODE
        defaultInputShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the inputList where code does not contain UPDATED_CODE
        defaultInputShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInputsByWarehouseIsEqualToSomething() throws Exception {
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            inputRepository.saveAndFlush(input);
            warehouse = WarehouseResourceIT.createEntity(em);
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        em.persist(warehouse);
        em.flush();
        input.setWarehouse(warehouse);
        inputRepository.saveAndFlush(input);
        Long warehouseId = warehouse.getId();

        // Get all the inputList where warehouse equals to warehouseId
        defaultInputShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the inputList where warehouse equals to (warehouseId + 1)
        defaultInputShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }

    @Test
    @Transactional
    void getAllInputsBySupplierIsEqualToSomething() throws Exception {
        Users supplier;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            inputRepository.saveAndFlush(input);
            supplier = UsersResourceIT.createEntity(em);
        } else {
            supplier = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        input.setSupplier(supplier);
        inputRepository.saveAndFlush(input);
        Long supplierId = supplier.getId();

        // Get all the inputList where supplier equals to supplierId
        defaultInputShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the inputList where supplier equals to (supplierId + 1)
        defaultInputShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInputShouldBeFound(String filter) throws Exception {
        restInputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(input.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restInputMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInputShouldNotBeFound(String filter) throws Exception {
        restInputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInputMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInput() throws Exception {
        // Get the input
        restInputMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeUpdate = inputRepository.findAll().size();

        // Update the input
        Input updatedInput = inputRepository.findById(input.getId()).get();
        // Disconnect from session so that the updates on updatedInput are not directly saved in db
        em.detach(updatedInput);
        updatedInput.name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);
        InputDTO inputDTO = inputMapper.toDto(updatedInput);

        restInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isOk());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInput.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInput.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inputDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInputWithPatch() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeUpdate = inputRepository.findAll().size();

        // Update the input using partial update
        Input partialUpdatedInput = new Input();
        partialUpdatedInput.setId(input.getId());

        partialUpdatedInput.name(UPDATED_NAME);

        restInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInput.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInput))
            )
            .andExpect(status().isOk());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInput.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInput.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateInputWithPatch() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeUpdate = inputRepository.findAll().size();

        // Update the input using partial update
        Input partialUpdatedInput = new Input();
        partialUpdatedInput.setId(input.getId());

        partialUpdatedInput.name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);

        restInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInput.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInput))
            )
            .andExpect(status().isOk());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
        Input testInput = inputList.get(inputList.size() - 1);
        assertThat(testInput.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInput.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInput.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inputDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInput() throws Exception {
        int databaseSizeBeforeUpdate = inputRepository.findAll().size();
        input.setId(count.incrementAndGet());

        // Create the Input
        InputDTO inputDTO = inputMapper.toDto(input);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInputMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inputDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Input in the database
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInput() throws Exception {
        // Initialize the database
        inputRepository.saveAndFlush(input);

        int databaseSizeBeforeDelete = inputRepository.findAll().size();

        // Delete the input
        restInputMockMvc
            .perform(delete(ENTITY_API_URL_ID, input.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Input> inputList = inputRepository.findAll();
        assertThat(inputList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
