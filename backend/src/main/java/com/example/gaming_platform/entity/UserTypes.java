package com.example.gaming_platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity representing user types data.
 */
@Entity
public class UserTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String description;

    public String getType(){return type;}
    public String getDescription(){return description;}

    public void setType(String newType){this.type = newType;}
    public void setDescscription(String newDescription){this.description = newDescription;}
}
