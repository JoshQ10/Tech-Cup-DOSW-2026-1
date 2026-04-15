package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.InvitationPageResponse;

public interface InvitationService {

    InvitationPageResponse listPendingByUser(Long userId, int page, int limit);

    void acceptInvitation(Long invitationId, Long currentUserId);

    void rejectInvitation(Long invitationId, Long currentUserId);

    void sendInvitation(Long teamId, Long playerId);
}
