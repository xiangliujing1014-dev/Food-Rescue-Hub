package com.frh.backend.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1 to 5
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    //who made the review
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // review which one
    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

}