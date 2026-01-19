package com.krowdless.usersmangement.dto;

import jakarta.validation.constraints.*;
import com.krowdless.usersmangement.entity.UserRole;

public class UserRegisterRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Email
    private String email;

    @Pattern(regexp="\\d{10}")
    private String phone;

    private UserRole role = UserRole.USER;  // default USER

    // getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
