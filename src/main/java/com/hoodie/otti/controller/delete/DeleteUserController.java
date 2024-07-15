package com.hoodie.otti.controller.delete;

import com.hoodie.otti.exception.delete.DeleteUserException;
import com.hoodie.otti.service.delete.DeleteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delete-user")
public class DeleteUserController {

    private final DeleteUserService deleteUserService;

    @Autowired
    public DeleteUserController(DeleteUserService deleteUserService) {
        this.deleteUserService = deleteUserService;
    }

    /**
     * 사용자 계정 탈퇴 엔드포인트
     *
     * @param userEmail 사용자 이메일
     * @param token 사용자 JWT 토큰
     * @return ResponseEntity로 탈퇴 결과 반환
     */
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam String userEmail, @RequestHeader("Authorization") String token) {
        try {
            boolean isDeleted = deleteUserService.deleteUser(userEmail, token);
            if (isDeleted) {
                return ResponseEntity.ok("Account successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete account.");
            }
        } catch (DeleteUserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
