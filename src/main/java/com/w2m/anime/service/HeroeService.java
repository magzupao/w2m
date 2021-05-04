package com.w2m.anime.service;

import com.w2m.anime.domain.Heroe;
import com.w2m.anime.repository.HeroeRepository;
import com.w2m.anime.service.dto.HeroeDTO;
import com.w2m.anime.service.mapper.HeroeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Heroe}.
 */
@Service
@Transactional
public class HeroeService {

    private final Logger log = LoggerFactory.getLogger(HeroeService.class);

    private final HeroeRepository heroeRepository;

    private final HeroeMapper heroeMapper;

    public HeroeService(HeroeRepository heroeRepository, HeroeMapper heroeMapper) {
        this.heroeRepository = heroeRepository;
        this.heroeMapper = heroeMapper;
    }

    /**
     * Save a heroe.
     *
     * @param heroeDTO the entity to save.
     * @return the persisted entity.
     */
    public HeroeDTO save(HeroeDTO heroeDTO) {
        log.debug("Request to save Heroe : {}", heroeDTO);
        Heroe heroe = heroeMapper.toEntity(heroeDTO);
        heroe = heroeRepository.save(heroe);
        return heroeMapper.toDto(heroe);
    }

    /**
     * Get all the heroes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HeroeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Heroes");
        return heroeRepository.findAll(pageable)
            .map(heroeMapper::toDto);
    }


    /**
     * Get one heroe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HeroeDTO> findOne(Long id) {
        log.debug("Request to get Heroe : {}", id);
        return heroeRepository.findById(id)
            .map(heroeMapper::toDto);
    }

    /**
     * Delete the heroe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Heroe : {}", id);
        heroeRepository.deleteById(id);
    }
}
