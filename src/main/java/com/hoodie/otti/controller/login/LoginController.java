package com.hoodie.otti.controller.login;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.hoodie.otti.service.login.LoginService;
import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;
import com.hoodie.otti.dto.login.BaseResponse;

@RestController
public class LoginController {

    private final LoginService loginService;

    // LoginService를 주입받는 생성자
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 카카오 로그인 처리를 위한 컨트롤러 메서드
     *
     * @param code 카카오 인증 코드
     * @return 카카오 로그인 응답을 포함하는 ResponseEntity
     */
    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인")
    @GetMapping("login/kakaologin") // localhost:8080/comm/login/kakaologin
    public ResponseEntity<BaseResponse<UserKakaoLoginResponseDto>> kakaoCallback(
            @ApiParam(value = "kakao auth code", required = true) @RequestParam String code) {

        try {
            // 카카오 AccessToken을 받아옵니다.
            String accessToken = loginService.getKaKaoAccessToken(code);

            // AccessToken을 이용해 카카오 로그인을 처리하고, 응답을 받아옵니다.
            UserKakaoLoginResponseDto responseDto = loginService.kakaoLogin(accessToken);

            // 정상적인 응답을 ResponseEntity에 담아 반환합니다.
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "요청에 성공했습니다.", responseDto));
        } catch (Exception e) {
            // 오류 발생 시, 오류 응답을 ResponseEntity에 담아 반환합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "카카오 로그인 처리 중 오류 발생", null));
        }
    }
}
