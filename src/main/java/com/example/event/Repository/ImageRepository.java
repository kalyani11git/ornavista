package com.example.event.Repository;

import com.example.event.Entity.ImageEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<ImageEntity,String> {

}
