package com.example.event.Controller;

import com.example.event.Criteria.ImageCriteria;
import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import com.example.event.Service.ImageService;
import com.example.event.Service.OrgService;
import com.example.event.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
@Component
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageCriteria imageCriteria;




    //Controller to get one user
    @GetMapping("/GetUser")
    public ResponseEntity<?> getOneUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(userService.existUser(username)){
            return new ResponseEntity<>(userService.findUser(username), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    //delete user
    @DeleteMapping("/DeleteUser")
    public boolean deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteUser(username);
        return true;
    }



    //update organizer
    @PutMapping("/UpdateUser")
    public boolean updateUser(@RequestBody UserEntity newUser) {
        // Get the authenticated user's username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the existing user by username
        UserEntity oldUser = userService.findUser(username);

        // Update only allowed fields (excluding username, role, following, and favourites)
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            oldUser.setPassword(newUser.getPassword());
        }

        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getMob() != null) {
            oldUser.setMob(newUser.getMob());
        }

        if (newUser.getCity() != null) {
            oldUser.setCity(newUser.getCity());
        }

        if (newUser.getDist() != null) {
            oldUser.setDist(newUser.getDist());
        }

        if (newUser.getState() != null) {
            oldUser.setState(newUser.getState());
        }

        // Save the updated user entity
        userService.saveUpdatedUser(oldUser);

        return true;
    }





    //to follow one organizer
    @PostMapping("/followOrganizer")
    public ResponseEntity<?> addOneFollower(@RequestBody String orgName){
     try{
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();

         UserEntity user = userService.findUser(username);
         OrgEntity org = orgService.getOneOrganizer(orgName);

//         System.out.println("org :"+org.getName());

         user.getFollowing().add(org.getUsername());
         userService.saveUpdatedUser(user);

//        OrgEntity org = orgService.getOneOrganizer(orgName.getUsername());
         org.getFollowers().add(user.getUsername());
         orgService.saveUpdatedOrganizer(org);

            System.out.println("org1 :"+org.getName());
            System.out.println("org2 :"+orgName);
            System.out.println("org3 :"+org.getFollowers());
            System.out.println("org4 :"+user.getUsername());

         return new ResponseEntity<>(HttpStatus.OK);

     }catch(Exception e){

         log.error("error : {}", e);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

     }

    }


//    @PostMapping("/followOrganizer")
//    public ResponseEntity<?> addOneFollower(@RequestBody String orgName) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//
//            // Fetch the user and the organizer
//            UserEntity user = userService.findUser(username);
//            OrgEntity org = orgService.getOneOrganizer(orgName);
//
//            System.out.println("org1 :"+org.getName());
//            System.out.println("org2 :"+orgName);
//            System.out.println("org3 :"+org.getFollowers());
//            System.out.println("org4 :"+user.getUsername());
//
//
//
//            // Check if the user is already following the organizer
//            if (!user.getFollowing().contains(org.getUsername())) {
//                user.getFollowing().add(orgName);
//                userService.saveUpdatedUser(user);
//            }
//
//            // Check if the organizer already has the user as a follower
//            if (!org.getFollowers().contains(user.getUsername())) {
//                org.getFollowers().add(user.getUsername());
//                orgService.saveUpdatedOrganizer(org);
//            }
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error: {}", e.getMessage(), e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    //to get all following of the user
    @GetMapping("/getFollowing")
    public ResponseEntity<?> getAllFollowingOrg(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<String> following =  userService.findUser(username).getFollowing();
        if(!following.isEmpty()) {
            return new ResponseEntity<>(following,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //adding favourite image
    @PostMapping("/addFavouriteImage")
    public ResponseEntity<?> addToFavourite(@RequestBody ImageEntity favImageId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userService.findUser(username);
//        System.out.println("form controller : "+favImageId);
        ImageEntity img = imageCriteria.getImgById(favImageId.getId());


        if(img!=null){
            user.getFavourite().add(img);
            userService.saveUpdatedUser(user);
//            System.out.println(img);
            return new ResponseEntity<>(img,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


    //delete from favourite
    @PostMapping("/deleteFavourite")
    public ResponseEntity<?> deleteFavImage(@RequestBody ImageEntity favImgId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            UserEntity user = userService.findUser(username);

            ImageEntity img = imageCriteria.getImgById(favImgId.getId());

            user.getFavourite().removeIf(dbRef -> dbRef.getId().equals(img.getId()));

            userService.saveUpdatedUser(user);

            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            log.error("error {}",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //showing favourite images
    @GetMapping("/showAllFavImages")
    public ResponseEntity<?> showFavImg(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user  = userService.findUser(username);
        List<ImageEntity> images = user.getFavourite();
        if(!images.isEmpty()){
            return new ResponseEntity<>(images,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


    @GetMapping("/showOrganizerByImage")
    public ResponseEntity<?> showOrgByImg(@RequestParam("id") String imageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        System.out.println("user : "+username);
        System.out.println("image id : "+imageId);


            ImageEntity img = imageCriteria.getImgById(imageId);
//            System.out.println("image : "+img.getUsername());

            if (img != null) {
                OrgEntity org = orgService.getOneOrganizer(img.getUsername());
                System.out.println("organizer : "+ org.getUsername());
                return new ResponseEntity<>(org, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



    //getting all following
    @GetMapping("/getAllFollowing")
    public ResponseEntity<?> getAllFollowing() {
        try {
            // Get the authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Find the user by username
            UserEntity user = userService.findUser(username);

            // Get the list of organizer IDs that the user is following
            List<String> followingIds = user.getFollowing();

            // Fetch all OrgEntity documents based on the following IDs
            List<OrgEntity> followingOrganizers = imageCriteria.getAllOrgByUsername(followingIds);

            // Return the list of organizers as the response
//            System.out.println("list1 :"+followingIds);
//            System.out.println("list2 :"+followingOrganizers);
            return new ResponseEntity<>(followingOrganizers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving following organizers: {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    // Fetch user or organizer based on the role
    @GetMapping("/getUserOrOrganizer")
    public ResponseEntity<?> getUserOrOrganizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (userService.existUser(username)) {
            UserEntity user = userService.findUser(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else if (orgService.existByUsername(username)) {
            OrgEntity organizer = orgService.getOneOrganizer(username);
            return new ResponseEntity<>(organizer, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }





}





