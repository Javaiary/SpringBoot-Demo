package com.example.demo.repository;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
        @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
        List<Review> findByMovie(Movie movie);
}
