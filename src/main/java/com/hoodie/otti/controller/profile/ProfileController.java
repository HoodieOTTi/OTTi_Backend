package com.hoodie.otti.controller.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.service.profile.UserProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import com.hoodie.otti.exception.profile.ErrorResponse;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users/{userId}/profile")
@Validated
public class ProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public ProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PutMapping("/photo")
    public ResponseEntity<Void> updateProfilePhoto(
            @PathVariable Long userId,
            @RequestParam @NotBlank String photoUrl) {
        userProfileService.updateUserProfilePhoto(userId, photoUrl);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/username")
    public ResponseEntity<Void> updateProfileUsername(
            @PathVariable Long userId,
            @RequestParam @NotBlank String newUsername) {
        userProfileService.updateUserProfileUsername(userId, newUsername);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateProfile(
            @PathVariable Long userId,
            @RequestBody @Valid UserProfileDTO userProfileDTO) {
        try {
            userProfileService.updateUserProfile(userId, userProfileDTO.getUsername(), userProfileDTO.getNickname(), userProfileDTO.getProfilePhotoUrl());
            return ResponseEntity.ok().build();
        } catch (UserProfileNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 프로필을 찾을 수 없습니다", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 프로필 업데이트에 실패했습니다", ex);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Get the error message from binding result
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse("요청이 유효하지 않습니다", errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserProfileNotFoundException(UserProfileNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("프로필을 찾을 수 없습니다", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
