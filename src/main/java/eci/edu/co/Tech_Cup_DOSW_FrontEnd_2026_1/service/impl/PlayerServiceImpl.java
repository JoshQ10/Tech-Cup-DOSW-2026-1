package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.SportProfile;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository.SportProfileRepository;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final SportProfileRepository sportProfileRepository;

    @Override
    public ProfileResponse updateProfile(String id, ProfileRequest request) {
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
    public ProfileResponse changeAvailability(String id, AvailabilityRequest request) {
        log.info("Changing availability for player: {}", id);

        SportProfile profile = sportProfileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: profile {} not found", id);
                    return new ResourceNotFoundException("Profile not found");
                });

        profile.setAvailable(request.isAvailable());

        SportProfile updatedProfile = sportProfileRepository.save(profile);
        log.info("Availability changed successfully for player: {}", id);

        return mapToProfileResponse(updatedProfile);
    }

    private ProfileResponse mapToProfileResponse(SportProfile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .position(profile.getPosition())
                .jerseyNumber(profile.getJerseyNumber())
                .photoUrl(profile.getPhotoUrl())
                .available(profile.isAvailable())
                .build();
    }
}
