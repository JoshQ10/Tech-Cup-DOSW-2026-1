package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.SportProfileMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final SportProfileRepository sportProfileRepository;
    private final UserRepository userRepository;
    private final SportProfileMapper sportProfileMapper;

    @Override
    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        log.info("Updating profile for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: profile {} not found", id);
                    return new ResourceNotFoundException("Profile not found");
                });

        profile.setPosition(request.getPosition());
        profile.setJerseyNumber(request.getJerseyNumber());
        profile.setPhotoUrl(request.getPhotoUrl());
        profile.setAvailable(request.isAvailable());
        profile.setSemester(request.getSemester());
        profile.setGender(request.getGender());
        profile.setAge(request.getAge());

        SportProfile updatedProfile = sportProfileRepository.save(profile);
        log.info("Profile updated successfully for player: {}", id);

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    public ProfileResponse changeAvailability(Long id, AvailabilityRequest request) {
        log.info("Changing availability for player: {}", id);
        log.debug("Availability change request - new state: {}", request.isAvailable());

        SportProfile profile = sportProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: profile {} not found", id);
                    return new ResourceNotFoundException("Profile not found");
                });
        log.debug("Current availability state: {}", profile.isAvailable());

        profile.setAvailable(request.isAvailable());
        log.debug("Availability state transition in memory");

        SportProfile updatedProfile = sportProfileRepository.save(profile);
        log.info("Availability changed successfully for player: {}", id);
        log.debug("Availability change persisted to database - new state: {}", updatedProfile.isAvailable());

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    public ProfileResponse uploadPhoto(Long id, PhotoUploadRequest request) {
        log.info("Uploading profile photo for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Upload photo failed: profile {} not found", id);
                    return new ResourceNotFoundException("Profile not found");
                });

        profile.setPhotoUrl(request.getPhotoUrl());
        SportProfile updatedProfile = sportProfileRepository.save(profile);
        log.info("Profile photo uploaded successfully for player: {}", id);

        return mapToProfileResponse(updatedProfile);
    }

    @Override
    public ProfileResponse getProfile(Long id) {
        log.info("Fetching profile for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Get profile failed: profile {} not found", id);
                    return new ResourceNotFoundException("Profile not found");
                });

        return mapToProfileResponse(profile);
    }

    private ProfileResponse mapToProfileResponse(SportProfile profile) {
        ProfileResponse response = sportProfileMapper.toResponse(profile);

        // Mapeo especial para playerName que requiere búsqueda de usuario
        if (profile.getUserId() != null) {
            User user = userRepository.findById(profile.getUserId()).orElse(null);
            if (user != null) {
                response.setPlayerName(user.getName());
            }
        }

        return response;
    }
}
