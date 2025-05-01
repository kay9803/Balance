package com.judebalance.backend.request;

import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.request.PasswordResetConfirmRequest;
import com.judebalance.backend.request.ChangePasswordRequest;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
