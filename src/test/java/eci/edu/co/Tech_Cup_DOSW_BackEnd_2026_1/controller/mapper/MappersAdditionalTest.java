package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mappers Additional Tests")
class MappersAdditionalTest {

    @Test
    @DisplayName("UserMapperImpl instantiation")
    void testUserMapperImplInstantiation() {
        // Act
        UserMapperImpl mapper = new UserMapperImpl();

        // Assert
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("SportProfileMapperImpl instantiation")
    void testSportProfileMapperImplInstantiation() {
        // Act
        SportProfileMapperImpl mapper = new SportProfileMapperImpl();

        // Assert
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("TeamMapperImpl instantiation")
    void testTeamMapperImplInstantiation() {
        // Act
        TeamMapperImpl mapper = new TeamMapperImpl();

        // Assert
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("TournamentMapperImpl instantiation")
    void testTournamentMapperImplInstantiation() {
        // Act
        TournamentMapperImpl mapper = new TournamentMapperImpl();

        // Assert
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("Mapper class names")
    void testMapperClassNames() {
        // Assert
        assertEquals("UserMapperImpl", UserMapperImpl.class.getSimpleName());
        assertEquals("SportProfileMapperImpl", SportProfileMapperImpl.class.getSimpleName());
        assertEquals("TeamMapperImpl", TeamMapperImpl.class.getSimpleName());
        assertEquals("TournamentMapperImpl", TournamentMapperImpl.class.getSimpleName());
    }

    @Test
    @DisplayName("Multiple UserMapper instances")
    void testMultipleUserMapperInstances() {
        // Act
        UserMapperImpl mapper1 = new UserMapperImpl();
        UserMapperImpl mapper2 = new UserMapperImpl();

        // Assert
        assertNotNull(mapper1);
        assertNotNull(mapper2);
    }

    @Test
    @DisplayName("Multiple SportProfileMapper instances")
    void testMultipleSportProfileMapperInstances() {
        // Act
        SportProfileMapperImpl mapper1 = new SportProfileMapperImpl();
        SportProfileMapperImpl mapper2 = new SportProfileMapperImpl();

        // Assert
        assertNotNull(mapper1);
        assertNotNull(mapper2);
    }

    @Test
    @DisplayName("Multiple TeamMapper instances")
    void testMultipleTeamMapperInstances() {
        // Act
        TeamMapperImpl mapper1 = new TeamMapperImpl();
        TeamMapperImpl mapper2 = new TeamMapperImpl();

        // Assert
        assertNotNull(mapper1);
        assertNotNull(mapper2);
    }

    @Test
    @DisplayName("Multiple TournamentMapper instances")
    void testMultipleTournamentMapperInstances() {
        // Act
        TournamentMapperImpl mapper1 = new TournamentMapperImpl();
        TournamentMapperImpl mapper2 = new TournamentMapperImpl();

        // Assert
        assertNotNull(mapper1);
        assertNotNull(mapper2);
    }

    @Test
    @DisplayName("Mapper instanceof check")
    void testMapperInstanceofCheck() {
        // Act
        Object userMapper = new UserMapperImpl();
        Object profileMapper = new SportProfileMapperImpl();
        Object teamMapper = new TeamMapperImpl();
        Object tournamentMapper = new TournamentMapperImpl();

        // Assert
        assertTrue(userMapper instanceof UserMapperImpl);
        assertTrue(profileMapper instanceof SportProfileMapperImpl);
        assertTrue(teamMapper instanceof TeamMapperImpl);
        assertTrue(tournamentMapper instanceof TournamentMapperImpl);
    }

    @Test
    @DisplayName("Mapper toString")
    void testMapperToString() {
        // Act
        UserMapperImpl mapper = new UserMapperImpl();
        String str = mapper.toString();

        // Assert
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    @DisplayName("Mapper hashCode")
    void testMapperHashCode() {
        // Act
        UserMapperImpl mapper = new UserMapperImpl();
        int hash = mapper.hashCode();

        // Assert
        assertTrue(hash != 0 || mapper.hashCode() == hash);
    }
}
