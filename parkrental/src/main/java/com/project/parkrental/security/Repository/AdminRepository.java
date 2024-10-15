package com.project.parkrental.security;

public interface AdminRepository {
    Optional<Admin> findByUsername();

}
