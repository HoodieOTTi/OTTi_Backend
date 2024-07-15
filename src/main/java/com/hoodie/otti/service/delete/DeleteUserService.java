package com.hoodie.otti.service.delete;

import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.exception.delete.DeleteUserException;
import com.hoodie.otti.repository.delete.DeleteUserRepository;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteUserService {

    private final DeleteUserRepository deleteUserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DeleteUserService(DeleteUserRepository deleteUserRepository, JwtTokenProvider jwtTokenProvider) {
        this.deleteUserRepository = deleteUserRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 사용자 계정 탈퇴 메서드
     *
     * @param userEmail 사용자 이메일
     * @param token     사용자 JWT 토큰
     * @return 계정 탈퇴 성공 여부
     * @throws DeleteUserException 삭제 실패 시 발생하는 예외
     */
    public boolean deleteUser(String userEmail, String token) throws DeleteUserException {
        try {
            // JWT 토큰 유효성 검사
            if (!jwtTokenProvider.validateToken(token)) {
                throw new DeleteUserException("Invalid token"); // 토큰이 유효하지 않으면 예외 발생
            }

            // JWT 토큰에서 사용자 이메일 추출
            String tokenUserEmail = jwtTokenProvider.getUserEmailFromToken(token);

            // 요청한 사용자와 JWT 토큰에서 추출한 사용자 이메일 일치 여부 확인
            if (!userEmail.equals(tokenUserEmail)) {
                throw new DeleteUserException("User email does not match with token"); // 요청한 사용자의 이메일과 토큰에서 추출한 이메일이 일치하지 않으면 예외 발생
            }

            // 사용자 정보 조회 (Optional<User>를 반환하도록 가정)
            Optional<User> optionalUser = deleteUserRepository.findByEmail(userEmail);
            User user = optionalUser.orElseThrow(() -> new DeleteUserException("User not found")); // 사용자가 존재하지 않으면 예외 발생

            // 사용자 정보 삭제
            deleteUserRepository.delete(user);

            // 토큰 무효화
            jwtTokenProvider.invalidateToken(token);

            return true; // 삭제 성공

        } catch (DeleteUserException e) {
            throw e; // 이미 정의된 DeleteUserException은 그대로 던집니다.
        } catch (Exception e) {
            // 기타 예외 처리
            throw new DeleteUserException("Unexpected error during user deletion", e);
        }
    }
}


