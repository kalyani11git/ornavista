package com.example.event.Repository;

import com.example.event.Entity.OrgProfilePictureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrgProfilePictureRepository extends MongoRepository<OrgProfilePictureEntity,String> {
}
