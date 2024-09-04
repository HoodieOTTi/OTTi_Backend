package com.hoodie.otti.controller.profile;

import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.ErrorResponse;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.service.profile.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/users/profile")
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateProfile(
            Principal principal,
            @ModelAttribute @Valid UserProfileDTO userProfileDTO,
            @ModelAttribute UploadImageRequestDto requestDto) {
        try {
            userProfileService.updateUserProfile(principal, userProfileDTO, requestDto);
            return ResponseEntity.ok().build();
        } catch (UserProfileNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 프로필을 찾을 수 없습니다", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 프로필 업데이트에 실패했습니다", ex);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserProfileDTO> getProfile(Principal principal) {
        return ResponseEntity.ok().body(userProfileService.getUserProfileDTOByPrincipal(principal));
    }

    @GetMapping("/userid")
    public ResponseEntity<Long> getUserId(Principal principal) {
        try {
            Long userId = Long.parseLong(principal.getName()); // Principal에서 사용자 ID를 가져옵니다.
            return ResponseEntity.ok(userId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 ID를 가져오는 데 실패했습니다", ex);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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