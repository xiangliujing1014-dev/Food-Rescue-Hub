package com.frh.backend.repository;



import com.frh.backend.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByListing_ListingId(Long listingId);
}