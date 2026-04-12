package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TournamentServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Court;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.TournamentDate;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.ChangeStatusRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentConfigRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TournamentSetupRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentDateEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TournamentPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TournamentServiceImpl Tests")
@SuppressWarnings("null")
class TournamentServiceImplTest {

        @Mock
        private TournamentRepository tournamentRepository;

        @Mock
        private TournamentMapper tournamentMapper;

        @Mock
        private TournamentPersistenceMapper tournamentPersistenceMapper;

        @Mock
        private TournamentRequestValidator tournamentRequestValidator;

        @Mock
        private TournamentConfigRequestValidator tournamentConfigRequestValidator;

        @Mock
        private TournamentSetupRequestValidator tournamentSetupRequestValidator;

        @Mock
        private ChangeStatusRequestValidator changeStatusRequestValidator;

        @Mock
        private CourtRepository courtRepository;

        @Mock
        private TournamentDateRepository tournamentDateRepository;

        @InjectMocks
        private TournamentServiceImpl tournamentService;

        private TournamentRequest tournamentRequest;
        private TournamentConfigRequest configRequest;
        private ChangeStatusRequest changeStatusRequest;
        private Tournament testTournament;
        private TournamentEntity testTournamentEntity;
        private TournamentResponse expectedResponse;

        @BeforeEach
        void setUp() {
                tournamentRequest = TournamentRequest.builder()
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .build();

                configRequest = TournamentConfigRequest.builder()
                                .startDate(LocalDate.of(2026, 4, 1))
                                .endDate(LocalDate.of(2026, 6, 30))
                                .teamCount(8)
                                .costPerTeam(300000.0)
                                .build();

                changeStatusRequest = ChangeStatusRequest.builder()
                                .status(TournamentStatus.ACTIVE)
                                .build();

                testTournament = Tournament.builder()
                                .id(1L)
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .status(TournamentStatus.DRAFT)
                                .build();

                testTournamentEntity = TournamentEntity.builder()
                                .id(1L)
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .status(TournamentStatus.DRAFT)
                                .build();

                expectedResponse = TournamentResponse.builder()
                                .id(1L)
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .status(TournamentStatus.DRAFT)
                                .build();
        }

        @Nested
        @DisplayName("create()")
        class CreateTests {

                @Test
                @DisplayName("Debe crear torneo exitosamente con datos validos")
                void testCreateTournamentSuccess() {
                        TournamentEntity draftEntity = TournamentEntity.builder()
                                        .name("Football Tournament 2026-1")
                                        .startDate(LocalDate.of(2026, 3, 1))
                                        .endDate(LocalDate.of(2026, 5, 31))
                                        .teamCount(16)
                                        .costPerTeam(500000.0)
                                        .status(TournamentStatus.DRAFT)
                                        .build();

                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(draftEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(tournamentPersistenceMapper.toModel(any(TournamentEntity.class))).thenReturn(testTournament);
                        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(expectedResponse);

                        TournamentResponse response = tournamentService.create(tournamentRequest);

                        assertNotNull(response);
                        assertEquals("Football Tournament 2026-1", response.getName());
                        assertEquals(1L, response.getId());
                        assertEquals(16, response.getTeamCount());
                        assertEquals(500000.0, response.getCostPerTeam());
                        assertEquals(TournamentStatus.DRAFT, response.getStatus());
                        verify(tournamentRequestValidator).validate(tournamentRequest);
                        verify(tournamentRepository).save(any(TournamentEntity.class));
                        verify(tournamentMapper).toResponse(testTournament);
                }

                @Test
                @DisplayName("Debe asignar estado DRAFT al crear un torneo")
                void testCreateTournamentSetsStatusDraft() {
                        TournamentEntity draftEntity = TournamentEntity.builder()
                                        .name("Football Tournament 2026-1")
                                        .startDate(LocalDate.of(2026, 3, 1))
                                        .endDate(LocalDate.of(2026, 5, 31))
                                        .teamCount(16)
                                        .costPerTeam(500000.0)
                                        .status(TournamentStatus.DRAFT)
                                        .build();

                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(draftEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(tournamentPersistenceMapper.toModel(any(TournamentEntity.class))).thenReturn(testTournament);
                        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(expectedResponse);

                        TournamentResponse response = tournamentService.create(tournamentRequest);

                        assertEquals(TournamentStatus.DRAFT, response.getStatus());
                }

                @Test
                @DisplayName("Debe lanzar ValidationException cuando el request es invalido")
                void testCreateTournamentValidationFails() {
                        doThrow(new ValidationException("Errores de validacion en la creacion del torneo"))
                                        .when(tournamentRequestValidator).validate(any(TournamentRequest.class));

                        assertThrows(ValidationException.class, () -> tournamentService.create(tournamentRequest));
                        verify(tournamentRepository, never()).save(any());
                }
        }

        @Nested
        @DisplayName("getById()")
        class GetByIdTests {

                @Test
                @DisplayName("Debe obtener torneo por ID exitosamente")
                void testGetTournamentByIdSuccess() {
                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentMapper.toResponse(testTournament)).thenReturn(expectedResponse);

                        TournamentResponse response = tournamentService.getById(1L);

                        assertNotNull(response);
                        assertEquals("Football Tournament 2026-1", response.getName());
                        assertEquals(1L, response.getId());
                        verify(tournamentRepository).findById(1L);
                }

                @Test
                @DisplayName("Debe lanzar ResourceNotFoundException cuando el torneo no existe")
                void testGetTournamentByIdNotFound() {
                        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

                        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getById(999L));
                        verify(tournamentRepository).findById(999L);
                }
        }

        @Nested
        @DisplayName("configure()")
        class ConfigureTests {

                @Test
                @DisplayName("Debe configurar torneo exitosamente")
                void testConfigureTournamentSuccess() {
                        TournamentResponse configuredResponse = TournamentResponse.builder()
                                        .id(1L).name("Football Tournament 2026-1")
                                        .startDate(LocalDate.of(2026, 4, 1))
                                        .endDate(LocalDate.of(2026, 6, 30))
                                        .teamCount(8).costPerTeam(300000.0)
                                        .status(TournamentStatus.DRAFT).build();

                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(testTournamentEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(configuredResponse);

                        TournamentResponse response = tournamentService.configure(1L, configRequest);

                        assertNotNull(response);
                        assertEquals(8, response.getTeamCount());
                        assertEquals(300000.0, response.getCostPerTeam());
                        verify(tournamentConfigRequestValidator).validate(configRequest);
                        verify(tournamentRepository).findById(1L);
                        verify(tournamentRepository).save(any(TournamentEntity.class));
                }

                @Test
                @DisplayName("Debe lanzar ResourceNotFoundException al configurar torneo inexistente")
                void testConfigureTournamentNotFound() {
                        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

                        assertThrows(ResourceNotFoundException.class,
                                        () -> tournamentService.configure(999L, configRequest));
                }

                @Test
                @DisplayName("Debe lanzar ValidationException con config invalida")
                void testConfigureTournamentValidationFails() {
                        doThrow(new ValidationException("Errores de validacion"))
                                        .when(tournamentConfigRequestValidator).validate(any());

                        assertThrows(ValidationException.class,
                                        () -> tournamentService.configure(1L, configRequest));
                        verify(tournamentRepository, never()).findById(anyLong());
                }
        }

        @Nested
        @DisplayName("changeStatus()")
        class ChangeStatusTests {

                @Test
                @DisplayName("Debe cambiar estado DRAFT -> ACTIVE exitosamente")
                void testChangeStatusSuccess() {
                        TournamentResponse activeResponse = TournamentResponse.builder()
                                        .id(1L).name("Football Tournament 2026-1")
                                        .status(TournamentStatus.ACTIVE).build();

                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(testTournamentEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(activeResponse);

                        TournamentResponse response = tournamentService.changeStatus(1L, changeStatusRequest);

                        assertNotNull(response);
                        assertEquals(TournamentStatus.ACTIVE, response.getStatus());
                        verify(changeStatusRequestValidator).validate(TournamentStatus.DRAFT, TournamentStatus.ACTIVE);
                        verify(tournamentRepository).findById(1L);
                        verify(tournamentRepository).save(any(TournamentEntity.class));
                }

                @Test
                @DisplayName("Debe lanzar ResourceNotFoundException al cambiar estado de torneo inexistente")
                void testChangeStatusNotFound() {
                        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

                        assertThrows(ResourceNotFoundException.class,
                                        () -> tournamentService.changeStatus(999L, changeStatusRequest));
                }

                @Test
                @DisplayName("Debe lanzar BusinessRuleException en transicion invalida")
                void testChangeStatusInvalidTransition() {
                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        doThrow(new BusinessRuleException("Transicion de estado no permitida: DRAFT -> FINISHED"))
                                        .when(changeStatusRequestValidator)
                                        .validate(TournamentStatus.DRAFT, TournamentStatus.FINISHED);

                        ChangeStatusRequest invalidRequest = ChangeStatusRequest.builder()
                                        .status(TournamentStatus.FINISHED).build();

                        assertThrows(BusinessRuleException.class,
                                        () -> tournamentService.changeStatus(1L, invalidRequest));
                        verify(tournamentRepository, never()).save(any());
                }
        }

        @Nested
        @DisplayName("setup()")
        class SetupTests {

                private TournamentSetupRequest setupRequest;

                @BeforeEach
                void setUpRequest() {
                        setupRequest = TournamentSetupRequest.builder()
                                        .rules("Partidos de 2 tiempos de 25 minutos.")
                                        .sanctionRules("Tarjeta roja = 2 partidos")
                                        .inscriptionCloseDate(LocalDate.of(2026, 3, 20))
                                        .courts(List.of(
                                                        CourtRequest.builder().name("Cancha Principal")
                                                                        .location("Bloque B").build(),
                                                        CourtRequest.builder().name("Cancha Auxiliar")
                                                                        .location("Bloque C").build()))
                                        .schedule(List.of(
                                                        TournamentDateRequest.builder()
                                                                        .description("Jornada 1")
                                                                        .eventDate(LocalDate.of(2026, 4, 15))
                                                                        .build()))
                                        .build();
                }

                private void stubCourtPersistenceChain() {
                        when(tournamentPersistenceMapper.toEntity(any(Court.class))).thenAnswer(inv -> {
                                Court c = inv.getArgument(0);
                                return CourtEntity.builder()
                                                .name(c.getName())
                                                .location(c.getLocation())
                                                .tournament(testTournamentEntity)
                                                .build();
                        });
                        when(courtRepository.saveAll(anyList())).thenAnswer(inv -> {
                                @SuppressWarnings("unchecked")
                                List<CourtEntity> in = inv.getArgument(0);
                                return in.stream()
                                                .map(e -> CourtEntity.builder()
                                                                .id("Cancha Principal".equals(e.getName()) ? 1L : 2L)
                                                                .name(e.getName())
                                                                .location(e.getLocation())
                                                                .tournament(e.getTournament())
                                                                .build())
                                                .toList();
                        });
                        when(tournamentPersistenceMapper.toModel(any(CourtEntity.class))).thenAnswer(inv -> {
                                CourtEntity e = inv.getArgument(0);
                                return Court.builder()
                                                .id(e.getId())
                                                .name(e.getName())
                                                .location(e.getLocation())
                                                .tournament(testTournament)
                                                .build();
                        });
                }

                private void stubTournamentDatePersistenceChain() {
                        when(tournamentPersistenceMapper.toEntity(any(TournamentDate.class))).thenAnswer(inv -> {
                                TournamentDate d = inv.getArgument(0);
                                return TournamentDateEntity.builder()
                                                .description(d.getDescription())
                                                .eventDate(d.getEventDate())
                                                .tournament(testTournamentEntity)
                                                .build();
                        });
                        when(tournamentDateRepository.saveAll(anyList())).thenAnswer(inv -> {
                                @SuppressWarnings("unchecked")
                                List<TournamentDateEntity> in = inv.getArgument(0);
                                return in.stream()
                                                .map(e -> TournamentDateEntity.builder()
                                                                .id(1L)
                                                                .description(e.getDescription())
                                                                .eventDate(e.getEventDate())
                                                                .tournament(e.getTournament())
                                                                .build())
                                                .toList();
                        });
                        when(tournamentPersistenceMapper.toModel(any(TournamentDateEntity.class))).thenAnswer(inv -> {
                                TournamentDateEntity e = inv.getArgument(0);
                                return TournamentDate.builder()
                                                .id(e.getId())
                                                .description(e.getDescription())
                                                .eventDate(e.getEventDate())
                                                .tournament(testTournament)
                                                .build();
                        });
                }

                private void stubPersistenceForCourtsAndDates() {
                        stubCourtPersistenceChain();
                        stubTournamentDatePersistenceChain();
                }

                @Test
                @DisplayName("Debe configurar torneo exitosamente con canchas, horarios y reglamento")
                void testSetupSuccess() {
                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(testTournamentEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(courtRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
                        when(tournamentDateRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
                        stubPersistenceForCourtsAndDates();

                        TournamentSetupResponse response = tournamentService.setup(1L, setupRequest);

                        assertNotNull(response);
                        assertEquals(1L, response.getTournamentId());
                        assertEquals("Partidos de 2 tiempos de 25 minutos.", response.getRules());
                        assertEquals("Tarjeta roja = 2 partidos", response.getSanctionRules());
                        assertEquals(2, response.getCourts().size());
                        assertEquals(1, response.getSchedule().size());
                        assertEquals("Cancha Principal", response.getCourts().get(0).getName());
                        assertEquals("Jornada 1", response.getSchedule().get(0).getDescription());
                        verify(tournamentSetupRequestValidator).validate(setupRequest);
                        verify(courtRepository).saveAll(anyList());
                        verify(tournamentDateRepository).saveAll(anyList());
                }

                @Test
                @DisplayName("Debe lanzar ResourceNotFoundException al configurar torneo inexistente")
                void testSetupTournamentNotFound() {
                        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

                        assertThrows(ResourceNotFoundException.class,
                                        () -> tournamentService.setup(999L, setupRequest));
                }

                @Test
                @DisplayName("Debe lanzar ValidationException con request invalido")
                void testSetupValidationFails() {
                        doThrow(new ValidationException("Errores de validacion"))
                                        .when(tournamentSetupRequestValidator).validate(any());

                        assertThrows(ValidationException.class,
                                        () -> tournamentService.setup(1L, setupRequest));
                        verify(tournamentRepository, never()).findById(anyLong());
                }

                @Test
                @DisplayName("Debe reemplazar canchas existentes al reconfigurar")
                void testSetupReplacesCourts() {
                        CourtEntity existingEntity = CourtEntity.builder()
                                        .id(10L).name("Vieja").location("Vieja")
                                        .tournament(testTournamentEntity).build();

                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(testTournamentEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(courtRepository.findByTournamentId(1L)).thenReturn(List.of(existingEntity));
                        when(tournamentDateRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
                        stubCourtPersistenceChain();
                        when(tournamentPersistenceMapper.toEntity(any(TournamentDate.class))).thenAnswer(inv -> {
                                TournamentDate d = inv.getArgument(0);
                                return TournamentDateEntity.builder()
                                                .description(d.getDescription())
                                                .eventDate(d.getEventDate())
                                                .tournament(testTournamentEntity)
                                                .build();
                        });
                        when(tournamentDateRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

                        tournamentService.setup(1L, setupRequest);

                        verify(courtRepository).deleteAll(List.of(existingEntity));
                        verify(courtRepository).saveAll(anyList());
                }

                @Test
                @DisplayName("Debe guardar inscriptionCloseDate cuando se proporciona")
                void testSetupWithInscriptionCloseDate() {
                        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournamentEntity));
                        when(tournamentPersistenceMapper.toModel(testTournamentEntity)).thenReturn(testTournament);
                        when(tournamentPersistenceMapper.toEntity(any(Tournament.class))).thenReturn(testTournamentEntity);
                        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(testTournamentEntity);
                        when(courtRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
                        when(courtRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
                        when(tournamentDateRepository.findByTournamentId(1L)).thenReturn(Collections.emptyList());
                        when(tournamentDateRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
                        when(tournamentPersistenceMapper.toEntity(any(Court.class))).thenAnswer(inv -> {
                                Court c = inv.getArgument(0);
                                return CourtEntity.builder()
                                                .name(c.getName())
                                                .location(c.getLocation())
                                                .tournament(testTournamentEntity)
                                                .build();
                        });
                        when(tournamentPersistenceMapper.toEntity(any(TournamentDate.class))).thenAnswer(inv -> {
                                TournamentDate d = inv.getArgument(0);
                                return TournamentDateEntity.builder()
                                                .description(d.getDescription())
                                                .eventDate(d.getEventDate())
                                                .tournament(testTournamentEntity)
                                                .build();
                        });

                        tournamentService.setup(1L, setupRequest);

                        assertEquals(LocalDate.of(2026, 3, 20), testTournament.getInscriptionCloseDate());
                }
        }
}
