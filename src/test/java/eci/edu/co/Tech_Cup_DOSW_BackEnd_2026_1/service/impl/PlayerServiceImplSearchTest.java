package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.PlayerServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.PhotoValidationUtil;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.SportProfileEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerServiceImpl Search Tests")
class PlayerServiceImplSearchTest {

    @Mock
    private SportProfileRepository sportProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SportProfileMapper sportProfileMapper;

    @Mock
    private PhotoValidationUtil photoValidationUtil;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private SportProfile player1;
    private SportProfile player2;
    private SportProfileEntity entity1;
    private SportProfileEntity entity2;
    private User user1;
    private User user2;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        user1 = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .username("juanperez")
                .email("juan@example.com")
                .build();

        user2 = User.builder()
                .id(2L)
                .firstName("María")
                .lastName("García")
                .username("mariagarcia")
                .email("maria@example.com")
                .build();

        userEntity1 = UserEntity.builder()
                .id(1L).firstName("Juan").lastName("Pérez").username("juanperez").email("juan@example.com").build();
        userEntity2 = UserEntity.builder()
                .id(2L).firstName("María").lastName("García").username("mariagarcia").email("maria@example.com").build();

        player1 = SportProfile.builder()
                .id(1L)
                .userId(1L)
                .position(Position.FORWARD)
                .available(true)
                .jerseyNumber(9)
                .build();

        player2 = SportProfile.builder()
                .id(2L)
                .userId(2L)
                .position(Position.DEFENDER)
                .available(true)
                .jerseyNumber(4)
                .build();

        entity1 = SportProfileEntity.builder()
                .id(1L).userId(1L).position(Position.FORWARD).available(true).jerseyNumber(9).build();
        entity2 = SportProfileEntity.builder()
                .id(2L).userId(2L).position(Position.DEFENDER).available(true).jerseyNumber(4).build();
    }

    @Test
    @DisplayName("Should search players by position successfully")
    void testSearchPlayersByPosition() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(Arrays.asList(entity1), pageable, 1);
        when(sportProfileRepository.searchAvailablePlayers(
                Position.FORWARD, null, null, null, null, pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(userPersistenceMapper.toModel(userEntity1)).thenReturn(user1);
        when(sportProfileMapper.toResponse(player1)).thenReturn(
                ProfileResponse.builder().id(1L).userId(1L).position(Position.FORWARD).build());

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                Position.FORWARD, null, null, null, null, pageable);

        assertThat(result)
                .isNotNull()
                .extracting("totalElements", "totalPages", "currentPage", "pageSize")
                .containsExactly(1L, 1, 0, 10);
        assertThat(result.getPlayers()).hasSize(1);

        verify(sportProfileRepository).searchAvailablePlayers(
                Position.FORWARD, null, null, null, null, pageable);
    }

    @Test
    @DisplayName("Should get all available players when no filters provided")
    void testSearchAllAvailablePlayers() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(
                Arrays.asList(entity1, entity2), pageable, 2);
        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userPersistenceMapper.toModel(entity2)).thenReturn(player2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity2));
        when(userPersistenceMapper.toModel(userEntity1)).thenReturn(user1);
        when(userPersistenceMapper.toModel(userEntity2)).thenReturn(user2);
        when(sportProfileMapper.toResponse(player1)).thenReturn(
                ProfileResponse.builder().id(1L).userId(1L).build());
        when(sportProfileMapper.toResponse(player2)).thenReturn(
                ProfileResponse.builder().id(2L).userId(2L).build());

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getPlayers()).hasSize(2);
        verify(sportProfileRepository).searchAvailablePlayers(null, null, null, null, null, pageable);
    }

    @Test
    @DisplayName("Should return empty results when no players match filters")
    void testSearchPlayersEmptyResults() {
        Page<SportProfileEntity> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        when(sportProfileRepository.searchAvailablePlayers(
                Position.GOALKEEPER, null, null, null, null, pageable))
                .thenReturn(emptyPage);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                Position.GOALKEEPER, null, null, null, null, pageable);

        assertThat(result.getPlayers()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.isLastPage()).isTrue();
    }

    @Test
    @DisplayName("Should map player names correctly in search results")
    void testSearchPlayersMapPlayerNames() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(
                Arrays.asList(entity1, entity2), pageable, 2);
        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userPersistenceMapper.toModel(entity2)).thenReturn(player2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity2));
        when(userPersistenceMapper.toModel(userEntity1)).thenReturn(user1);
        when(userPersistenceMapper.toModel(userEntity2)).thenReturn(user2);

        ProfileResponse response1 = ProfileResponse.builder().id(1L).userId(1L).build();
        ProfileResponse response2 = ProfileResponse.builder().id(2L).userId(2L).build();

        when(sportProfileMapper.toResponse(player1)).thenReturn(response1);
        when(sportProfileMapper.toResponse(player2)).thenReturn(response2);

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, pageable);

        assertThat(result.getPlayers()).hasSize(2);
        assertThat(result.getPlayers().get(0).getPlayerName()).isEqualTo("Juan Pérez");
        assertThat(result.getPlayers().get(1).getPlayerName()).isEqualTo("María García");
    }

    @Test
    @DisplayName("Should include pagination info in response")
    void testSearchResponseIncludesPaginationInfo() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(
                Arrays.asList(entity1, entity2), pageable, 25);
        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userPersistenceMapper.toModel(entity2)).thenReturn(player2);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity1));
        when(userPersistenceMapper.toModel(userEntity1)).thenReturn(user1);
        when(sportProfileMapper.toResponse(any(SportProfile.class))).thenReturn(
                ProfileResponse.builder().id(1L).build());

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, pageable);

        assertThat(result)
                .extracting("currentPage", "pageSize", "totalElements", "totalPages", "isFirstPage", "isLastPage")
                .containsExactly(0, 10, 25L, 3, true, false);
        assertThat(result.isHasNextPage()).isTrue();
    }

    @Test
    @DisplayName("Should search with multiple filters")
    void testSearchPlayersWithMultipleFilters() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(Arrays.asList(entity1), pageable, 1);
        when(sportProfileRepository.searchAvailablePlayers(
                Position.FORWARD, 5, 20, "M", "Juan", pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(userPersistenceMapper.toModel(userEntity1)).thenReturn(user1);
        when(sportProfileMapper.toResponse(player1)).thenReturn(
                ProfileResponse.builder().id(1L).userId(1L).build());

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                Position.FORWARD, 5, 20, "M", "Juan", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(sportProfileRepository).searchAvailablePlayers(
                Position.FORWARD, 5, 20, "M", "Juan", pageable);
    }

    @Test
    @DisplayName("Should handle user not found when mapping player names")
    void testSearchPlayersUserNotFound() {
        Page<SportProfileEntity> playersPage = new PageImpl<>(Arrays.asList(entity1), pageable, 1);
        when(sportProfileRepository.searchAvailablePlayers(null, null, null, null, null, pageable))
                .thenReturn(playersPage);
        when(userPersistenceMapper.toModel(entity1)).thenReturn(player1);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(sportProfileMapper.toResponse(player1)).thenReturn(
                ProfileResponse.builder().id(1L).userId(1L).build());

        PlayerSearchResponse result = playerService.searchAvailablePlayers(
                null, null, null, null, null, pageable);

        assertThat(result.getPlayers()).hasSize(1);
        assertThat(result.getPlayers().get(0).getPlayerName()).isNull();
    }
}
