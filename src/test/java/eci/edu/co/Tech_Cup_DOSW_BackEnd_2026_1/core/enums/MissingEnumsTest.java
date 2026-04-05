package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Missing Enums Tests")
class MissingEnumsTest {

    @Test
    @DisplayName("MatchEventType enum values")
    void testMatchEventTypeValues() {
        // Act & Assert
        assertNotNull(MatchEventType.GOAL);
        assertNotNull(MatchEventType.YELLOW_CARD);
        assertNotNull(MatchEventType.RED_CARD);
        
        for (MatchEventType eventType : MatchEventType.values()) {
            assertNotNull(eventType.name());
            assertTrue(eventType.name().length() > 0);
        }
    }

    @Test
    @DisplayName("InvitationStatus enum values")
    void testInvitationStatusValues() {
        // Act & Assert
        assertNotNull(InvitationStatus.PENDING);
        assertNotNull(InvitationStatus.ACCEPTED);
        assertNotNull(InvitationStatus.REJECTED);
        
        for (InvitationStatus status : InvitationStatus.values()) {
            assertNotNull(status.name());
            assertTrue(status.name().length() > 0);
        }
    }

    @Test
    @DisplayName("PaymentStatus enum values")
    void testPaymentStatusValues() {
        // Act & Assert
        assertNotNull(PaymentStatus.PENDING);
        assertNotNull(PaymentStatus.IN_REVIEW);
        assertNotNull(PaymentStatus.APPROVED);
        assertNotNull(PaymentStatus.REJECTED);
        
        for (PaymentStatus status : PaymentStatus.values()) {
            assertNotNull(status.name());
            assertTrue(status.name().length() > 0);
        }
    }

    @Test
    @DisplayName("MatchEventType valueOf")
    void testMatchEventTypeValueOf() {
        // Act & Assert
        assertEquals(MatchEventType.GOAL, MatchEventType.valueOf("GOAL"));
        assertEquals(MatchEventType.YELLOW_CARD, MatchEventType.valueOf("YELLOW_CARD"));
    }

    @Test
    @DisplayName("InvitationStatus valueOf")
    void testInvitationStatusValueOf() {
        // Act & Assert
        assertEquals(InvitationStatus.PENDING, InvitationStatus.valueOf("PENDING"));
        assertEquals(InvitationStatus.ACCEPTED, InvitationStatus.valueOf("ACCEPTED"));
    }

    @Test
    @DisplayName("PaymentStatus valueOf")
    void testPaymentStatusValueOf() {
        // Act & Assert
        assertEquals(PaymentStatus.PENDING, PaymentStatus.valueOf("PENDING"));
        assertEquals(PaymentStatus.IN_REVIEW, PaymentStatus.valueOf("IN_REVIEW"));
    }
}
