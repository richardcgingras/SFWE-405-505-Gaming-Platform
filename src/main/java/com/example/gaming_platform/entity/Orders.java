package com.example.gaming_platform.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Entity representing orders data.
 */
@Entity
public class Orders {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private UserProfile destinationAccount;

    @ManyToOne
    private VideoGame game;

    private Date purchaseTimestamp;

    private Boolean paymentProcessed;

    // Getters
/**
 * Gets the destination account.
 *
 * @return the destination account
 */
    public UserProfile getDestinationAccount(){return destinationAccount;}
    public VideoGame getGame(){return game;}
    public Date getDate(){return purchaseTimestamp;}
    public Boolean getPaymentProcessed(){return paymentProcessed;}

    // Setters
    public void setDestinationAccount(UserProfile newDestinationAccount){destinationAccount = newDestinationAccount;}
    public void setGame(VideoGame newGame){game = newGame;}
    public void setDate(Date newDate){purchaseTimestamp = newDate;}
    public void setPaymentProcessed(Boolean newBool){paymentProcessed = newBool;}
}
