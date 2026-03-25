package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.ProfileResponse;

public interface PlayerService {
    ProfileResponse updateProfile(Long id, ProfileRequest request);
    ProfileResponse changeAvailability(Long id, AvailabilityRequest request);
}
