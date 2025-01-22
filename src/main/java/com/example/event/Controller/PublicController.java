package com.example.event.Controller;

import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import com.example.event.Service.*;
import com.example.event.Util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import static com.example.event.Service.UserService.passwordEncoder;

@Component
@Slf4j
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {

    @Autowired
    private OrgService orgService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomOrganizerServiceImpl customOrganizerService;

    @Autowired
    private CustomUserServiceImpl customUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;



    // Sign up organizer
    @PostMapping("/Signup")
    public boolean signup(@RequestBody OrgEntity newUser) {
        orgService.saveOrganizer(newUser);
        return true;
    }

    // Login organizer
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody OrgEntity newUser) {

        if (newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
            return new ResponseEntity<>("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!orgService.existByUsername(newUser.getUsername())) {
            return new ResponseEntity<>("Incorrect username", HttpStatus.UNAUTHORIZED);
        }

        String password =  newUser.getPassword();

        boolean matches = passwordEncoder.matches(password,orgService.getOneOrganizer(newUser.getUsername()).getPassword());

        if(orgService.existByUsername(newUser.getUsername()) && matches){

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword()));
                UserDetails userDetails = customOrganizerService.loadUserByUsername(newUser.getUsername());

                String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);

                System.out.println(role);
                String jwt = jwtUtil.generateToken(userDetails.getUsername(), role.toString());
                System.out.println(jwt);
                System.out.println(userDetails);
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);

        }

    }


    //get all images
    @GetMapping("/GetAllImages")
    public ResponseEntity<?> getAllImages(){
        List<ImageEntity> images = imageService.ShowAllImages();
        if(images.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(images,HttpStatus.FOUND);
    }




    //User part started


    // Sign up user
    @PostMapping("/SignupUser")
    public boolean signupUser(@RequestBody UserEntity newUser) {
        userService.saveUser(newUser);
        return true;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Login user
    @PostMapping("/loginUser")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity newUser) {

        if (newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
            return new ResponseEntity<>("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!userService.existUser(newUser.getUsername())) {
            return new ResponseEntity<>("Incorrect username", HttpStatus.UNAUTHORIZED);
        }

         String password =  newUser.getPassword();

         boolean matches = passwordEncoder.matches(password,userService.findUser(newUser.getUsername()).getPassword());

        if(userService.existUser(newUser.getUsername()) && matches){
            try {

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword()));
                UserDetails userDetails = customUserService.loadUserByUsername(newUser.getUsername());

                System.out.println("userDetails : "+userDetails.getPassword());

                String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);

                System.out.println(role);
                String jwt = jwtUtil.generateToken(userDetails.getUsername(), role.toString());
                System.out.println(jwt);
                System.out.println(userDetails);

                return new ResponseEntity<>(jwt, HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
            }


        }
        else{
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }

    }




}
