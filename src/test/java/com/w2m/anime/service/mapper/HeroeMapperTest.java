package com.w2m.anime.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HeroeMapperTest {

    private HeroeMapper heroeMapper;

    @BeforeEach
    public void setUp() {
        heroeMapper = new HeroeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(heroeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(heroeMapper.fromId(null)).isNull();
    }
}
