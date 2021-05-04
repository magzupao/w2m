package com.w2m.anime.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.w2m.anime.domain.Heroe;
import com.w2m.anime.domain.*; // for static metamodels
import com.w2m.anime.repository.HeroeRepository;
import com.w2m.anime.service.dto.HeroeCriteria;
import com.w2m.anime.service.dto.HeroeDTO;
import com.w2m.anime.service.mapper.HeroeMapper;

/**
 * Service for executing complex queries for {@link Heroe} entities in the database.
 * The main input is a {@link HeroeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HeroeDTO} or a {@link Page} of {@link HeroeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HeroeQueryService extends QueryService<Heroe> {

    private final Logger log = LoggerFactory.getLogger(HeroeQueryService.class);

    private final HeroeRepository heroeRepository;

    private final HeroeMapper heroeMapper;

    public HeroeQueryService(HeroeRepository heroeRepository, HeroeMapper heroeMapper) {
        this.heroeRepository = heroeRepository;
        this.heroeMapper = heroeMapper;
    }

    /**
     * Return a {@link List} of {@link HeroeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HeroeDTO> findByCriteria(HeroeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Heroe> specification = createSpecification(criteria);
        return heroeMapper.toDto(heroeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HeroeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HeroeDTO> findByCriteria(HeroeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Heroe> specification = createSpecification(criteria);
        return heroeRepository.findAll(specification, page)
            .map(heroeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HeroeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Heroe> specification = createSpecification(criteria);
        return heroeRepository.count(specification);
    }

    /**
     * Function to convert {@link HeroeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Heroe> createSpecification(HeroeCriteria criteria) {
        Specification<Heroe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Heroe_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Heroe_.name));
            }
        }
        return specification;
    }
}
