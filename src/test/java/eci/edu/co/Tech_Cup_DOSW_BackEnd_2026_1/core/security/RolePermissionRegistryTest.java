package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("RolePermissionRegistry Tests")
class RolePermissionRegistryTest {

    @Test
    @DisplayName("Should instantiate RolePermissionRegistry successfully")
    void testInstantiateRolePermissionRegistry() {

        RolePermissionRegistry registry = new RolePermissionRegistry();

        assertNotNull(registry, "RolePermissionRegistry should not be null");
    }
}
