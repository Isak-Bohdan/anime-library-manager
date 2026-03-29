package com.bohdan.anime_library_manager.controller.web;

import com.bohdan.anime_library_manager.service.JikanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final JikanService jikanService;

    public SearchController(JikanService jikanService) {
        this.jikanService = jikanService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query,
                         @RequestParam(defaultValue = "anime") String type,
                         Model model) {

        if (query != null && !query.isEmpty()) {

            if (type.equals("manga")) {
                model.addAttribute("results", jikanService.searchManga(query));
            } else {
                model.addAttribute("results", jikanService.searchAnime(query));
            }
        }

        return "search";
    }
}