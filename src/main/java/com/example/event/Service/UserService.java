package com.example.event.Service;

import com.example.event.Entity.UserEntity;
import com.example.event.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

//import static com.example.event.Service.OrgService.passwordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //add new user
    public void saveUser(UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }

   //add updated user
   public void saveUpdatedUser(UserEntity user){
        userRepository.save(user);
   }



    //find user by username
    public UserEntity findUser(String username){
        return userRepository.findByUsername(username);
    }



    public UserEntity findUserByPassword(String password){
        String pass = passwordEncoder.encode(password);
       return userRepository.findByPassword(pass);
    }


    //to check user exists or not
    public boolean existUser(String username){
        if(userRepository.existsByUsername(username)){
            return true;
        }
        return false;
    }


    //delete user
    public void deleteUser(String username){
         userRepository.deleteByUsername(username);
    }




}
