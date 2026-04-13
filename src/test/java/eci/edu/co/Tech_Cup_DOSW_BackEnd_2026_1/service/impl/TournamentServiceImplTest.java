package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
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
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.CourtEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentDateEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TournamentPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentDateRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
