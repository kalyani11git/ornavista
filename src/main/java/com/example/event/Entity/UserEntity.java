package com.example.event.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection="users")
public class UserEntity {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String username;

    @DBRef
    private OrgProfilePictureEntity profile;

    @NonNull
    private String password;

    private String name;

    private String email;
    private String mob;

    private String city;
    private String dist;
    private String state;

    //roles
    private String role;



    private List<String> following = new ArrayList<>();


    @DBRef
    private List<ImageEntity> favourite = new ArrayList<>();



}
