package com.srv.sumit.controller;

import com.srv.sumit.entity.MovieDetails;
import com.srv.sumit.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/movies")
public class MovieController {
    private final MovieService movieService;


    @PostMapping
    public MovieDetails addMovie(@RequestBody MovieDetails movieDetails) {
        return movieService.addNewMovie(movieDetails);
    }

    @GetMapping
    public List<MovieDetails> getAllMovie(@RequestParam("genre") String genre) {
        return movieService.scanDataByGenre(genre);
    }

    @GetMapping("/{movieId}")
    public MovieDetails getMovieById(@PathVariable("movieId") String movieId) {
        return movieService.searchMovieById(movieId);
    }

    @PatchMapping
    public MovieDetails updateMovieDetails(@RequestBody MovieDetails movieDetailsDTO) {
        return movieService.updateMovie(movieDetailsDTO);
    }

    @DeleteMapping("/{movieId}")
    public MovieDetails deleteMovieDetails(@PathVariable("movieId") String movieId) {
        return movieService.deleteByMovieId(movieId);
    }
}
