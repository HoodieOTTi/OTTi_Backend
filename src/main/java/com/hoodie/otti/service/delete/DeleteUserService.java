package com.hoodie.otti.service.delete;

import com.hoodie.otti.repository.delete.DeleteUserRepository;
import com.hoodie.otti.repository.login.UserRepository;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserService {

    private final DeleteUserRepository deleteuserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DeleteUserService(DeleteUserRepository deleteuserRepository, JwtTokenProvider jwtTokenProvider) {
        this.deleteuserRepository = deleteuserRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 사용자 계정 탈퇴 메서드
     *
     * @param userEmail 사용자 이메일
     * @param token 사용자 JWT 토큰
     * @return 계정 탈퇴 성공 여부
     */
    public boolean deleteUser(String userEmail, String token) {
        try {
            // 사용자 정보 삭제
            deleteuserRepository.deleteByEmail(userEmail);
            // 토큰 무효화
            jwtTokenProvider.invalidateToken(token);
            return true;
        } catch (Exception e) {
            // 예외 처리 로직
            return false;
        }
    }
}
