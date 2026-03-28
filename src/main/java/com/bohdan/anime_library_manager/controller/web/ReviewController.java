package com.bohdan.anime_library_manager.controller.web;

import com.bohdan.anime_library_manager.entity.Review;
import com.bohdan.anime_library_manager.entity.Title;
import com.bohdan.anime_library_manager.service.ReviewService;
import com.bohdan.anime_library_manager.service.TitleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final TitleService titleService;

    public ReviewController(ReviewService reviewService, TitleService titleService) {
        this.reviewService = reviewService;
        this.titleService = titleService;
    }

    @GetMapping("/titles/{titleId}/reviews")
    public String getReviewsByTitle(@PathVariable Long titleId, Model model) {
        Title title = titleService.getTitleById(titleId)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + titleId));

        model.addAttribute("title", title);
        model.addAttribute("reviews", reviewService.getReviewsByTitleId(titleId));
        return "reviews";
    }

    @GetMapping("/titles/{titleId}/reviews/new")
    public String showCreateReviewForm(@PathVariable Long titleId, Model model) {
        Title title = titleService.getTitleById(titleId)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + titleId));

        Review review = new Review();
        review.setTitle(title);

        model.addAttribute("title", title);
        model.addAttribute("reviewForm", review);
        return "review-form";
    }

    @PostMapping("/titles/{titleId}/reviews")
    public String createReview(@PathVariable Long titleId,
                               @ModelAttribute("reviewForm") Review review) {
        Title title = titleService.getTitleById(titleId)
                .orElseThrow(() -> new RuntimeException("Title not found with id: " + titleId));

        review.setTitle(title);
        reviewService.saveReview(review);

        return "redirect:/titles/" + titleId + "/reviews";
    }

    @PostMapping("/titles/{titleId}/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long titleId,
                               @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/titles/" + titleId + "/reviews";
    }
}