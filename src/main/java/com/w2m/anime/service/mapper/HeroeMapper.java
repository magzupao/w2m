package com.w2m.anime.service.mapper;


import com.w2m.anime.domain.*;
import com.w2m.anime.service.dto.HeroeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Heroe} and its DTO {@link HeroeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HeroeMapper extends EntityMapper<HeroeDTO, Heroe> {



    default Heroe fromId(Long id) {
        if (id == null) {
            return null;
        }
        Heroe heroe = new Heroe();
        heroe.setId(id);
        return heroe;
    }
}
