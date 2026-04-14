package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enums Tests")
class EnumsTest {

    @Test
    @DisplayName("Position enum should have all expected values")
    void testPositionEnum() {
        assertEquals(4, Position.values().length);
        assertEquals(Position.FORWARD, Position.valueOf("FORWARD"));
        assertEquals(Position.MIDFIELDER, Position.valueOf("MIDFIELDER"));
        assertEquals(Position.DEFENDER, Position.valueOf("DEFENDER"));
        assertEquals(Position.GOALKEEPER, Position.valueOf("GOALKEEPER"));
    }

    @Test
    @DisplayName("Role enum should have all expected values")
    void testRoleEnum() {
        assertTrue(Role.values().length > 0);
        assertNotNull(Role.valueOf("PLAYER"));
        assertNotNull(Role.valueOf("CAPTAIN"));
    }

    @Test
    @DisplayName("TournamentStatus enum should have all expected values")
    void testTournamentStatusEnum() {
        assertTrue(TournamentStatus.values().length > 0);
        assertNotNull(TournamentStatus.valueOf("DRAFT"));
        assertNotNull(TournamentStatus.valueOf("ACTIVE"));
        assertNotNull(TournamentStatus.valueOf("IN_PROGRESS"));
        assertNotNull(TournamentStatus.valueOf("FINISHED"));
    }

    @Test
    @DisplayName("MatchPhase enum should have all expected values")
    void testMatchPhaseEnum() {
        assertTrue(MatchPhase.values().length > 0);
        assertNotNull(MatchPhase.valueOf("GROUP"));
        assertNotNull(MatchPhase.valueOf("QUARTER_FINAL"));
        assertNotNull(MatchPhase.valueOf("SEMI_FINAL"));
        assertNotNull(MatchPhase.valueOf("FINAL"));
    }

    @Test
    @DisplayName("Program enum should have all expected values")
    void testProgramEnum() {
        assertTrue(Program.values().length > 0);
    }

    @Test
    @DisplayName("UserType enum should have all expected values")
    void testUserTypeEnum() {
        assertTrue(UserType.values().length > 0);
    }

    @Test
    @DisplayName("Position can be compared")
    void testPositionComparison() {
        Position pos1 = Position.FORWARD;
        Position pos2 = Position.FORWARD;
        Position pos3 = Position.MIDFIELDER;

        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
    }
}
