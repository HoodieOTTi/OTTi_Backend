package com.hoodie.otti.controller.profile;

import com.hoodie.otti.dto.profile.UserProfileDTO;
import com.hoodie.otti.exception.profile.ErrorResponse;
import com.hoodie.otti.exception.profile.UserProfileNotFoundException;
import com.hoodie.otti.service.profile.UserProfileService;
import com.hoodie.otti.util.login.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users/{userId}/profile")
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, JwtTokenProvider jwtTokenProvider) {
        this.userProfileService = userProfileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PutMapping("/update")
    public ResponseEntity<Void> updateProfile(
            @RequestBody @Valid UserProfileDTO userProfileDTO,
            HttpServletRequest request) {
        try {
            // 요청 헤더에서 토큰을 가져옵니다.
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }
            // 토큰을 사용하여 프로필을 업데이트합니다.
            userProfileService.updateUserProfile(token, userProfileDTO);
            return ResponseEntity.ok().build();
        } catch (UserProfileNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 프로필을 찾을 수 없습니다", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 프로필 업데이트에 실패했습니다", ex);
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
