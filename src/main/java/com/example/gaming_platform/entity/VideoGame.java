package com.example.gaming_platform.entity;

import jakarta.persistence.*;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

@Entity
public class VideoGame {
    @Id
    @GeneratedValue
    private Long id;

    //private variables
    String name;

    Date releaseDate;

    @OneToMany
    List<Category> category;

    //Not Completely Sure
    @OneToMany
    List<Blob> files;

    @OneToMany
    List<Device> system;

    public VideoGame(){}

    //Constructor
    public VideoGame(String name, Date releaseDate, List<Category> category,
                     List<Blob> files, List<Device>system) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.files = files;
        this.system = system;
    }

    //Setters and Getters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public List<Category> getCategory() { return category; }
    public void setCategory(List<Category> category) { this.category = category; }

    public List<Blob> getFiles() { return files; }
    public void setFiles(List<Blob> files) { this.files = files; }

    public List<Device> getSystem(){ return system; }
    public void setSystem(List<Device> system) { this.system = system; }


}
