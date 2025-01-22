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
@Document(collection = "organizers")
public class OrgEntity {


    @Id
    private ObjectId id;

    //not only by specifying @Indexed(unique = true) annotation it will not create index
    //add spring.data.mongodb.auto-index-creation=true in application.properties file
    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;

    private String name;

    private String email;

    @NonNull
    private String mob;


    private String companyName;

    private String description;

    private List<String> followers = new ArrayList<>();

    @DBRef
    private OrgProfilePictureEntity profile;

    private String city;
    private String dist;
    private String state;

    //roles
    private String role;





    @DBRef
    private List<ImageEntity> images = new ArrayList<>();

}
