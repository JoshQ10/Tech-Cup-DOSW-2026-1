package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchCardRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchGoalRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchPossessionRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchResultRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.MatchStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchEventEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.MatchPossessionEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.match.SanctionEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.StandingEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchEventRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchPossessionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.MatchRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SanctionRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MatchServiceImpl Unit Tests")
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchEventRepository matchEventRepository;

    @Mock
    private MatchPossessionRepository matchPossessionRepository;

    @Mock
    private SanctionRepository sanctionRepository;

    @Mock
    private StandingRepository standingRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private MatchServiceImpl matchService;

    private static final Long MATCH_ID = 1L;
    private static final Long TEAM_ID = 1L;
    private static final int PAGE = 0;
    private static final int LIMIT = 10;

    @BeforeEach
    void setUp() {
        matchService = new MatchServiceImpl(matchRepository, matchEventRepository, matchPossessionRepository,
                sanctionRepository, standingRepository, teamRepository, userRepository);
    }

    @Test
    @DisplayName("Should get match detail successfully")
    void testGetMatchDetailSuccess() {
        MatchEntity match = createMatch();

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchEventRepository.findByMatchId(MATCH_ID)).thenReturn(new ArrayList<>());
        when(matchPossessionRepository.findByMatchId(MATCH_ID)).thenReturn(Optional.empty());

        matchService.getMatchDetail(MATCH_ID);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository).findByMatchId(MATCH_ID);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when match not found")
    void testGetMatchDetailNotFound() {
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchDetail(MATCH_ID));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository, never()).findByMatchId(any());
    }

    @Test
    @DisplayName("Should get team matches successfully")
    void testGetTeamMatchesSuccess() {
        TeamEntity team = createTeam();
        MatchEntity match = createMatch();
        List<MatchEntity> matches = List.of(match);
        Page<MatchEntity> page = new PageImpl<>(matches);

        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findByHomeTeamIdOrAwayTeamId(eq(TEAM_ID), eq(TEAM_ID), any(Pageable.class)))
                .thenReturn(page);

        matchService.getTeamMatches(TEAM_ID, PAGE, LIMIT);

        verify(teamRepository).findById(TEAM_ID);
        verify(matchRepository).findByHomeTeamIdOrAwayTeamId(eq(TEAM_ID), eq(TEAM_ID), any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when team not found")
    void testGetTeamMatchesTeamNotFound() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.getTeamMatches(TEAM_ID, PAGE, LIMIT));

        verify(teamRepository).findById(TEAM_ID);
        verify(matchRepository, never()).findByHomeTeamIdOrAwayTeamId(any(), any(), any());
    }

    @Test
    @DisplayName("Should update match status successfully")
    void testUpdateStatusSuccess() {
        MatchEntity match = createMatch();
        MatchStatusRequest request = new MatchStatusRequest();
        request.setStatus(MatchStatus.CANCELLED);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchEventRepository.findByMatchId(MATCH_ID)).thenReturn(new ArrayList<>());
        when(matchPossessionRepository.findByMatchId(MATCH_ID)).thenReturn(Optional.empty());
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(match);

        matchService.updateStatus(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating status on non-existent match")
    void testUpdateStatusMatchNotFound() {
        MatchStatusRequest request = new MatchStatusRequest();
        request.setStatus(MatchStatus.FINISHED);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.updateStatus(MATCH_ID, request));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register match result successfully")
    void testRegisterResultSuccess() {
        MatchEntity match = createMatch();
        MatchResultRequest request = new MatchResultRequest();
        request.setHomeScore(2);
        request.setAwayScore(1);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(standingRepository.findByTournamentIdAndTeamId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(createStanding()));
        when(standingRepository.save(any(StandingEntity.class)))
                .thenReturn(createStanding());
        when(matchEventRepository.findByMatchId(MATCH_ID)).thenReturn(new ArrayList<>());
        when(matchPossessionRepository.findByMatchId(MATCH_ID)).thenReturn(Optional.empty());
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(match);

        matchService.registerResult(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(standingRepository, times(2)).save(any(StandingEntity.class));
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when registering result on non-existent match")
    void testRegisterResultMatchNotFound() {
        MatchResultRequest request = new MatchResultRequest();
        request.setHomeScore(1);
        request.setAwayScore(1);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.registerResult(MATCH_ID, request));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register goals successfully")
    void testRegisterGoalsSuccess() {
        MatchEntity match = createMatch();
        UserEntity player = createPlayer();
        TeamEntity team = createTeam();

        List<MatchGoalRequest.GoalEntry> entries = List.of(
                new MatchGoalRequest.GoalEntry(1L, 1L, 45, 0),
                new MatchGoalRequest.GoalEntry(2L, 1L, 67, 0)
        );
        MatchGoalRequest request = new MatchGoalRequest();
        request.setGoals(entries);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(player));
        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(team));
        when(matchEventRepository.save(any(MatchEventEntity.class)))
                .thenReturn(new MatchEventEntity());
        when(matchEventRepository.findByMatchId(MATCH_ID)).thenReturn(new ArrayList<>());
        when(matchPossessionRepository.findByMatchId(MATCH_ID)).thenReturn(Optional.empty());

        matchService.registerGoals(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository, times(2)).save(any(MatchEventEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when player not found in registerGoals")
    void testRegisterGoalsPlayerNotFound() {
        MatchEntity match = createMatch();

        List<MatchGoalRequest.GoalEntry> entries = List.of(
                new MatchGoalRequest.GoalEntry(999L, 1L, 45, 0)
        );
        MatchGoalRequest request = new MatchGoalRequest();
        request.setGoals(entries);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.registerGoals(MATCH_ID, request));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when registering invalid card type")
    void testRegisterCardsInvalidCardType() {
        MatchEntity match = createMatch();

        List<MatchCardRequest.CardEntry> entries = List.of(
                new MatchCardRequest.CardEntry(1L, 1L, MatchEventType.GOAL, 45, 0)
        );
        MatchCardRequest request = new MatchCardRequest();
        request.setCards(entries);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));

        assertThrows(BusinessRuleException.class,
                () -> matchService.registerCards(MATCH_ID, request));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register cards successfully")
    void testRegisterCardsSuccess() {
        MatchEntity match = createMatch();
        UserEntity player = createPlayer();
        TeamEntity team = createTeam();

        List<MatchCardRequest.CardEntry> entries = List.of(
                new MatchCardRequest.CardEntry(1L, 1L, MatchEventType.YELLOW_CARD, 30, 0),
                new MatchCardRequest.CardEntry(2L, 2L, MatchEventType.RED_CARD, 65, 0)
        );
        MatchCardRequest request = new MatchCardRequest();
        request.setCards(entries);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(player));
        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(team));
        when(matchEventRepository.save(any(MatchEventEntity.class)))
                .thenReturn(new MatchEventEntity());
        when(sanctionRepository.save(any(SanctionEntity.class)))
                .thenReturn(new SanctionEntity());
        when(matchEventRepository.countByPlayerIdAndEventType(any(Long.class), any(MatchEventType.class)))
                .thenReturn(0L);
        when(matchEventRepository.findByMatchId(MATCH_ID)).thenReturn(new ArrayList<>());
        when(matchPossessionRepository.findByMatchId(MATCH_ID)).thenReturn(Optional.empty());

        matchService.registerCards(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchEventRepository, times(2)).save(any(MatchEventEntity.class));
        verify(sanctionRepository).save(any(SanctionEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when registering possession on non-existent match")
    void testRegisterPossessionMatchNotFound() {
        MatchPossessionRequest request = new MatchPossessionRequest();
        request.setHomePercentage(55);
        request.setAwayPercentage(45);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.registerPossession(MATCH_ID, request));

        verify(matchRepository).findById(MATCH_ID);
        verify(matchPossessionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register possession successfully")
    void testRegisterPossessionSuccess() {
        MatchEntity match = createMatch();

        MatchPossessionRequest request = new MatchPossessionRequest();
        request.setHomePercentage(60);
        request.setAwayPercentage(40);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchPossessionRepository.findByMatchId(MATCH_ID))
                .thenReturn(Optional.empty());
        when(matchPossessionRepository.save(any(MatchPossessionEntity.class)))
                .thenReturn(new MatchPossessionEntity());

        matchService.registerPossession(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchPossessionRepository).save(any(MatchPossessionEntity.class));
    }

    @Test
    @DisplayName("Should update existing possession successfully")
    void testUpdateExistingPossessionSuccess() {
        MatchEntity match = createMatch();
        MatchPossessionEntity existing = new MatchPossessionEntity();
        existing.setHomePercentage(45);
        existing.setAwayPercentage(55);

        MatchPossessionRequest request = new MatchPossessionRequest();
        request.setHomePercentage(50);
        request.setAwayPercentage(50);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchPossessionRepository.findByMatchId(MATCH_ID))
                .thenReturn(Optional.of(existing));
        when(matchPossessionRepository.save(any(MatchPossessionEntity.class)))
                .thenReturn(existing);

        matchService.registerPossession(MATCH_ID, request);

        verify(matchRepository).findById(MATCH_ID);
        verify(matchPossessionRepository).save(any(MatchPossessionEntity.class));
    }

    private MatchEntity createMatch() {
        MatchEntity match = new MatchEntity();
        match.setId(MATCH_ID);
        match.setStatus(MatchStatus.TO_PLAY);
        match.setMatchDate(LocalDate.now());
        match.setMatchTime(LocalTime.of(15, 0));
        match.setPlayed(false);
        match.setHomeTeam(createTeam());
        match.setAwayTeam(createTeam());
        match.setTournament(createTournament());
        return match;
    }

    private TeamEntity createTeam() {
        TeamEntity team = new TeamEntity();
        team.setId(TEAM_ID);
        team.setName("Test Team");
        team.setPlayers(new ArrayList<>());
        return team;
    }

    private UserEntity createPlayer() {
        UserEntity player = new UserEntity();
        player.setId(1L);
        player.setFirstName("Test");
        player.setLastName("Player");
        return player;
    }

    private StandingEntity createStanding() {
        StandingEntity standing = new StandingEntity();
        standing.setId(1L);
        standing.setPlayed(0);
        standing.setWon(0);
        standing.setDrawn(0);
        standing.setLost(0);
        standing.setGoalsFor(0);
        standing.setGoalsAgainst(0);
        standing.setGoalDifference(0);
        standing.setPoints(0);
        return standing;
    }

    private TournamentEntity createTournament() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(1L);
        tournament.setName("Test Tournament");
        return tournament;
    }

    
}
