package org.example.userservice.models;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Token extends BaseModel{
    public String value;
    public Date expiryAt;
    @ManyToOne
    User user;
}


/*
* user : token => 1:m
* 1 : m
* 1 : 1
* */