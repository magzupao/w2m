package com.w2m.anime.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.w2m.anime.web.rest.TestUtil;

public class HeroeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Heroe.class);
        Heroe heroe1 = new Heroe();
        heroe1.setId(1L);
        Heroe heroe2 = new Heroe();
        heroe2.setId(heroe1.getId());
        assertThat(heroe1).isEqualTo(heroe2);
        heroe2.setId(2L);
        assertThat(heroe1).isNotEqualTo(heroe2);
        heroe1.setId(null);
        assertThat(heroe1).isNotEqualTo(heroe2);
    }
}
