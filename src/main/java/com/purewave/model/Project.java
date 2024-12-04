package com.purewave.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
@Data
public class Project {

    @Id
    private String id;
    private String title;
    private String userId;
    private String dry_audio;
    private String wet_audio;
}
