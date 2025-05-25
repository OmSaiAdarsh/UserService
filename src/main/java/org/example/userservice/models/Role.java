package org.example.userservice.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Role extends BaseModel{
    private String value;

}

/*
* user : role => m:m
* 1:m
* m:1
*
* */
