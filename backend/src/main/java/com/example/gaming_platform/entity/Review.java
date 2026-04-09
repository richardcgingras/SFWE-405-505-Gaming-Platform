package com.example.gaming_platform.entity;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing review data.
 */
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile from;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private VideoGame game;

    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar sent;
    private int rating;

/**
 * Creates a new Review instance.
 */
    public Review() {}

    public Review(UserProfile from, VideoGame game, String comments, int rating) {
        this.from = from;
        this.game = game;
        this.comments = comments;
        this.rating = rating;
        this.sent = Calendar.getInstance();
    }

    //setters
/**
 * Sets the from.
 *
 * @param from the from
 */
    public void setFrom(UserProfile from) { this.from = from; }
    public void setGame(VideoGame game) { this.game = game; }
    public void setComments(String comments) { this.comments = comments; }
    public void setSent(Calendar sent) { this.sent = sent; }
    public void setRating(int rating) { this.rating = rating; }

    //getters
    public Long getId() { return id; }
    public UserProfile getFrom() { return from; }
    public VideoGame getGame() { return game; }
    public String getComments() { return comments; }
    public Calendar getSent() { return sent; }
    public int getRating() { return rating; }
}
