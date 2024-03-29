package fr.uga.l3miage.spring.tp2.exo1.services;


import fr.uga.l3miage.exo1.requests.PlaylistCreationRequest;
import fr.uga.l3miage.exo1.response.PlaylistResponseDTO;
import fr.uga.l3miage.spring.tp2.exo1.components.PlaylistComponent;
import fr.uga.l3miage.spring.tp2.exo1.components.SongComponent;
import fr.uga.l3miage.spring.tp2.exo1.mappers.PlaylistMapper;
import fr.uga.l3miage.spring.tp2.exo1.models.PlaylistEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PlaylistServiceTest {
    @Autowired
    private PlaylistService playlistService;

    @MockBean
    private PlaylistComponent playlistComponent;

    @MockBean
    private SongComponent songComponent;

    @SpyBean
    private PlaylistMapper playlistMapper;

        /// le test echoue, problème
    @Test
    void createPlaylist() {
        //given
        PlaylistCreationRequest playlistCreationRequest = PlaylistCreationRequest
                .builder()
                .name("test")
                .description("Une description de test")
                .songsIds(Set.of())
                .build();

        PlaylistEntity playlistEntity = playlistMapper.toEntity(playlistCreationRequest);
        playlistEntity.setSongEntities(Set.of());
        when(songComponent.getSetSongEntity(same(Set.of()))).thenReturn(Set.of());
        when(playlistComponent.createPlaylistEntity(any(PlaylistEntity.class))).thenReturn(playlistEntity);

        PlaylistResponseDTO responseExpected = playlistMapper.toResponse(playlistEntity);

        //when
        PlaylistResponseDTO response = playlistService.createPlaylist(playlistCreationRequest);

        // then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
        verify(playlistMapper, times(2)).toEntity(playlistCreationRequest);
        verify(playlistMapper, times(2)).toResponse(same(playlistEntity));
        verify(playlistComponent, times(1)).createPlaylistEntity(any(PlaylistEntity.class));
        verify(songComponent, times(1)).getSetSongEntity(Set.of());
    }
}