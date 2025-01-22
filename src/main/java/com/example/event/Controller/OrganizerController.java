package com.example.event.Controller;

import com.example.event.Criteria.ImageCriteria;
import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import com.example.event.Service.OrgService;
import com.example.event.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/organizer")
public class OrganizerController {

    private static final Logger log = LoggerFactory.getLogger(OrganizerController.class);
    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageCriteria imageCriteria;

    //Controller to get one organizer
    @GetMapping("/GetOrganizer")
    public ResponseEntity<?> getOneOrganizer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        System.out.println(username);
        System.out.println(orgService.existByUsername(username));

        if(orgService.existByUsername(username)){

             return new ResponseEntity<>(orgService.getOneOrganizer(username), HttpStatus.OK);
        }

        System.out.println(username);
        System.out.println(orgService.existByUsername(username));

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    //delete organizer
    @DeleteMapping("/DeleteOrganizer")
    public boolean deleteOrganizer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        orgService.deleteOrganizer(username);
        return true;
    }


    //update organizer
    @PutMapping("/UpdateOrg")
    public boolean updateOrganizer(@RequestBody OrgEntity newOrg) {
        // Get the authenticated user's username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the existing organizer by username
        OrgEntity oldOrg = orgService.getOneOrganizer(username);

        // Update only allowed fields (excluding username, role, followers, and favourites)
        if (newOrg.getPassword() != null && !newOrg.getPassword().isEmpty()) {
            oldOrg.setPassword(newOrg.getPassword());
        }

        if (newOrg.getName() != null) {
            oldOrg.setName(newOrg.getName());
        }

        if (newOrg.getEmail() != null) {
            oldOrg.setEmail(newOrg.getEmail());
        }

        if (newOrg.getMob() != null) {
            oldOrg.setMob(newOrg.getMob());
        }

        if (newOrg.getCompanyName() != null) {
            oldOrg.setCompanyName(newOrg.getCompanyName());
        }

        if (newOrg.getDescription() != null) {
            oldOrg.setDescription(newOrg.getDescription());
        }

        if (newOrg.getProfile() != null) {
            oldOrg.setProfile(newOrg.getProfile());
        }

        if (newOrg.getCity() != null) {
            oldOrg.setCity(newOrg.getCity());
        }

        if (newOrg.getDist() != null) {
            oldOrg.setDist(newOrg.getDist());
        }

        if (newOrg.getState() != null) {
            oldOrg.setState(newOrg.getState());
        }

        // Update the list of images if provided
        if (newOrg.getImages() != null && !newOrg.getImages().isEmpty()) {
            oldOrg.setImages(newOrg.getImages());
        }

        // Save the updated organizer entity
        orgService.saveUpdatedOrganizer(oldOrg);

        return true;
    }


    @PostMapping("/deleteFollowers")
    public ResponseEntity<?> deleteFollowers(@RequestBody String org){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            UserEntity user = userService.findUser(username);
            user.getFollowing().remove(org);
            userService.saveUpdatedUser(user);

            OrgEntity organizer = orgService.getOneOrganizer(org);
            organizer.getFollowers().remove(user.getUsername());
            orgService.saveUpdatedOrganizer(organizer);



            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            log.error("error {}",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }





    //Get all followers
    @GetMapping("/getAllFollowers")
    public ResponseEntity<?> getAllFollowers() {
        try {
            // Get the authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Find the user by username
            OrgEntity org = orgService.getOneOrganizer(username);

            // Get the list of organizer IDs that the user is following
            List<String> followersName = org.getFollowers();

            // Fetch all OrgEntity documents based on the following IDs
            List<UserEntity> followersUser = imageCriteria.getAllUserByUsername(followersName);

            // Return the list of organizers as the response
//            System.out.println("list1 :"+followingIds);
//            System.out.println("list2 :"+followingOrganizers);
            return new ResponseEntity<>(followersUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving following organizers: {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







}
