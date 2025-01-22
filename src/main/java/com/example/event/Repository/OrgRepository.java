package com.example.event.Repository;

import com.example.event.Entity.OrgEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface OrgRepository extends MongoRepository<OrgEntity,Object> {
    //findByUsername method
    OrgEntity findByUsername(String username);

    //existByUsername method
    boolean existsByUsername(String username);

    boolean existsByPassword(String password);

    //deleteByUsername method
    void deleteByUsername(String username);

}
