package com.bohdan.anime_library_manager.service;

import com.bohdan.anime_library_manager.dto.JikanAnimeDto;
import com.bohdan.anime_library_manager.dto.JikanSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class JikanService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<JikanAnimeDto> searchAnime(String query) {
        String url = "https://api.jikan.moe/v4/anime?q=" + query + "&limit=10";

        JikanSearchResponse response =
                restTemplate.getForObject(url, JikanSearchResponse.class);

        return response != null ? response.getData() : List.of();
    }

    public JikanAnimeDto getAnimeById(Long malId) {
        String url = "https://api.jikan.moe/v4/anime/" + malId;

        JikanAnimeDtoWrapper response =
                restTemplate.getForObject(url, JikanAnimeDtoWrapper.class);

        return response != null ? response.getData() : null;
    }

    public static class JikanAnimeDtoWrapper {
        private JikanAnimeDto data;

        public JikanAnimeDto getData() {
            return data;
        }

        public void setData(JikanAnimeDto data) {
            this.data = data;
        }
    }
}