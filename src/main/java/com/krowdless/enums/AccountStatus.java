package com.krowdless.enums;

public enum AccountStatus {
    PENDING,    // email not verified
    ACTIVE,     // normal user
    SUSPENDED,  // banned by admin
    DELETED     // soft-deleted
}
