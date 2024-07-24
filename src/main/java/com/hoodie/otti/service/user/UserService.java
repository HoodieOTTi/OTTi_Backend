//package com.hoodie.otti.service.user;
//
//import com.hoodie.otti.dto.login.RegisterRequest;
//import com.hoodie.otti.model.login.User1;
//import com.hoodie.otti.repository.user.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    public Optional<User1> findByUserId(Long UserId) {
//        return userRepository.findByUserId(UserId);
//    }
//
//    public Optional<User1> findByEmail(String email) {
//        return userRepository.findByUserEmail(email);
//    }
//
//    public User1 saveUser(User1 user) {
//        return userRepository.save(user);
//    }
//
//    public void saveUser(RegisterRequest registerRequest) {
//        User1 user = new User1();
//        user.setUserId(registerRequest.getUserId());
//        user.setNickname(registerRequest.getNickname());
//        user.setUserEmail(registerRequest.getEmail());
//        userRepository.save(user);
//    }
//
//}
