package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ActiveTournamentInfoResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentBracketResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentStandingRowResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTopScorerResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMatchHistoryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMonthlyPerformanceResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Court;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.TournamentDate;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TournamentServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.ChangeStatusRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentConfigRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentSetupRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentDateEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentRulesConfirmationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TournamentPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.EliminationBracketRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRulesConfirmationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TournamentServiceImpl Tests")
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private TournamentDateRepository tournamentDateRepository;

    @Mock
    private StandingRepository standingRepository;

    @Mock
    private EliminationBracketRepository eliminationBracketRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TournamentMapper tournamentMapper;

    @Mock
    private TournamentRequestValidator tournamentRequestValidator;

    @Mock
    private TournamentConfigRequestValidator tournamentConfigRequestValidator;

    @Mock
    private TournamentSetupRequestValidator tournamentSetupRequestValidator;

    @Mock
    private ChangeStatusRequestValidator changeStatusRequestValidator;

    @Mock
    private TournamentPersistenceMapper tournamentPersistenceMapper;

    @Mock
    private MatchEventRepository matchEventRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TournamentRulesConfirmationRepository tournamentRulesConfirmationRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private TournamentRequest request;
    private Tournament model;
    private TournamentEntity entity;

    @BeforeEach
    void setUp() {
        request = TournamentRequest.builder()
                .name("Copa DOSW")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 6, 1))
                .teamCount(8)
                .costPerTeam(100000)
                .build();

        model = Tournament.builder()
                .id(1L)
                .name("Copa DOSW")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 6, 1))
                .teamCount(8)
                .costPerTeam(100000)
                .status(TournamentStatus.DRAFT)
                .build();

        entity = TournamentEntity.builder()
                .id(1L)
                .name("Copa DOSW")
                .build();
    }

    @Test
    @DisplayName("create debe guardar y responder correctamente")
    void createSuccess() {
        TournamentResponse expected = TournamentResponse.builder().id(1L).name("Copa DOSW").build();

        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(entity);
        when(tournamentRepository.save(entity)).thenReturn(entity);
        when(tournamentPersistenceMapper.toModel(entity)).thenReturn(model);
        when(tournamentMapper.toResponse(model)).thenReturn(expected);

        TournamentResponse result = tournamentService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tournamentRequestValidator).validate(request);
        verify(tournamentRepository).save(entity);
    }

    @Test
    @DisplayName("getById debe lanzar not found cuando no existe")
    void getByIdNotFound() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getById(99L));
    }

    @Test
    @DisplayName("changeStatus debe persistir nuevo estado")
    void changeStatusSuccess() {
        ChangeStatusRequest changeStatusRequest = ChangeStatusRequest.builder()
                .status(TournamentStatus.ACTIVE)
                .build();

        Tournament activeModel = Tournament.builder()
                .id(1L)
                .name("Copa DOSW")
                .status(TournamentStatus.ACTIVE)
                .build();

        TournamentResponse expected = TournamentResponse.builder().id(1L).status(TournamentStatus.ACTIVE).build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tournamentPersistenceMapper.toModel(entity)).thenReturn(model, activeModel);
        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(entity);
        when(tournamentRepository.save(entity)).thenReturn(entity);
        when(tournamentMapper.toResponse(activeModel)).thenReturn(expected);

        TournamentResponse result = tournamentService.changeStatus(1L, changeStatusRequest);

        assertEquals(TournamentStatus.ACTIVE, result.getStatus());
        verify(changeStatusRequestValidator).validate(TournamentStatus.DRAFT, TournamentStatus.ACTIVE);
        verify(tournamentRepository).save(entity);
    }

    @Test
    @DisplayName("setup debe guardar canchas y fechas")
    void setupSuccess() {
        TournamentSetupRequest setupRequest = TournamentSetupRequest.builder()
                .rules("Reglas")
                .sanctionRules("Sanciones")
                .inscriptionCloseDate(LocalDate.of(2026, 3, 20))
                .courts(List.of(CourtRequest.builder().name("Cancha A").location("Bloque A").build()))
                .schedule(List.of(
                        TournamentDateRequest.builder().description("J1").eventDate(LocalDate.of(2026, 4, 10)).build()))
                .build();

        CourtEntity courtEntity = CourtEntity.builder().id(10L).name("Cancha A").location("Bloque A").build();
        TournamentDateEntity dateEntity = TournamentDateEntity.builder().id(20L).description("J1")
                .eventDate(LocalDate.of(2026, 4, 10)).build();

        Court courtModel = Court.builder().id(10L).name("Cancha A").location("Bloque A").build();
        TournamentDate dateModel = TournamentDate.builder().id(20L).description("J1")
                .eventDate(LocalDate.of(2026, 4, 10)).build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tournamentPersistenceMapper.toModel(entity)).thenReturn(model);
        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(entity);
        when(tournamentRepository.save(entity)).thenReturn(entity);

        when(courtRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
        when(courtRepository.saveAll(anyList())).thenReturn(List.of(courtEntity));
        when(tournamentPersistenceMapper.toEntity(any(Court.class))).thenReturn(courtEntity);
        when(tournamentPersistenceMapper.toModel(courtEntity)).thenReturn(courtModel);

        when(tournamentDateRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
        when(tournamentDateRepository.saveAll(anyList())).thenReturn(List.of(dateEntity));
        when(tournamentPersistenceMapper.toEntity(any(TournamentDate.class))).thenReturn(dateEntity);
        when(tournamentPersistenceMapper.toModel(dateEntity)).thenReturn(dateModel);

        TournamentSetupResponse result = tournamentService.setup(1L, setupRequest);

        assertNotNull(result);
        assertEquals(1, result.getCourts().size());
        assertEquals(1, result.getSchedule().size());
        verify(tournamentSetupRequestValidator).validate(setupRequest);
        verify(courtRepository).deleteAll(eq(Collections.emptyList()));
    }

    @Test
    @DisplayName("delete debe lanzar not found cuando no existe")
    void deleteNotFound() {
        when(tournamentRepository.existsById(33L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.delete(33L));
        verify(tournamentRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("configure debe actualizar parámetros del torneo")
    void configureSuccess() {
        TournamentConfigRequest configRequest = TournamentConfigRequest.builder()
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 7, 1))
                .teamCount(16)
                .costPerTeam(150000)
                .build();

        Tournament configuredModel = Tournament.builder()
                .id(1L)
                .name("Copa DOSW")
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 7, 1))
                .teamCount(16)
                .costPerTeam(150000)
                .status(TournamentStatus.DRAFT)
                .build();

        TournamentResponse expected = TournamentResponse.builder().id(1L).name("Copa DOSW").build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tournamentPersistenceMapper.toModel(entity)).thenReturn(model, configuredModel);
        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(entity);
        when(tournamentRepository.save(entity)).thenReturn(entity);
        when(tournamentMapper.toResponse(configuredModel)).thenReturn(expected);

        TournamentResponse result = tournamentService.configure(1L, configRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tournamentConfigRequestValidator).validate(configRequest);
        verify(tournamentRepository).save(entity);
    }

    @Test
    @DisplayName("configure debe lanzar not found cuando torneo no existe")
    void configureNotFound() {
        TournamentConfigRequest configRequest = TournamentConfigRequest.builder()
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 7, 1))
                .teamCount(16)
                .costPerTeam(150000)
                .build();

        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.configure(99L, configRequest));
    }

    @Test
    @DisplayName("getActiveTournamentInfo debe retornar información del torneo activo")
    void getActiveTournamentInfoSuccess() {
        when(tournamentRepository.findTopByStatusInOrderByIdDesc(any())).thenReturn(Optional.of(entity));
        when(teamRepository.countByTournamentId(1L)).thenReturn(8L);
        when(courtRepository.countByTournamentId(1L)).thenReturn(4L);

        ActiveTournamentInfoResponse result = tournamentService.getActiveTournamentInfo();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(8L, result.getRegisteredTeams());
        assertEquals(4L, result.getAvailableCourts());
    }

    @Test
    @DisplayName("getActiveTournamentInfo debe lanzar excepción cuando no hay torneo activo")
    void getActiveTournamentInfoNotFound() {
        when(tournamentRepository.findTopByStatusInOrderByIdDesc(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getActiveTournamentInfo());
    }

    @Test
    @DisplayName("getTournamentStandings debe retornar tabla de posiciones")
    void getTournamentStandingsSuccess() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        StandingEntity standing = StandingEntity.builder()
                .id(1L)
                .team(team1)
                .played(5)
                .won(3)
                .drawn(1)
                .lost(1)
                .goalsFor(10)
                .goalsAgainst(5)
                .goalDifference(5)
                .points(10)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(standingRepository.findByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(1L))
                .thenReturn(Collections.singletonList(standing));

        List<TournamentStandingRowResponse> result = tournamentService.getTournamentStandings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPos());
        assertEquals(10L, result.get(0).getTeamId());
    }

    @Test
    @DisplayName("getTournamentStandings debe lanzar excepción cuando torneo no existe")
    void getTournamentStandingsNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentStandings(99L));
    }

    @Test
    @DisplayName("getTournamentBracket debe retornar bracket del torneo")
    void getTournamentBracketSuccess() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(eliminationBracketRepository.findByTournamentIdOrderByRoundAscMatchPositionAsc(1L))
                .thenReturn(Collections.emptyList());

        TournamentBracketResponse result = tournamentService.getTournamentBracket(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTournamentId());
    }

    @Test
    @DisplayName("getTournamentBracket debe lanzar excepción cuando torneo no existe")
    void getTournamentBracketNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentBracket(99L));
    }

    @Test
    @DisplayName("getTournamentCards debe retornar tarjetas del torneo")
    void getTournamentCardsSuccess() {
        UserEntity player1 = UserEntity.builder().id(1L).firstName("Juan").lastName("Pérez").username("juan.perez").build();
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        MatchEntity match1 = MatchEntity.builder().id(100L).matchDate(LocalDate.of(2026, 4, 10)).matchTime(LocalTime.of(14, 0)).build();

        MatchEventEntity yellowCard = MatchEventEntity.builder()
                .id(1L)
                .eventType(MatchEventType.YELLOW_CARD)
                .player(player1)
                .match(match1)
                .minute(45)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.YELLOW_CARD, MatchEventType.RED_CARD)))
                .thenReturn(List.of(yellowCard));

        List<TournamentCardEventResponse> result = tournamentService.getTournamentCards(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AMARILLA", result.get(0).getType());
        assertEquals("Juan Pérez", result.get(0).getPlayer());
        assertEquals(45, result.get(0).getMinute());
    }

    @Test
    @DisplayName("getTournamentCards debe retornar lista vacía cuando no hay tarjetas")
    void getTournamentCardsEmpty() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.YELLOW_CARD, MatchEventType.RED_CARD)))
                .thenReturn(Collections.emptyList());

        List<TournamentCardEventResponse> result = tournamentService.getTournamentCards(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getTournamentCards debe lanzar excepción cuando torneo no existe")
    void getTournamentCardsNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentCards(99L));
    }

    @Test
    @DisplayName("getTournamentCards debe mapear tarjeta roja correctamente")
    void getTournamentCardsRedCard() {
        UserEntity player1 = UserEntity.builder().id(2L).firstName("Carlos").lastName("García").username("carlos.garcia").build();
        MatchEntity match1 = MatchEntity.builder().id(101L).matchDate(LocalDate.of(2026, 4, 11)).matchTime(LocalTime.of(15, 30)).build();

        MatchEventEntity redCard = MatchEventEntity.builder()
                .id(2L)
                .eventType(MatchEventType.RED_CARD)
                .player(player1)
                .match(match1)
                .minute(80)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.YELLOW_CARD, MatchEventType.RED_CARD)))
                .thenReturn(List.of(redCard));

        List<TournamentCardEventResponse> result = tournamentService.getTournamentCards(1L);

        assertEquals(1, result.size());
        assertEquals("ROJA", result.get(0).getType());
    }

    @Test
    @DisplayName("getTournamentCards debe manejar player nulo")
    void getTournamentCardsNullPlayer() {
        MatchEntity match1 = MatchEntity.builder().id(102L).matchDate(LocalDate.of(2026, 4, 12)).matchTime(LocalTime.of(16, 0)).build();

        MatchEventEntity cardEvent = MatchEventEntity.builder()
                .id(3L)
                .eventType(MatchEventType.YELLOW_CARD)
                .player(null)
                .match(match1)
                .minute(25)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.YELLOW_CARD, MatchEventType.RED_CARD)))
                .thenReturn(List.of(cardEvent));

        List<TournamentCardEventResponse> result = tournamentService.getTournamentCards(1L);

        assertEquals(1, result.size());
        assertEquals("N/A", result.get(0).getPlayer());
    }

    @Test
    @DisplayName("getTournamentTopScorers debe retornar goleadores ordenados por goles")
    void getTournamentTopScorersSuccess() {
        UserEntity player1 = UserEntity.builder().id(1L).firstName("Juan").lastName("Pérez").username("juan.perez").avatarUrl("http://avatar.jpg").build();
        UserEntity player2 = UserEntity.builder().id(2L).firstName("Carlos").lastName("García").username("carlos.garcia").avatarUrl("http://avatar2.jpg").build();
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEventEntity goal1 = MatchEventEntity.builder().id(1L).eventType(MatchEventType.GOAL).player(player1).team(team1).build();
        MatchEventEntity goal2 = MatchEventEntity.builder().id(2L).eventType(MatchEventType.GOAL).player(player1).team(team1).build();
        MatchEventEntity goal3 = MatchEventEntity.builder().id(3L).eventType(MatchEventType.GOAL).player(player2).team(team2).build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.GOAL)))
                .thenReturn(List.of(goal1, goal2, goal3));

        List<TournamentTopScorerResponse> result = tournamentService.getTournamentTopScorers(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getGoals());
        assertEquals(1, result.get(1).getGoals());
    }

    @Test
    @DisplayName("getTournamentTopScorers debe retornar lista vacía cuando no hay goles")
    void getTournamentTopScorersEmpty() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.GOAL)))
                .thenReturn(Collections.emptyList());

        List<TournamentTopScorerResponse> result = tournamentService.getTournamentTopScorers(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getTournamentTopScorers debe lanzar excepción cuando torneo no existe")
    void getTournamentTopScorersNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentTopScorers(99L));
    }

    @Test
    @DisplayName("getTournamentTopScorers debe manejar eventos con player nulo")
    void getTournamentTopScorersNullPlayer() {
        UserEntity player1 = UserEntity.builder().id(1L).firstName("Juan").lastName("Pérez").username("juan.perez").build();
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();

        MatchEventEntity goalWithPlayer = MatchEventEntity.builder().id(1L).eventType(MatchEventType.GOAL).player(player1).team(team1).build();
        MatchEventEntity goalWithoutPlayer = MatchEventEntity.builder().id(2L).eventType(MatchEventType.GOAL).player(null).team(team1).build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.GOAL)))
                .thenReturn(List.of(goalWithPlayer, goalWithoutPlayer));

        List<TournamentTopScorerResponse> result = tournamentService.getTournamentTopScorers(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getTournamentTopScorers debe actualizar equipo cuando se encuentra en otro evento")
    void getTournamentTopScorersUpdateTeam() {
        UserEntity player1 = UserEntity.builder().id(1L).firstName("Juan").lastName("Pérez").username("juan.perez").build();
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEventEntity goal1 = MatchEventEntity.builder().id(1L).eventType(MatchEventType.GOAL).player(player1).team(null).build();
        MatchEventEntity goal2 = MatchEventEntity.builder().id(2L).eventType(MatchEventType.GOAL).player(player1).team(team1).build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchEventRepository.findTournamentEventsByTypesOrderByChronology(1L, List.of(MatchEventType.GOAL)))
                .thenReturn(List.of(goal1, goal2));

        List<TournamentTopScorerResponse> result = tournamentService.getTournamentTopScorers(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getTeamId());
        assertEquals("Equipo A", result.get(0).getTeam());
    }

    @Test
    @DisplayName("getTournamentMatchHistory debe retornar historial de partidos jugados")
    void getTournamentMatchHistorySuccess() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity match1 = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(2)
                .awayScore(1)
                .matchDate(LocalDate.of(2026, 4, 10))
                .matchTime(LocalTime.of(14, 0))
                .phase(MatchPhase.GROUP)
                .played(true)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match1));

        List<TournamentMatchHistoryResponse> result = tournamentService.getTournamentMatchHistory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getHomeScore());
        assertEquals(1, result.get(0).getAwayScore());
    }

    @Test
    @DisplayName("getTournamentMatchHistory debe filtrar solo partidos jugados")
    void getTournamentMatchHistoryFiltersPlayed() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity playedMatch = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(2)
                .awayScore(1)
                .matchDate(LocalDate.of(2026, 4, 10))
                .matchTime(LocalTime.of(14, 0))
                .played(true)
                .build();

        MatchEntity unplayedMatch = MatchEntity.builder()
                .id(2L)
                .homeTeam(team1)
                .awayTeam(team2)
                .matchDate(LocalDate.of(2026, 4, 11))
                .matchTime(LocalTime.of(15, 0))
                .played(false)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(playedMatch, unplayedMatch));

        List<TournamentMatchHistoryResponse> result = tournamentService.getTournamentMatchHistory(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMatchId());
    }

    @Test
    @DisplayName("getTournamentMatchHistory debe retornar lista vacía cuando no hay partidos jugados")
    void getTournamentMatchHistoryEmpty() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());

        List<TournamentMatchHistoryResponse> result = tournamentService.getTournamentMatchHistory(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getTournamentMatchHistory debe lanzar excepción cuando torneo no existe")
    void getTournamentMatchHistoryNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentMatchHistory(99L));
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe retornar desempeño mensual de equipos")
    void getTournamentMonthlyPerformanceSuccess() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity match1 = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(3)
                .awayScore(0)
                .matchDate(LocalDate.of(2026, 4, 10))
                .played(true)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findByTournamentId(1L)).thenReturn(List.of(team1, team2));
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match1));

        List<TournamentMonthlyPerformanceResponse> result = tournamentService.getTournamentMonthlyPerformance(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe retornar lista vacía cuando no hay equipos")
    void getTournamentMonthlyPerformanceEmpty() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
        when(matchRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());

        List<TournamentMonthlyPerformanceResponse> result = tournamentService.getTournamentMonthlyPerformance(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe lanzar excepción cuando torneo no existe")
    void getTournamentMonthlyPerformanceNotFound() {
        when(tournamentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getTournamentMonthlyPerformance(99L));
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe calcular puntos correctamente para victorias")
    void getTournamentMonthlyPerformanceWinLogic() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity match1 = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(2)
                .awayScore(0)
                .matchDate(LocalDate.of(2026, 4, 10))
                .played(true)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findByTournamentId(1L)).thenReturn(List.of(team1, team2));
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match1));

        List<TournamentMonthlyPerformanceResponse> result = tournamentService.getTournamentMonthlyPerformance(1L);

        TournamentMonthlyPerformanceResponse team1Performance = result.stream()
                .filter(t -> t.getTeamId().equals(10L))
                .findFirst()
                .orElse(null);

        assertNotNull(team1Performance);
        assertEquals(3, team1Performance.getMonthly().stream()
                .filter(m -> m.getMonth() == 4)
                .findFirst()
                .map(TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint::getPoints)
                .orElse(0));
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe calcular puntos correctamente para empates")
    void getTournamentMonthlyPerformanceDrawLogic() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity match1 = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(1)
                .awayScore(1)
                .matchDate(LocalDate.of(2026, 4, 10))
                .played(true)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findByTournamentId(1L)).thenReturn(List.of(team1, team2));
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match1));

        List<TournamentMonthlyPerformanceResponse> result = tournamentService.getTournamentMonthlyPerformance(1L);

        TournamentMonthlyPerformanceResponse team1Performance = result.stream()
                .filter(t -> t.getTeamId().equals(10L))
                .findFirst()
                .orElse(null);

        assertNotNull(team1Performance);
        assertEquals(1, team1Performance.getMonthly().stream()
                .filter(m -> m.getMonth() == 4)
                .findFirst()
                .map(TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint::getPoints)
                .orElse(0));
    }

    @Test
    @DisplayName("getTournamentMonthlyPerformance debe ignorar meses fuera del rango 1-4")
    void getTournamentMonthlyPerformanceOutOfRangeMonth() {
        TeamEntity team1 = TeamEntity.builder().id(10L).name("Equipo A").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Equipo B").build();

        MatchEntity match1 = MatchEntity.builder()
                .id(1L)
                .homeTeam(team1)
                .awayTeam(team2)
                .homeScore(2)
                .awayScore(0)
                .matchDate(LocalDate.of(2026, 5, 10))
                .played(true)
                .build();

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(teamRepository.findByTournamentId(1L)).thenReturn(List.of(team1, team2));
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match1));

        List<TournamentMonthlyPerformanceResponse> result = tournamentService.getTournamentMonthlyPerformance(1L);

        TournamentMonthlyPerformanceResponse team1Performance = result.stream()
                .filter(t -> t.getTeamId().equals(10L))
                .findFirst()
                .orElse(null);

        assertNotNull(team1Performance);
        assertEquals(0, team1Performance.getMonthly().stream()
                .filter(m -> m.getMonth() == 5)
                .findFirst()
                .map(TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint::getPoints)
                .orElse(0));
    }

    @Test
    @DisplayName("confirmTournamentRules debe crear confirmación nueva cuando no existe")
    void confirmTournamentRulesCreateNew() {
        UserEntity user = UserEntity.builder().id(1L).email("user@example.com").username("user").build();
        TournamentEntity tournament = TournamentEntity.builder().id(1L).name("Copa DOSW").build();

        TournamentRulesConfirmationEntity savedConfirmation = TournamentRulesConfirmationEntity.builder()
                .id(1L)
                .tournament(tournament)
                .user(user)
                .confirmedAt(LocalDateTime.now())
                .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tournamentRulesConfirmationRepository.findByTournamentIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(tournamentRulesConfirmationRepository.save(any(TournamentRulesConfirmationEntity.class))).thenReturn(savedConfirmation);

        var result = tournamentService.confirmTournamentRules(1L, "user@example.com");

        assertNotNull(result);
        assertEquals(1L, result.getTournamentId());
        assertEquals(1L, result.getUserId());
        assertTrue(result.isRulesConfirmed());
    }

    @Test
    @DisplayName("confirmTournamentRules debe actualizar confirmación existente")
    void confirmTournamentRulesUpdate() {
        UserEntity user = UserEntity.builder().id(1L).email("user@example.com").username("user").build();
        TournamentEntity tournament = TournamentEntity.builder().id(1L).name("Copa DOSW").build();

        LocalDateTime oldTime = LocalDateTime.of(2026, 3, 1, 10, 0);
        TournamentRulesConfirmationEntity existingConfirmation = TournamentRulesConfirmationEntity.builder()
                .id(1L)
                .tournament(tournament)
                .user(user)
                .confirmedAt(oldTime)
                .build();

        TournamentRulesConfirmationEntity updatedConfirmation = TournamentRulesConfirmationEntity.builder()
                .id(1L)
                .tournament(tournament)
                .user(user)
                .confirmedAt(LocalDateTime.now())
                .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tournamentRulesConfirmationRepository.findByTournamentIdAndUserId(1L, 1L)).thenReturn(Optional.of(existingConfirmation));
        when(tournamentRulesConfirmationRepository.save(any(TournamentRulesConfirmationEntity.class))).thenReturn(updatedConfirmation);

        var result = tournamentService.confirmTournamentRules(1L, "user@example.com");

        assertNotNull(result);
        assertTrue(result.isRulesConfirmed());
        verify(tournamentRulesConfirmationRepository).save(any());
    }

    @Test
    @DisplayName("confirmTournamentRules debe lanzar excepción cuando torneo no existe")
    void confirmTournamentRulesNotFound() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.confirmTournamentRules(99L, "user@example.com"));
    }

    @Test
    @DisplayName("confirmTournamentRules debe lanzar excepción cuando usuario no existe por email")
    void confirmTournamentRulesUserNotFoundByEmail() {
        TournamentEntity tournament = TournamentEntity.builder().id(1L).name("Copa DOSW").build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.confirmTournamentRules(1L, "nonexistent@example.com"));
    }

    @Test
    @DisplayName("confirmTournamentRules debe buscar por username cuando email no existe")
    void confirmTournamentRulesFallbackToUsername() {
        UserEntity user = UserEntity.builder().id(1L).email("user@example.com").username("myusername").build();
        TournamentEntity tournament = TournamentEntity.builder().id(1L).name("Copa DOSW").build();

        TournamentRulesConfirmationEntity savedConfirmation = TournamentRulesConfirmationEntity.builder()
                .id(1L)
                .tournament(tournament)
                .user(user)
                .confirmedAt(LocalDateTime.now())
                .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(userRepository.findByEmail("myusername")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("myusername")).thenReturn(Optional.of(user));
        when(tournamentRulesConfirmationRepository.findByTournamentIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(tournamentRulesConfirmationRepository.save(any(TournamentRulesConfirmationEntity.class))).thenReturn(savedConfirmation);

        var result = tournamentService.confirmTournamentRules(1L, "myusername");

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }
}
