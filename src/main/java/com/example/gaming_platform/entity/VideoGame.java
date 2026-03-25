package com.example.gaming_platform.entity;

// import java.sql.Blob; // commented out because Blob is not an @Entity and caused JPA errors
import java.util.Date;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class VideoGame {
    @Id
    @GeneratedValue
    private Long id;

    // private variables
    String name;

    Date releaseDate;

    float price;

    @ManyToMany
    private List<Category> category;


    /*
     * ORIGINAL CODE:
     *
     * @OneToMany
     * List<Blob> files;
     *
     * This caused the application to fail at startup because:
     * - Blob is NOT an @Entity
     * - @OneToMany only works with other entity classes
     *
     * Since we are not actually modeling a File entity yet,
     * I changed this to store file names/paths as simple Strings instead.
     * This lets the app run without redesigning the data model.
     */

    @ElementCollection
    private List<String> files;  // temporarily storing file names instead of Blob objects


    @Enumerated(EnumType.STRING)
    private Device system;

    private float size; // in GB

    public VideoGame(){}

    // Constructor
    /*
     * ORIGINAL CONSTRUCTOR:
     *
     * public VideoGame(String name, Date releaseDate, Category category,
     *                  List<Blob> files, Device system)
     *
     * Changed List<Blob> to List<String> for the same reason explained above.
     */

    public VideoGame(String name, Date releaseDate, List<Category> category,
                     List<String> files, Device system, float price, float size) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.files = files;
        this.system = system;
        this.size = size;
        this.price = price;
    }

    // Setters and Getters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public List<Category> getCategory() { return category; }
    public void setCategory(List<Category> category) { this.category = category; }

    public float getSize() { return size; }
    public void setSize(float size) { this.size = size; }

    /*
     * ORIGINAL:
     * public List<Blob> getFiles()
     * public void setFiles(List<Blob> files)
     *
     * Updated to List<String> so JPA doesn't try to treat Blob as an entity.
     */
    public List<String> getFiles() { return files; }
    public void setFiles(List<String> files) { this.files = files; }

    public Device getSystem(){ return system; }
    public void setSystem(Device system) { this.system = system; }
}
