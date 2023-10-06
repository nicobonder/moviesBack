package dev.nicobonder.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository; //asociamos con la DB

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) { 
        Review review = reviewRepository.insert(new Review(reviewBody));//insertamos en la DB la review

        //asociamos la review con la movie
        mongoTemplate.update(Movie.class)
            .matching(Criteria.where("imdbId").is(imdbId))
            .apply(new Update().push("reviewIds").value(review)) //review es el value que quiero empujar en reviewIds
            .first();

            return review;
    }
}
