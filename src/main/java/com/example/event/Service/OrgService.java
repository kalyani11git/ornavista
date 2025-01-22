package com.example.event.Service;

import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.OrgProfilePictureEntity;
import com.example.event.Repository.OrgProfilePictureRepository;
import com.example.event.Repository.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Component
public class OrgService {

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private OrgProfilePictureRepository orgProfilePictureRepository;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //To save organizers
    public void saveOrganizer(OrgEntity newOrg){
        newOrg.setPassword(passwordEncoder.encode(newOrg.getPassword()));
//        newOrg.setFollowers(new ArrayList<>());
        newOrg.setRole("ORGANIZER");
        orgRepository.save(newOrg);
    }


    //To save updated organizer
    public void saveUpdatedOrganizer(OrgEntity newOrg){
        orgRepository.save(newOrg);
    }


    //To check organizer exists or not
    public boolean existByUsername(String username){
        if(orgRepository.existsByUsername(username)){
            return true;
        }
        return false;
    }



    //checking exist by password
    public boolean findOrgByPassword(String password){
        if(orgRepository.existsByPassword(password)){
            return true;
        }
        else{
            return false;
        }
    }


    //Get organizer by username
    public OrgEntity getOneOrganizer(String username){
        return orgRepository.findByUsername(username);
    }


    //To delete one organizer
    public void deleteOrganizer(String username){
        orgRepository.deleteByUsername(username);
    }






    //to Save Admin
    public void saveAdmin(OrgEntity newAdmin) {
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        newAdmin.setRole("ADMIN");
        orgRepository.save(newAdmin);
    }


}
