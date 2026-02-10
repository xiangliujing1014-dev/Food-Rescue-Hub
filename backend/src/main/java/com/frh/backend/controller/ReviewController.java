package com.frh.backend.controller;

import com.frh.backend.Model.*;
import com.frh.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    //accept json data from app
    public static class ReviewRequest {
        public Long userId;
        public Long listingId;
        public int rating;
        public String comment;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest request) {


        User user = userRepository.findById(request.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Listing listing = listingRepository.findById(request.listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        // save review
        Review review = new Review();
        review.setUser(user);
        review.setListing(listing);
        review.setRating(request.rating);
        review.setComment(request.comment);

        reviewRepository.save(review);

        return ResponseEntity.ok("Review added successfully");
    }
}