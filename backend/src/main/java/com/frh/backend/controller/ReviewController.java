package com.frh.backend.controller;

import com.frh.backend.Model.*;
import com.frh.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    // accept json data from app
    public static class ReviewRequest {
        public Long userId;
        public Long listingId;
        public int rating;
        public String comment;
    }

    // all users can get comment
    @GetMapping("/list/{listingId}")
    public ResponseEntity<?> getReviewsByListing(@PathVariable Long listingId) {
        List<Review> reviews = reviewRepository.findByListing_ListingId(listingId);
        return ResponseEntity.ok(reviews);
    }

    //current user can add comment
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest request) {

        User user = userRepository.findById(request.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Listing listing = listingRepository.findById(request.listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        Review review = new Review();
        review.setUser(user);
        review.setListing(listing);
        review.setRating(request.rating);
        review.setComment(request.comment);

        reviewRepository.save(review);

        return ResponseEntity.ok("Review added successfully");
    }

    // current user can delete comment
    // App : /api/reviews/delete/5?currentUserId=1
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @RequestParam Long currentUserId) {

        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Review not found");
        }

        Review review = reviewOpt.get();


        if (!review.getUser().getUserId().equals(currentUserId)) {
            return ResponseEntity.status(403).body("Permission denied: You can only delete your own review.");
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok("Review deleted successfully");
    }
}