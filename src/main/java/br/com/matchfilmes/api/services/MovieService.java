package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.models.GenreWeight;
import br.com.matchfilmes.api.models.User;
import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MovieService {
  private final MoviesAPI moviesAPI;
  private final UserAlgorithmService userAlgorithmService;
  private final Logger logger;

  public MovieDTO findById(Long movieId, User user) throws MovieNotFoundException {
    MovieDTO movie = moviesAPI.getMovie(movieId);

    movie.genres().forEach(
        genreDTO -> userAlgorithmService.improveGenreWeight(genreDTO, user, null)
    );
    userAlgorithmService.decreaseOthersGenresWeights(movie.genres(), user, null);

    return movie;
  }

  public PagedModel<MovieDTO> findPopularMovies(Pageable pageable) {
    return moviesAPI.getPopularMovies(pageable);
  }

  public PagedModel<MovieDTO> findRecommendedMovies(Pageable pageable, User user) {
    List<GenreWeight> userTopGenres = userAlgorithmService.getUserTopGenreWeights(user).stream().toList();

    Set<MovieDTO> recommendedMovies = getMovieDTOS(pageable, userTopGenres);
    return new PagedModel<>(new PageImpl<>(recommendedMovies.stream().toList(), pageable, pageable.getPageSize() * 4L));
  }

  private Set<MovieDTO> getMovieDTOS(Pageable pageable, List<GenreWeight> userTopGenres) {
    int maxNumberOfMovies = pageable.getPageSize();
    int actualNumberOfMovies = 0;
    Map<Long, Integer> totalMoviesPerGenre = new HashMap<>();

    for (int i = 0; actualNumberOfMovies < maxNumberOfMovies; i++) {
      if (i == userTopGenres.size()) break;
      GenreWeight genreWeight = userTopGenres.get(i);
      int totalMoviesOfGenre = (int) Math.ceil(genreWeight.getWeight() * maxNumberOfMovies);

      if (actualNumberOfMovies + totalMoviesOfGenre > maxNumberOfMovies) {
        totalMoviesOfGenre = maxNumberOfMovies - actualNumberOfMovies;
      }

      totalMoviesPerGenre.put(genreWeight.getGenreId(), totalMoviesOfGenre);
      actualNumberOfMovies += totalMoviesOfGenre;
    }

    logger.info(String.format("Map for each genre total movies: %s", totalMoviesPerGenre));

    Set<MovieDTO> recommendedMovies = new HashSet<>();

    totalMoviesPerGenre.forEach((genreId, totalMovies) -> {
      Set<MovieDTO> data = moviesAPI.getRecommendedMoviesByGenres(pageable, new Long[]{genreId});
      Iterables.limit(data, totalMovies).forEach(recommendedMovies::add);
    });
    return recommendedMovies;
  }
}