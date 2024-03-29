package fr.uga.l3miage.spring.tp2.exo1.Controllers;

import fr.uga.l3miage.exo1.errors.NotFoundErrorResponse;
import fr.uga.l3miage.exo1.requests.PlaylistCreationRequest;
import fr.uga.l3miage.exo1.response.PlaylistResponseDTO;
import fr.uga.l3miage.spring.tp2.exo1.components.PlaylistComponent;
import fr.uga.l3miage.spring.tp2.exo1.components.SongComponent;
import fr.uga.l3miage.spring.tp2.exo1.models.PlaylistEntity;
import fr.uga.l3miage.spring.tp2.exo1.repositories.PlaylistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class PlaylistControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PlaylistRepository playlistRepository;

    @SpyBean
    private PlaylistComponent playlistComponent;

    @SpyBean
    private SongComponent songComponent;

    //"si des données sont en base, elles ne sont pas supprimées entre 2 tests de la même classe. C'est pour cela qu'on va aussi définir une méthode qui va s'exécuter après chaque test d'une classe grâce à l'annotation @AfterEach"
    // en gros on fait du clean up après chaque test
    @AfterEach
    public void clear() {
        playlistRepository.deleteAll();
    }



    @Test
    void canCreatePlaylistWithoutSong() {
        //given
        final HttpHeaders headers = new HttpHeaders();

        final PlaylistCreationRequest request = PlaylistCreationRequest
                .builder()
                .name("Test playlist")
                .description("une playlist de test")
                .songsIds(Set.of())
                .build();


        // when
        ResponseEntity<PlaylistResponseDTO> response = testRestTemplate.exchange("/api/playlist/create", HttpMethod.POST, new HttpEntity<>(request, headers), PlaylistResponseDTO.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(playlistRepository.count()).isEqualTo(1);
        verify(playlistComponent, times(1)).createPlaylistEntity(any(PlaylistEntity.class));
        verify(songComponent, times(1)).getSetSongEntity(Set.of());
    }

    @Test
    void getNotFoundPlaylist() {
        //Given
        final HttpHeaders headers = new HttpHeaders();

        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("idPlaylist", "ma playlist qui n'existe pas");

        NotFoundErrorResponse notFoundErrorResponseExpected = NotFoundErrorResponse
                .builder()
                .uri("/api/playlist/ma%20playlist%20qui%20n%27existe%20pas")
                .errorMessage("La playlist [ma playlist qui n'existe pas] n'a pas été trouvé")
                .build();

        //when
        ResponseEntity<NotFoundErrorResponse> response = testRestTemplate.exchange("/api/playlist/{idPlaylist}", HttpMethod.GET, new HttpEntity<>(null, headers), NotFoundErrorResponse.class, urlParams);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).usingRecursiveComparison()
                .isEqualTo(notFoundErrorResponseExpected);
    }
}