package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.AvailabilityRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.ProfileRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.ProfileResponse;

public interface PlayerService {
    ProfileResponse updateProfile(String id, ProfileRequest request);
    ProfileResponse changeAvailability(String id, AvailabilityRequest request);
}
