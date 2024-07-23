package com.hoodie.otti.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoodie.otti.dto.login.KakaoInfo;
import com.hoodie.otti.dto.login.MemberResponse;
import com.hoodie.otti.service.user.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class KakaoOAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userinfoUri;

    @Value("${kakao.logout-redirect-uri}")
    private String logoutRedirectUri;

    private final OAuthService oAuthService;
    private final RestTemplate restTemplate;

    @Autowired
    public KakaoOAuth2Controller(OAuthService oAuthService, RestTemplate restTemplate) {
        this.oAuthService = oAuthService;
        this.restTemplate = restTemplate;
    }


    // 카카오에 인가코드 요청
    @GetMapping("/login/kakaologin")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+clientId);
        url.append("&redirect_uri="+redirectUri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    // 세션 로그인
    // Service에 작성한 함수를 바탕으로 Controller를 작성하고, 세션에 값을 담아 로그인을 구현
    @RequestMapping(value = "/kakao/callback", method = RequestMethod.GET)
    public String kakaoCallback(@RequestParam("accessToken") String accessToken, HttpSession session){
//         SETP1 : 인가코드 받기
//         (카카오 인증 서버는 서비스 서버의 Redirect URI로 인가 코드를 전달합니다.)
//         System.out.println(accessToken);

//         STEP2: 인가코드를 기반으로 토큰(Access Token) 발급
//        String accessToken = null;
        try {
            accessToken = oAuthService.getAccessToken(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("엑세스 토큰  "+accessToken);

        // STEP3: 토큰를 통해 사용자 정보 조회
        KakaoInfo kakaoInfo = null;
        try {
            kakaoInfo = oAuthService.getKakaoInfo(accessToken);
//            kakaoInfo = oAuthService.getKakaoInfo(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("이메일 확인 "+kakaoInfo.getEmail());

        // STEP4: 카카오 사용자 정보 확인
        MemberResponse kakaoMember = oAuthService.ifNeedKakaoInfo(kakaoInfo);

        // STEP5: 강제 로그인
        // 세션에 회원 정보 저장 & 세션 유지 시간 설정
        if (kakaoMember != null) {
            session.setAttribute("loginMember", kakaoMember);
            System.out.println("User info stored in session: " + kakaoMember); // 로그 확인
            // session.setMaxInactiveInterval( ) : 세션 타임아웃을 설정하는 메서드
            // 로그인 유지 시간 설정 (1800초 == 30분)
            session.setMaxInactiveInterval(60 * 30);
            // 로그아웃 시 사용할 카카오토큰 추가
            session.setAttribute("kakaoToken", accessToken);
        }

        return "redirect:/";
    }

    @GetMapping("/api/user/info")
    public ResponseEntity<MemberResponse> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");

        try {
            KakaoInfo kakaoInfo = oAuthService.getKakaoInfo(accessToken);
            MemberResponse member = new MemberResponse(kakaoInfo.getId(), kakaoInfo.getNickname(), kakaoInfo.getEmail());
            return ResponseEntity.ok(member);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/kakaoLogout")
    public void kakaoLogout(HttpServletResponse response) throws IOException {
        String logoutUrl = oAuthService.buildKakaoLogoutUrl();
        response.sendRedirect(logoutUrl);
    }

//    @GetMapping("/api/logout")
//    public String logout(HttpServletRequest request) {
//        // 세션에서 엑세스 토큰을 가져옴
////        String accessToken = (String) session.getAttribute("kakaoToken");
//
//        // 액세스 토큰을 헤더에서 추출
////        String accessToken = null;
////        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
////            accessToken = authorizationHeader.substring(7); // "Bearer " 문자열 제거
////        }
//
//        // 엑세스 토큰이 존재할 경우 로그아웃 처리
////        if (accessToken != null && !"".equals(accessToken)) {
////            try {
////                // 카카오 로그아웃 처리
////                oAuthService.kakaoDisconnect(accessToken);
////            } catch (JsonProcessingException e) {
////                // 예외 처리
////                e.printStackTrace();
////            }
////            // 세션에서 관련 정보 삭제
////            session.removeAttribute("kakaoToken");
////            session.removeAttribute("loginMember");
////        } else {
////            System.out.println("accessToken is null");
////        }
//
//        // 세션에서 관련 정보 삭제
////            session.removeAttribute("kakaoToken");
////            session.removeAttribute("loginMember");
//
//
//        // 세션 무효화
////        session.invalidate();
//
//        request.getSession().invalidate();
//
//        // 로그아웃 후 리다이렉트할 URL 설정
//        return "redirect:/";
//    }

    @GetMapping("/api/logout")
    public String logout(HttpServletRequest request) {
        // 세션에서 엑세스 토큰을 가져옴
        String accessToken = (String) request.getSession().getAttribute("kakaoToken");

        // 액세스 토큰이 존재할 경우 로그아웃 처리
        if (accessToken != null && !accessToken.isEmpty()) {
            try {
                // 카카오 로그아웃 처리
                oAuthService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                // 예외 처리
                e.printStackTrace();
                // 로그아웃 처리 실패 시, 사용자에게 에러 메시지를 보여줄 수도 있습니다.
            }

            // 세션에서 관련 정보 삭제
            request.getSession().removeAttribute("kakaoToken");
            request.getSession().removeAttribute("loginMember");
        } else {
            System.out.println("accessToken is null or empty");
        }

        // 세션 무효화
        request.getSession().invalidate();

        // 로그아웃 후 리다이렉트할 URL 설정
        return "redirect:" + oAuthService.buildKakaoLogoutUrl();
    }



    @GetMapping("/api/unlink")
    public ResponseEntity<String> unlink(HttpServletRequest request) {
        // Authorization 헤더에서 액세스 토큰을 추출
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // "Bearer "를 제거하여 토큰만 추출
        } else {
            return ResponseEntity.badRequest().body("Error: access_token is required");
        }

        try {
            oAuthService.unlinkUser(accessToken);
            request.getSession().invalidate();
            return ResponseEntity.ok("User successfully unlinked");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}

