package com.example.event.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection="images")
public class ImageEntity {

    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("filename")
    private String filename;

    @Field("contentType")
    private String contentType;

    @Field("data")
    private byte[] data;

}
