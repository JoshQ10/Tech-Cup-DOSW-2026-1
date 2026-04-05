package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.PhotoUploadRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.PlayerSearchResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ProfileResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import org.springframework.data.domain.Pageable;

public interface PlayerService {
    ProfileResponse updateProfile(Long id, ProfileRequest request);

    ProfileResponse uploadPhoto(Long id, PhotoUploadRequest request);

    ProfileResponse changeAvailability(Long id, AvailabilityRequest request);

    ProfileResponse getProfile(Long id);

    PlayerSearchResponse searchAvailablePlayers(
            Position position,
            Integer semester,
            Integer age,
            String gender,
            String name,
            Pageable pageable);
}
