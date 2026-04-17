package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.InvitationStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.InvitationServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamInvitationEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamInvitationRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("InvitationServiceImpl Unit Tests")
class InvitationServiceImplTest {

    @Mock
    private TeamInvitationRepository invitationRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private InvitationServiceImpl invitationService;

    private static final Long TEAM_ID = 1L;
    private static final Long PLAYER_ID = 2L;
    private static final Long INVITATION_ID = 100L;
    private static final Long CAPTAIN_ID = 3L;
    private static final int PAGE = 0;
    private static final int LIMIT = 10;

    @BeforeEach
    void setUp() {
        invitationService = new InvitationServiceImpl(invitationRepository, teamRepository, userRepository);
    }

    @Test
    @DisplayName("Should list pending invitations successfully")
    void testListPendingByUserSuccess() {
        TeamInvitationEntity invitation = createInvitation();
        List<TeamInvitationEntity> invitations = List.of(invitation);
        Page<TeamInvitationEntity> page = new PageImpl<>(invitations);

        when(invitationRepository.findByPlayerIdAndStatus(eq(PLAYER_ID), eq(InvitationStatus.PENDING), any(Pageable.class)))
                .thenReturn(page);

        invitationService.listPendingByUser(PLAYER_ID, PAGE, LIMIT);

        verify(invitationRepository).findByPlayerIdAndStatus(eq(PLAYER_ID), eq(InvitationStatus.PENDING), any(Pageable.class));
    }

    @Test
    @DisplayName("Should accept invitation successfully")
    void testAcceptInvitationSuccess() {
        TeamInvitationEntity invitation = createInvitation();
        TeamEntity team = createTeam();

        when(invitationRepository.findById(INVITATION_ID)).thenReturn(Optional.of(invitation));
        when(teamRepository.findCurrentTeamByPlayerId(PLAYER_ID)).thenReturn(Optional.empty());
        when(invitationRepository.findByPlayerIdAndStatus(PLAYER_ID, InvitationStatus.PENDING))
                .thenReturn(new ArrayList<>());

        invitationService.acceptInvitation(INVITATION_ID, PLAYER_ID);

        verify(invitationRepository).save(invitation);
        verify(teamRepository).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when invitation not found")
    void testAcceptInvitationNotFound() {
        when(invitationRepository.findById(INVITATION_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> invitationService.acceptInvitation(INVITATION_ID, PLAYER_ID));

        verify(invitationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject invitation successfully")
    void testRejectInvitationSuccess() {
        TeamInvitationEntity invitation = createInvitation();

        when(invitationRepository.findById(INVITATION_ID)).thenReturn(Optional.of(invitation));

        invitationService.rejectInvitation(INVITATION_ID, PLAYER_ID);

        verify(invitationRepository).save(any(TeamInvitationEntity.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when rejecting already processed invitation")
    void testRejectInvitationAlreadyProcessed() {
        TeamInvitationEntity invitation = createInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);

        when(invitationRepository.findById(INVITATION_ID)).thenReturn(Optional.of(invitation));

        assertThrows(BusinessRuleException.class,
                () -> invitationService.rejectInvitation(INVITATION_ID, PLAYER_ID));

        verify(invitationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should send invitation successfully")
    void testSendInvitationSuccess() {
        TeamEntity team = createTeam();
        UserEntity player = createPlayer();

        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(userRepository.findById(PLAYER_ID)).thenReturn(Optional.of(player));
        when(teamRepository.findCurrentTeamByPlayerId(PLAYER_ID)).thenReturn(Optional.empty());
        when(invitationRepository.findByTeamIdAndPlayerId(TEAM_ID, PLAYER_ID)).thenReturn(Optional.empty());

        invitationService.sendInvitation(TEAM_ID, PLAYER_ID);

        verify(invitationRepository).save(any(TeamInvitationEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when team not found for sending invitation")
    void testSendInvitationTeamNotFound() {
        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> invitationService.sendInvitation(TEAM_ID, PLAYER_ID));

        verify(invitationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when team is full")
    void testSendInvitationTeamFull() {
        TeamEntity team = createTeam();
        team.setPlayers(createFullTeamPlayers());

        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        assertThrows(BusinessRuleException.class,
                () -> invitationService.sendInvitation(TEAM_ID, PLAYER_ID));

        verify(invitationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when player not found for sending invitation")
    void testSendInvitationPlayerNotFound() {
        TeamEntity team = createTeam();

        when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(userRepository.findById(PLAYER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> invitationService.sendInvitation(TEAM_ID, PLAYER_ID));

        verify(invitationRepository, never()).save(any());
    }

    private TeamInvitationEntity createInvitation() {
        TeamInvitationEntity invitation = new TeamInvitationEntity();
        invitation.setId(INVITATION_ID);
        invitation.setTeam(createTeam());
        invitation.setPlayer(createPlayer());
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setSentAt(LocalDateTime.now());
        return invitation;
    }

    private TeamEntity createTeam() {
        TeamEntity team = new TeamEntity();
        team.setId(TEAM_ID);
        team.setName("Test Team");
        team.setCaptainId(CAPTAIN_ID);
        team.setPlayers(new ArrayList<>());
        return team;
    }

    private UserEntity createPlayer() {
        UserEntity player = new UserEntity();
        player.setId(PLAYER_ID);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setEmail("john@example.com");
        return player;
    }

    private List<Long> createFullTeamPlayers() {
        List<Long> players = new ArrayList<>();
        for (int i = 0; i < AppConstants.MAX_TEAM_PLAYERS; i++) {
            players.add((long) i);
        }
        return players;
    }
}
