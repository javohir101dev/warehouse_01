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
import uz.devops.domain.Output;
import uz.devops.domain.Users;
import uz.devops.domain.Warehouse;
import uz.devops.repository.OutputRepository;
import uz.devops.service.criteria.OutputCriteria;
import uz.devops.service.dto.OutputDTO;
import uz.devops.service.mapper.OutputMapper;

/**
 * Integration tests for the {@link OutputResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OutputResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/outputs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private OutputMapper outputMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutputMockMvc;

    private Output output;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Output createEntity(EntityManager em) {
        Output output = new Output().name(DEFAULT_NAME).date(DEFAULT_DATE).code(DEFAULT_CODE);
        return output;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Output createUpdatedEntity(EntityManager em) {
        Output output = new Output().name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);
        return output;
    }

    @BeforeEach
    public void initTest() {
        output = createEntity(em);
    }

    @Test
    @Transactional
    void createOutput() throws Exception {
        int databaseSizeBeforeCreate = outputRepository.findAll().size();
        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);
        restOutputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeCreate + 1);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOutput.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOutput.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createOutputWithExistingId() throws Exception {
        // Create the Output with an existing ID
        output.setId(1L);
        OutputDTO outputDTO = outputMapper.toDto(output);

        int databaseSizeBeforeCreate = outputRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = outputRepository.findAll().size();
        // set the field null
        output.setName(null);

        // Create the Output, which fails.
        OutputDTO outputDTO = outputMapper.toDto(output);

        restOutputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = outputRepository.findAll().size();
        // set the field null
        output.setDate(null);

        // Create the Output, which fails.
        OutputDTO outputDTO = outputMapper.toDto(output);

        restOutputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = outputRepository.findAll().size();
        // set the field null
        output.setCode(null);

        // Create the Output, which fails.
        OutputDTO outputDTO = outputMapper.toDto(output);

        restOutputMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOutputs() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList
        restOutputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(output.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get the output
        restOutputMockMvc
            .perform(get(ENTITY_API_URL_ID, output.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(output.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getOutputsByIdFiltering() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        Long id = output.getId();

        defaultOutputShouldBeFound("id.equals=" + id);
        defaultOutputShouldNotBeFound("id.notEquals=" + id);

        defaultOutputShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutputShouldNotBeFound("id.greaterThan=" + id);

        defaultOutputShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutputShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOutputsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where name equals to DEFAULT_NAME
        defaultOutputShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the outputList where name equals to UPDATED_NAME
        defaultOutputShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOutputsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOutputShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the outputList where name equals to UPDATED_NAME
        defaultOutputShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOutputsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where name is not null
        defaultOutputShouldBeFound("name.specified=true");

        // Get all the outputList where name is null
        defaultOutputShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputsByNameContainsSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where name contains DEFAULT_NAME
        defaultOutputShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the outputList where name contains UPDATED_NAME
        defaultOutputShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOutputsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where name does not contain DEFAULT_NAME
        defaultOutputShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the outputList where name does not contain UPDATED_NAME
        defaultOutputShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOutputsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where date equals to DEFAULT_DATE
        defaultOutputShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the outputList where date equals to UPDATED_DATE
        defaultOutputShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOutputsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOutputShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the outputList where date equals to UPDATED_DATE
        defaultOutputShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOutputsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where date is not null
        defaultOutputShouldBeFound("date.specified=true");

        // Get all the outputList where date is null
        defaultOutputShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where code equals to DEFAULT_CODE
        defaultOutputShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the outputList where code equals to UPDATED_CODE
        defaultOutputShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOutputsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOutputShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the outputList where code equals to UPDATED_CODE
        defaultOutputShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOutputsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where code is not null
        defaultOutputShouldBeFound("code.specified=true");

        // Get all the outputList where code is null
        defaultOutputShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllOutputsByCodeContainsSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where code contains DEFAULT_CODE
        defaultOutputShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the outputList where code contains UPDATED_CODE
        defaultOutputShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOutputsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputList where code does not contain DEFAULT_CODE
        defaultOutputShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the outputList where code does not contain UPDATED_CODE
        defaultOutputShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOutputsByWarehouseIsEqualToSomething() throws Exception {
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            outputRepository.saveAndFlush(output);
            warehouse = WarehouseResourceIT.createEntity(em);
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        em.persist(warehouse);
        em.flush();
        output.setWarehouse(warehouse);
        outputRepository.saveAndFlush(output);
        Long warehouseId = warehouse.getId();

        // Get all the outputList where warehouse equals to warehouseId
        defaultOutputShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the outputList where warehouse equals to (warehouseId + 1)
        defaultOutputShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }

    @Test
    @Transactional
    void getAllOutputsByClientIsEqualToSomething() throws Exception {
        Users client;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            outputRepository.saveAndFlush(output);
            client = UsersResourceIT.createEntity(em);
        } else {
            client = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(client);
        em.flush();
        output.setClient(client);
        outputRepository.saveAndFlush(output);
        Long clientId = client.getId();

        // Get all the outputList where client equals to clientId
        defaultOutputShouldBeFound("clientId.equals=" + clientId);

        // Get all the outputList where client equals to (clientId + 1)
        defaultOutputShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutputShouldBeFound(String filter) throws Exception {
        restOutputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(output.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restOutputMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutputShouldNotBeFound(String filter) throws Exception {
        restOutputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutputMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOutput() throws Exception {
        // Get the output
        restOutputMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output
        Output updatedOutput = outputRepository.findById(output.getId()).get();
        // Disconnect from session so that the updates on updatedOutput are not directly saved in db
        em.detach(updatedOutput);
        updatedOutput.name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);
        OutputDTO outputDTO = outputMapper.toDto(updatedOutput);

        restOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOutput.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOutput.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outputDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOutputWithPatch() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output using partial update
        Output partialUpdatedOutput = new Output();
        partialUpdatedOutput.setId(output.getId());

        partialUpdatedOutput.date(UPDATED_DATE);

        restOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutput.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutput))
            )
            .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOutput.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOutput.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateOutputWithPatch() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output using partial update
        Output partialUpdatedOutput = new Output();
        partialUpdatedOutput.setId(output.getId());

        partialUpdatedOutput.name(UPDATED_NAME).date(UPDATED_DATE).code(UPDATED_CODE);

        restOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutput.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutput))
            )
            .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputList.get(outputList.size() - 1);
        assertThat(testOutput.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOutput.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOutput.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outputDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutput() throws Exception {
        int databaseSizeBeforeUpdate = outputRepository.findAll().size();
        output.setId(count.incrementAndGet());

        // Create the Output
        OutputDTO outputDTO = outputMapper.toDto(output);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutputMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outputDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Output in the database
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        int databaseSizeBeforeDelete = outputRepository.findAll().size();

        // Delete the output
        restOutputMockMvc
            .perform(delete(ENTITY_API_URL_ID, output.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Output> outputList = outputRepository.findAll();
        assertThat(outputList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
