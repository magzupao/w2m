package com.w2m.anime.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.w2m.anime.web.rest.TestUtil;

public class HeroeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HeroeDTO.class);
        HeroeDTO heroeDTO1 = new HeroeDTO();
        heroeDTO1.setId(1L);
        HeroeDTO heroeDTO2 = new HeroeDTO();
        assertThat(heroeDTO1).isNotEqualTo(heroeDTO2);
        heroeDTO2.setId(heroeDTO1.getId());
        assertThat(heroeDTO1).isEqualTo(heroeDTO2);
        heroeDTO2.setId(2L);
        assertThat(heroeDTO1).isNotEqualTo(heroeDTO2);
        heroeDTO1.setId(null);
        assertThat(heroeDTO1).isNotEqualTo(heroeDTO2);
    }
}
