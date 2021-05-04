package com.w2m.anime.repository;

import com.w2m.anime.domain.Heroe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Heroe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeroeRepository extends JpaRepository<Heroe, Long>, JpaSpecificationExecutor<Heroe> {
    
    @Query(value = "select * from heroe where name %:name% ",
    countQuery = "select count(*) from heroe where name %:name% ", nativeQuery = true)
    Page<Heroe> findByLikeName(String name, Pageable pageable);
    
}
