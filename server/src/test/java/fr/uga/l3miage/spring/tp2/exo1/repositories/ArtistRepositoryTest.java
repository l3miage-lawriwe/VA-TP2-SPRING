package fr.uga.l3miage.spring.tp2.exo1.repositories;


import fr.uga.l3miage.exo1.enums.GenreMusical;
import fr.uga.l3miage.spring.tp2.exo1.models.ArtistEntity;
import fr.uga.l3miage.spring.tp2.exo1.models.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class ArtistRepositoryTest {
    @Autowired
    private ArtistRepository artistRepository;

    // a tester
    // int countByGenreMusicalEquals(GenreMusical genreMusical);


    @Test
    void testRequestcountByGenreMusicalEquals(){

        ArtistEntity artistEntity = ArtistEntity
                .builder()
                .name("testguy")
                .biography("né en 1987")
                .genreMusical(GenreMusical.valueOf("jazz"))
                .build();

        ArtistEntity artistEntity2 = ArtistEntity
                .builder()
                .name("testguy2")
                .biography("inventé google")
                .genreMusical(GenreMusical.valueOf("jazz"))
                .build();

        artistRepository.save(artistEntity);
        artistRepository.save(artistEntity2);

        //when

        int nb = artistRepository.countByGenreMusicalEquals(GenreMusical.valueOf("jazz"));

        //then
        //assertThat(nb).hasSize(1);
        //assertThat(nb.stream().findFirst().get().getMail()).isEqualTo("test@gmail.com");
    
        // PAS FINI DE CODER SA A FAIRE PLUS TARD
    }
}
