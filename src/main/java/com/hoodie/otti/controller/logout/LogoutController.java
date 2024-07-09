package com.hoodie.otti.controller.logout;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoodie.otti.service.logout.LogoutService;
import com.hoodie.otti.dto.login.BaseResponse;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    /**
     * 로그아웃 처리를 위한 컨트롤러 메서드
     *
     * @param token 사용자 JWT 토큰
     * @return 로그아웃 결과를 포함하는 ResponseEntity
     */
    @ApiOperation(value = "로그아웃", notes = "JWT 토큰을 사용하여 로그아웃")
    @DeleteMapping("/{token}") // http://localhost:8080/api/logout/{token}
    public ResponseEntity<BaseResponse<String>> logout(
            @ApiParam(value = "JWT 토큰", required = true) @PathVariable String token) {

        boolean success = logoutService.logout(token);
        if (success) {
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "로그아웃 되었습니다.", null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "로그아웃 처리 중 오류 발생", null));
        }
    }
}
