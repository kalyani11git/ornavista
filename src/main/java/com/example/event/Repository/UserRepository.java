package com.example.event.Repository;

import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity,Object> {
    UserEntity findByUsername(String username);

    boolean existsByPassword(String password);

    UserEntity findByPassword(String password);

    //existByUsername method
    boolean existsByUsername(String username);

    //deleteByUsername method
    void deleteByUsername(String username);


}
