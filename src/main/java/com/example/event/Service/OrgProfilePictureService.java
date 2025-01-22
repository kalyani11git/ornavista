package com.example.event.Service;

import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.OrgProfilePictureEntity;
import com.example.event.Entity.UserEntity;
import com.example.event.Repository.OrgProfilePictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgProfilePictureService {

    @Autowired
    private OrgProfilePictureRepository orgProfilePictureRepository;

    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;

    //save updated profile picture
    public void saveUpdatedImage(OrgProfilePictureEntity oldImg){
        orgProfilePictureRepository.save(oldImg);
    }
//
    //save profile picture
    //@Transactional
    public void saveImage(OrgProfilePictureEntity newImg, String orgName){

        OrgProfilePictureEntity saved = orgProfilePictureRepository.save(newImg);

        if(orgService.existByUsername(orgName)) {
            OrgEntity organizer = orgService.getOneOrganizer(orgName);
            organizer.setProfile(saved);
            saved.setUsername(organizer.getUsername());
            saveUpdatedImage(saved);
            orgService.saveUpdatedOrganizer(organizer);
        }
        else {
            UserEntity user = userService.findUser(orgName);
            user.setProfile(saved);
            saved.setUsername(user.getUsername());
            saveUpdatedImage(saved);
            userService.saveUpdatedUser(user);
        }

    }

}
