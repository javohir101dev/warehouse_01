package uz.devops.web.rest;

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
import uz.devops.IntegrationTest;
import uz.devops.domain.Category;
import uz.devops.domain.Category;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.CategoryRepository;
import uz.devops.service.criteria.CategoryCriteria;
import uz.devops.service.dto.CategoryDTO;
import uz.devops.service.mapper.CategoryMapper;

/**
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.DELETED;

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return category;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category().name(UPDATED_NAME).status(UPDATED_STATUS);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setName(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStatus(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        Long id = category.getId();

        defaultCategoryShouldBeFound("id.equals=" + id);
        defaultCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name equals to DEFAULT_NAME
        defaultCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name is not null
        defaultCategoryShouldBeFound("name.specified=true");

        // Get all the categoryList where name is null
        defaultCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name contains DEFAULT_NAME
        defaultCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the categoryList where name contains UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name does not contain DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the categoryList where name does not contain UPDATED_NAME
        defaultCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where status equals to DEFAULT_STATUS
        defaultCategoryShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the categoryList where status equals to UPDATED_STATUS
        defaultCategoryShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCategoriesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCategoryShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the categoryList where status equals to UPDATED_STATUS
        defaultCategoryShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCategoriesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where status is not null
        defaultCategoryShouldBeFound("status.specified=true");

        // Get all the categoryList where status is null
        defaultCategoryShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryParentIsEqualToSomething() throws Exception {
        Category categoryParent;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            categoryRepository.saveAndFlush(category);
            categoryParent = CategoryResourceIT.createEntity(em);
        } else {
            categoryParent = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(categoryParent);
        em.flush();
        category.setCategoryParent(categoryParent);
        categoryRepository.saveAndFlush(category);
        Long categoryParentId = categoryParent.getId();

        // Get all the categoryList where categoryParent equals to categoryParentId
        defaultCategoryShouldBeFound("categoryParentId.equals=" + categoryParentId);

        // Get all the categoryList where categoryParent equals to (categoryParentId + 1)
        defaultCategoryShouldNotBeFound("categoryParentId.equals=" + (categoryParentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory.name(UPDATED_NAME).status(UPDATED_STATUS);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.name(UPDATED_NAME);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.name(UPDATED_NAME).status(UPDATED_STATUS);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, category.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}