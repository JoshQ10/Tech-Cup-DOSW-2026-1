package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.SportsProfileUpdateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.FileStorageService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.AppConstants;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.PhotoValidationUtil;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final SportProfileRepository sportProfileRepository;
    private final UserRepository userRepository;
    private final SportProfileMapper sportProfileMapper;
    private final PhotoValidationUtil photoValidationUtil;
    private final UserPersistenceMapper userPersistenceMapper;
    private final FileStorageService fileStorageService;

    @Override
    @SuppressWarnings("null")
    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        log.info("Updating profile for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Update failed: profile {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_PROFILE_NOT_FOUND);
                });

        // Validar y actualizar posición
        if (request.getPosition() != null) {
            profile.setPosition(request.getPosition());
            log.debug("Posición actualizada a: {}", request.getPosition());
        }

        if (request.getSecondaryPosition() != null) {
            profile.setSecondaryPosition(request.getSecondaryPosition());
        }

        if (request.getDominantFoot() != null) {
            profile.setDominantFoot(request.getDominantFoot());
        }

        // Validar y actualizar dorsal
        if (request.getJerseyNumber() > 0) {
            profile.setJerseyNumber(request.getJerseyNumber());
            log.debug("Dorsal actualizado a: {}", request.getJerseyNumber());
        }

        // Validar y actualizar foto si se proporciona
        if (request.getPhotoUrl() != null && !request.getPhotoUrl().isBlank()) {
            try {
                photoValidationUtil.validateBase64Photo(request.getPhotoUrl());
                profile.setPhotoUrl(request.getPhotoUrl());
                log.debug("Foto actualizada para jugador: {}", id);
            } catch (IllegalArgumentException e) {
                log.warn("Validación de foto fallida: {}", e.getMessage());
                throw e;
            }
        }

        // Actualizar disponibilidad
        profile.setAvailable(request.isAvailable());

        // Actualizar datos adicionales si se proporcionan
        if (request.getSemester() != null && request.getSemester() > 0) {
            profile.setSemester(request.getSemester());
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            profile.setGender(request.getGender());
        }

        if (request.getAge() != null && request.getAge() > 0) {
            profile.setAge(request.getAge());
        }

        SportProfile updatedProfile = userPersistenceMapper.toModel(
                sportProfileRepository.save(userPersistenceMapper.toEntity(profile)));
        log.info("Profile updated successfully for player: {}", id);

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    @SuppressWarnings("null")
    public ProfileResponse changeAvailability(Long id, AvailabilityRequest request) {
        log.info("Changing availability for player: {}", id);
        log.debug("Availability change request - new state: {}, reason: {}", request.isAvailable(),
                request.getReason());

        SportProfile profile = sportProfileRepository.findById(id)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Update failed: profile {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_PROFILE_NOT_FOUND);
                });

        boolean previousAvailability = profile.isAvailable();
        log.debug("Current availability state: {}", previousAvailability);

        // Validar si el estado es diferente para evitar cambios innecesarios
        if (previousAvailability == request.isAvailable()) {
            log.info("Availability state unchanged for player: {}. New state is same as current state", id);
        } else {
            log.info("Availability state changed for player: {} from {} to {}",
                    id, previousAvailability, request.isAvailable());
        }

        // Actualizar estado y auditoría
        profile.setAvailable(request.isAvailable());
        profile.setLastAvailabilityChange(LocalDateTime.now());
        profile.setAvailabilityChangeReason(request.getReason());

        log.debug("Availability state transition in memory");

        SportProfile updatedProfile = userPersistenceMapper.toModel(
                sportProfileRepository.save(userPersistenceMapper.toEntity(profile)));
        log.info("Availability changed successfully for player: {}", id);
        log.debug("Availability change persisted to database - new state: {}, changed at: {}",
                updatedProfile.isAvailable(), updatedProfile.getLastAvailabilityChange());

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    @SuppressWarnings("null")
    public ProfileResponse uploadPhoto(Long id, PhotoUploadRequest request) {
        log.info("Uploading profile photo for player: {}", id);

        try {
            // Validar la foto en base64
            photoValidationUtil.validateBase64Photo(request.getPhotoUrl());
            log.debug("Foto validada correctamente para jugador: {}", id);
        } catch (IllegalArgumentException e) {
            log.warn("Validación de foto fallida para jugador: {}: {}", id, e.getMessage());
            throw e;
        }

        SportProfile profile = sportProfileRepository.findById(id)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Upload photo failed: profile {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_PROFILE_NOT_FOUND);
                });

        profile.setPhotoUrl(request.getPhotoUrl());
        SportProfile updatedProfile = userPersistenceMapper.toModel(
                sportProfileRepository.save(userPersistenceMapper.toEntity(profile)));
        log.info("Profile photo uploaded successfully for player: {}", id);

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    @SuppressWarnings("null")
    public ProfileResponse getProfile(Long id) {
        log.info("Fetching profile for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> {
                    log.warn("Get profile failed: profile {} not found", id);
                    return new ResourceNotFoundException(AppConstants.ERROR_PROFILE_NOT_FOUND);
                });

        return mapToProfileResponse(profile);
    }

    @Override
    @SuppressWarnings("null")
    public ProfileResponse upsertSportsProfileByUserId(Long userId, SportsProfileUpdateRequest request) {
        log.info("Upserting sports profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .map(userPersistenceMapper::toModel)
                .orElseGet(() -> SportProfile.builder().userId(userId).program(user.getProgram()).build());

        profile.setPosition(request.getPrimaryPosition());
        profile.setSecondaryPosition(request.getSecondaryPosition());
        profile.setJerseyNumber(request.getJerseyNumber());
        profile.setDominantFoot(request.getDominantFoot());
        profile.setAvailable(request.getAvailable());

        if (profile.getProgram() == null) {
            profile.setProgram(user.getProgram());
        }

        SportProfile updatedProfile = userPersistenceMapper.toModel(
                sportProfileRepository.save(userPersistenceMapper.toEntity(profile)));
        return mapToProfileResponse(updatedProfile);
    }

    @Override
    @SuppressWarnings("null")
    public ProfileResponse uploadFullPhotoByUserId(Long userId, MultipartFile file) {
        log.info("Uploading full photo for user: {}", userId);

        User user = userRepository.findById(userId)
                .map(userPersistenceMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));

        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .map(userPersistenceMapper::toModel)
                .orElseGet(() -> SportProfile.builder().userId(userId).program(user.getProgram()).build());

        String fullPhotoUrl = fileStorageService.storeFullPhoto(file, userId);
        profile.setFullPhotoUrl(fullPhotoUrl);

        SportProfile updatedProfile = userPersistenceMapper.toModel(
                sportProfileRepository.save(userPersistenceMapper.toEntity(profile)));
        return mapToProfileResponse(updatedProfile);
    }

    private ProfileResponse mapToProfileResponse(SportProfile profile) {
        ProfileResponse response = sportProfileMapper.toResponse(profile);

        // Mapeo especial para playerName que requiere búsqueda de usuario
        if (profile.getUserId() != null) {
            @SuppressWarnings("null")
            User user = userRepository.findById(profile.getUserId())
                    .map(userPersistenceMapper::toModel).orElse(null);
            if (user != null) {
                response.setPlayerName(user.getFirstName() + " " + user.getLastName());
            }
        }

        // Agregar información de auditoría de disponibilidad
        response.setLastAvailabilityChange(profile.getLastAvailabilityChange());
        response.setAvailabilityChangeReason(profile.getAvailabilityChangeReason());

        return response;
    }

    @Override
    public PlayerSearchResponse searchAvailablePlayers(
            Position position,
            Integer semester,
            Integer age,
            String gender,
            String name,
            Pageable pageable) {
        log.info("Searching available players with filters - position: {}, semester: {}, age: {}, gender: {}, name: {}",
                position, semester, age, gender, name);

        var entityPage = sportProfileRepository.searchAvailablePlayers(
                position, semester, age, gender, name, pageable);

        log.debug("Found {} available players matching criteria", entityPage.getTotalElements());

        var players = entityPage.getContent().stream()
                .map(userPersistenceMapper::toModel)
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());

        PlayerSearchResponse response = PlayerSearchResponse.builder()
                .players(players)
                .currentPage(entityPage.getNumber())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .pageSize(entityPage.getSize())
                .hasNextPage(entityPage.hasNext())
                .isFirstPage(entityPage.isFirst())
                .isLastPage(entityPage.isLast())
                .build();

        log.info("Player search completed successfully - returned {} players", players.size());
        return response;
    }
}
