package com.example.gaming_platform.entity;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Entity representing orders data.
 */
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserProfile destinationAccount;

    @ManyToOne
    private VideoGame game;

    private Calendar purchaseTimestamp;

    private Boolean paymentProcessed;

    // constructors
    public Orders(){}

    public Orders(UserProfile destinationAccount, VideoGame game, Calendar purchaseTimestamp, Boolean paymentProcessed) {
        this.destinationAccount = destinationAccount;
        this.game = game;
        this.purchaseTimestamp = purchaseTimestamp;
        this.paymentProcessed = paymentProcessed;
    }

    // Getters
/**
 * Gets the destination account.
 *
 * @return the destination account
 */
    public UserProfile getDestinationAccount(){return destinationAccount;}
    public VideoGame getGame(){return game;}
    public Calendar getDate(){return purchaseTimestamp;}
    public Boolean getPaymentProcessed(){return paymentProcessed;}

    // Setters
    public void setDestinationAccount(UserProfile newDestinationAccount){destinationAccount = newDestinationAccount;}
    public void setGame(VideoGame newGame){game = newGame;}
    public void setDate(Calendar newDate){purchaseTimestamp = newDate;}
    public void setPaymentProcessed(Boolean newBool){paymentProcessed = newBool;}
}
