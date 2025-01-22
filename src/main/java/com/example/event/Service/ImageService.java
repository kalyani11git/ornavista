package com.example.event.Service;

import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgEntity;
import com.example.event.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private OrgService orgService;

    @Autowired
    private ImageRepository imageRepository;

    //save updated image
    public void saveUpdatedImage(ImageEntity oldImg){
        imageRepository.save(oldImg);
    }

    //to adding one image
//    @Transactional
    public void saveImage(ImageEntity newImg, String orgName){
        OrgEntity organizer = orgService.getOneOrganizer(orgName);
        ImageEntity saved = imageRepository.save(newImg);
        organizer.getImages().add(saved);
        saved.setUsername(organizer.getUsername());
        saveUpdatedImage(saved);
        orgService.saveUpdatedOrganizer(organizer);
    }

    //find image by id
    public Optional<ImageEntity> findImgById(String imgId){
        Optional<ImageEntity> img =  imageRepository.findById(imgId);
        return img;
    }

    public List<ImageEntity> ShowAllImages(){
       return imageRepository.findAll();
    }

}
