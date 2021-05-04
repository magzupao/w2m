package com.w2m.anime.web.rest;

import com.w2m.anime.service.HeroeService;
import com.w2m.anime.web.rest.errors.BadRequestAlertException;
import com.w2m.anime.service.dto.HeroeDTO;
import com.w2m.anime.service.dto.HeroeCriteria;
import com.w2m.anime.service.HeroeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.w2m.anime.domain.Heroe}.
 */
@RestController
@RequestMapping("/api")
public class HeroeResource {

    private final Logger log = LoggerFactory.getLogger(HeroeResource.class);

    private static final String ENTITY_NAME = "heroe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HeroeService heroeService;

    private final HeroeQueryService heroeQueryService;

    public HeroeResource(HeroeService heroeService, HeroeQueryService heroeQueryService) {
        this.heroeService = heroeService;
        this.heroeQueryService = heroeQueryService;
    }

    /**
     * {@code POST  /heroes} : Create a new heroe.
     *
     * @param heroeDTO the heroeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new heroeDTO, or with status {@code 400 (Bad Request)} if the heroe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/heroes")
    public ResponseEntity<HeroeDTO> createHeroe(@Valid @RequestBody HeroeDTO heroeDTO) throws URISyntaxException {
        log.debug("REST request to save Heroe : {}", heroeDTO);
        if (heroeDTO.getId() != null) {
            throw new BadRequestAlertException("A new heroe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HeroeDTO result = heroeService.save(heroeDTO);
        return ResponseEntity.created(new URI("/api/heroes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    @PostMapping("/heroes/like")
    public ResponseEntity<HeroeDTO> findLikeName(@Valid @RequestBody HeroeDTO heroeDTO) throws URISyntaxException {
        log.debug("REST request to save Heroe : {}", heroeDTO);
        if (heroeDTO.getId() != null) {
            throw new BadRequestAlertException("A new heroe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HeroeDTO result = heroeService.save(heroeDTO);
        return ResponseEntity.created(new URI("/api/heroes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }    

    /**
     * {@code PUT  /heroes} : Updates an existing heroe.
     *
     * @param heroeDTO the heroeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated heroeDTO,
     * or with status {@code 400 (Bad Request)} if the heroeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the heroeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/heroes")
    public ResponseEntity<HeroeDTO> updateHeroe(@Valid @RequestBody HeroeDTO heroeDTO) throws URISyntaxException {
        log.debug("REST request to update Heroe : {}", heroeDTO);
        if (heroeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HeroeDTO result = heroeService.save(heroeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, heroeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /heroes} : get all the heroes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of heroes in body.
     */
    @GetMapping("/heroes")
    public ResponseEntity<List<HeroeDTO>> getAllHeroes(HeroeCriteria criteria, Pageable pageable) {
        log.debug(" ************************************ REST request to get Heroes by criteria: {}", criteria);
        Page<HeroeDTO> page = heroeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /heroes/count} : count all the heroes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/heroes/count")
    public ResponseEntity<Long> countHeroes(HeroeCriteria criteria) {
        log.debug("REST request to count Heroes by criteria: {}", criteria);
        return ResponseEntity.ok().body(heroeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /heroes/:id} : get the "id" heroe.
     *
     * @param id the id of the heroeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the heroeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/heroes/{id}")
    public ResponseEntity<HeroeDTO> getHeroe(@PathVariable Long id) {
        log.debug("REST request to get Heroe : {}", id);
        Optional<HeroeDTO> heroeDTO = heroeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(heroeDTO);
    }

    /**
     * {@code DELETE  /heroes/:id} : delete the "id" heroe.
     *
     * @param id the id of the heroeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/heroes/{id}")
    public ResponseEntity<Void> deleteHeroe(@PathVariable Long id) {
        log.debug("REST request to delete Heroe : {}", id);
        heroeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
