package com.w2m.anime.web.rest;

import com.w2m.anime.W2MApp;
import com.w2m.anime.domain.Heroe;
import com.w2m.anime.repository.HeroeRepository;
import com.w2m.anime.service.HeroeService;
import com.w2m.anime.service.dto.HeroeDTO;
import com.w2m.anime.service.mapper.HeroeMapper;
import com.w2m.anime.service.dto.HeroeCriteria;
import com.w2m.anime.service.HeroeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HeroeResource} REST controller.
 */
@SpringBootTest(classes = W2MApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class HeroeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HeroeRepository heroeRepository;

    @Autowired
    private HeroeMapper heroeMapper;

    @Autowired
    private HeroeService heroeService;

    @Autowired
    private HeroeQueryService heroeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHeroeMockMvc;

    private Heroe heroe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Heroe createEntity(EntityManager em) {
        Heroe heroe = new Heroe()
            .name(DEFAULT_NAME);
        return heroe;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Heroe createUpdatedEntity(EntityManager em) {
        Heroe heroe = new Heroe()
            .name(UPDATED_NAME);
        return heroe;
    }

    @BeforeEach
    public void initTest() {
        heroe = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeroe() throws Exception {
        int databaseSizeBeforeCreate = heroeRepository.findAll().size();
        // Create the Heroe
        HeroeDTO heroeDTO = heroeMapper.toDto(heroe);
        restHeroeMockMvc.perform(post("/api/heroes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heroeDTO)))
            .andExpect(status().isCreated());

        // Validate the Heroe in the database
        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeCreate + 1);
        Heroe testHeroe = heroeList.get(heroeList.size() - 1);
        assertThat(testHeroe.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createHeroeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = heroeRepository.findAll().size();

        // Create the Heroe with an existing ID
        heroe.setId(1L);
        HeroeDTO heroeDTO = heroeMapper.toDto(heroe);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeroeMockMvc.perform(post("/api/heroes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heroeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Heroe in the database
        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = heroeRepository.findAll().size();
        // set the field null
        heroe.setName(null);

        // Create the Heroe, which fails.
        HeroeDTO heroeDTO = heroeMapper.toDto(heroe);


        restHeroeMockMvc.perform(post("/api/heroes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heroeDTO)))
            .andExpect(status().isBadRequest());

        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeroes() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList
        restHeroeMockMvc.perform(get("/api/heroes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heroe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getHeroe() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get the heroe
        restHeroeMockMvc.perform(get("/api/heroes/{id}", heroe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(heroe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getHeroesByIdFiltering() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        Long id = heroe.getId();

        defaultHeroeShouldBeFound("id.equals=" + id);
        defaultHeroeShouldNotBeFound("id.notEquals=" + id);

        defaultHeroeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHeroeShouldNotBeFound("id.greaterThan=" + id);

        defaultHeroeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHeroeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHeroesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name equals to DEFAULT_NAME
        defaultHeroeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the heroeList where name equals to UPDATED_NAME
        defaultHeroeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHeroesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name not equals to DEFAULT_NAME
        defaultHeroeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the heroeList where name not equals to UPDATED_NAME
        defaultHeroeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHeroesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHeroeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the heroeList where name equals to UPDATED_NAME
        defaultHeroeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHeroesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name is not null
        defaultHeroeShouldBeFound("name.specified=true");

        // Get all the heroeList where name is null
        defaultHeroeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllHeroesByNameContainsSomething() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name contains DEFAULT_NAME
        defaultHeroeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the heroeList where name contains UPDATED_NAME
        defaultHeroeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHeroesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        // Get all the heroeList where name does not contain DEFAULT_NAME
        defaultHeroeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the heroeList where name does not contain UPDATED_NAME
        defaultHeroeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHeroeShouldBeFound(String filter) throws Exception {
        restHeroeMockMvc.perform(get("/api/heroes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heroe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restHeroeMockMvc.perform(get("/api/heroes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHeroeShouldNotBeFound(String filter) throws Exception {
        restHeroeMockMvc.perform(get("/api/heroes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHeroeMockMvc.perform(get("/api/heroes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingHeroe() throws Exception {
        // Get the heroe
        restHeroeMockMvc.perform(get("/api/heroes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeroe() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        int databaseSizeBeforeUpdate = heroeRepository.findAll().size();

        // Update the heroe
        Heroe updatedHeroe = heroeRepository.findById(heroe.getId()).get();
        // Disconnect from session so that the updates on updatedHeroe are not directly saved in db
        em.detach(updatedHeroe);
        updatedHeroe
            .name(UPDATED_NAME);
        HeroeDTO heroeDTO = heroeMapper.toDto(updatedHeroe);

        restHeroeMockMvc.perform(put("/api/heroes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heroeDTO)))
            .andExpect(status().isOk());

        // Validate the Heroe in the database
        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeUpdate);
        Heroe testHeroe = heroeList.get(heroeList.size() - 1);
        assertThat(testHeroe.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingHeroe() throws Exception {
        int databaseSizeBeforeUpdate = heroeRepository.findAll().size();

        // Create the Heroe
        HeroeDTO heroeDTO = heroeMapper.toDto(heroe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeroeMockMvc.perform(put("/api/heroes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(heroeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Heroe in the database
        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHeroe() throws Exception {
        // Initialize the database
        heroeRepository.saveAndFlush(heroe);

        int databaseSizeBeforeDelete = heroeRepository.findAll().size();

        // Delete the heroe
        restHeroeMockMvc.perform(delete("/api/heroes/{id}", heroe.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Heroe> heroeList = heroeRepository.findAll();
        assertThat(heroeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
