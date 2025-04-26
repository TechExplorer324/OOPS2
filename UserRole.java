/**
 * Enumeration for different user roles within the system.
 * Each role represents a specific level of access or function within the system.
 */
enum UserRole {
    /**
     * Admin role with the highest level of access and control over the system.
     */
    ADMIN, 

    /**
     * Staff role with permissions to assist with managing the system but not full administrative access.
     */
    STAFF, 

    /**
     * Regular user role with basic access to the system's features.
     */
    REGULAR_USER, 

    /**
     * Tenant Manager role, responsible for managing tenant-specific actions and permissions within the system.
     */
    TENANT_MANAGER
}

