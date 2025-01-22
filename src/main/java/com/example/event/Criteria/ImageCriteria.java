package com.example.event.Criteria;

import com.example.event.Entity.ImageEntity;
import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Component
public class ImageCriteria {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ImageEntity getImgById(String imgId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(imgId
        ));  // Safely convert to ObjectId
        return mongoTemplate.findOne(query, ImageEntity.class);
    }

    public List<OrgEntity> getAllOrgByUsername(List<String> usernames) {
        // Create a query to find OrgEntity documents where the username field is in the provided list
        Query query = new Query();
        query.addCriteria(Criteria.where("username").in(usernames));

        // Execute the query and return the list of OrgEntity documents
        return mongoTemplate.find(query, OrgEntity.class);
    }

    public List<UserEntity> getAllUserByUsername(List<String> usernames) {
        // Create a query to find OrgEntity documents where the username field is in the provided list
        Query query = new Query();
        query.addCriteria(Criteria.where("username").in(usernames));

        // Execute the query and return the list of OrgEntity documents
        return mongoTemplate.find(query, UserEntity.class);
    }



}
