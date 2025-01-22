package com.example.event.Controller;

import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgProfilePictureEntity;
import com.example.event.Service.ImageService;
import com.example.event.Service.OrgProfilePictureService;
import com.example.event.Service.OrgService;
import com.example.event.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "http://localhost:5173")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private OrgProfilePictureService orgProfilePictureService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;


    //adding new image
    @PostMapping("/AddImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        try {
            // Check if file is empty
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String organizer = authentication.getName();

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected to upload");
            }

            // Get file details
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] data = file.getBytes();


            // Log the file details
            System.out.println("File Name: " + fileName);
            System.out.println("Content Type: " + contentType);
            System.out.println("File Size: " + data.length);

            // Create an ImageEntity object to store in the database
            ImageEntity newImage = new ImageEntity();
            newImage.setFilename(fileName);
            newImage.setContentType(contentType);
            newImage.setData(data);


            // Save the ImageEntity object using your service or repository layer
            // Assume imageService is autowired and handles saving the entity
            imageService.saveImage(newImage,organizer);

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }


    //adding profile picture
    @PostMapping("/AddProfilePicture")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {

        try {
            // Check if file is empty
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();



            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected to upload");
            }

            // Get file details
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] data = file.getBytes();


            // Log the file details
            System.out.println("File Name: " + fileName);
            System.out.println("Content Type: " + contentType);
            System.out.println("File Size: " + data.length);

            // Create an ImageEntity object to store in the database
            OrgProfilePictureEntity newImage = new OrgProfilePictureEntity();
            newImage.setFilename(fileName);
            newImage.setContentType(contentType);
            newImage.setData(data);


            // Save the ImageEntity object using your service or repository layer
            // Assume imageService is autowired and handles saving the entity
            orgProfilePictureService.saveImage(newImage,user);

            if(orgService.existByUsername(user)){
                orgService.getOneOrganizer(user).setProfile(newImage);
            }
            else if(userService.existUser(user)){
                userService.findUser(user).setProfile(newImage);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User bot found");
            }

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }




}
