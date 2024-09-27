package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.models.FavoriteMovie;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.models.UserAlgorithm;
import br.com.matchfilmes.api.repositories.FavoriteMovieRepository;
import br.com.matchfilmes.api.repositories.UserAlgorithmRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FavoriteService {
  private final MoviesAPI moviesAPI;
  private final UserAlgorithmRepository userAlgorithmRepository;
  private final FavoriteMovieRepository favoriteMovieRepository;
  private final UserAlgorithmService userAlgorithmService;
  private final Logger logger;
  private final MovieService movieService;

  public MovieDTO favoriteMovie(Long movieId, User user) throws MovieNotFoundException, ResponseStatusException {
    Optional<UserAlgorithm> userAlgorithmOptional = userAlgorithmRepository.findByUser(user);
    UserAlgorithm userAlgorithm;

    if (userAlgorithmOptional.isPresent()) {
      userAlgorithm = userAlgorithmOptional.get();
    } else {
      userAlgorithm = UserAlgorithm.builder()
          .user(user)
          .build();
      userAlgorithm = userAlgorithmRepository.save(userAlgorithm);
    }

    Optional<FavoriteMovie> favoriteMovieOptional = favoriteMovieRepository.findByMovieIdAndUserAlgorithm(movieId, userAlgorithm);

    if (favoriteMovieOptional.isPresent()) {
      logger.info(String.format("Movie with Id '%s' is already in the favorites list.", favoriteMovieOptional.get().getMovieId()));
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    Set<FavoriteMovie> favoriteMovies = userAlgorithm.getFavoriteMovies();

    if (favoriteMovies == null) {
      logger.info("Generating new Set for Favorite Movies");
      favoriteMovies = new HashSet<>();
    }

    FavoriteMovie favoriteMovie = FavoriteMovie.builder().movieId(movieId).userAlgorithm(userAlgorithm).build();
    MovieDTO movie = moviesAPI.getMovie(movieId);

    favoriteMovies.add(favoriteMovie);
    userAlgorithm.setFavoriteMovies(favoriteMovies);

    Set<Long> favoriteMoviesId = favoriteMovies.stream().map(FavoriteMovie::getMovieId).collect(Collectors.toSet());

    logger.info(String.format("Set of Favorite Movies after insertion: %s", favoriteMoviesId));

    userAlgorithmRepository.save(userAlgorithm);

    movie.genres().forEach(
        genreDTO -> userAlgorithmService.improveGenreWeight(genreDTO, user, 5.0)
    );
    userAlgorithmService.decreaseOthersGenresWeights(movie.genres(), user, null);

    return movie;
  }

  public PagedModel<MovieDTO> getFavoritesMovies(Pageable pageable, User user) {
    Optional<UserAlgorithm> userAlgorithmOptional = userAlgorithmRepository.findByUser(user);
    UserAlgorithm userAlgorithm;

    if (userAlgorithmOptional.isPresent()) {
      userAlgorithm = userAlgorithmOptional.get();
    } else {
      userAlgorithm = UserAlgorithm.builder()
          .user(user)
          .build();
      userAlgorithm = userAlgorithmRepository.save(userAlgorithm);
    }

    Page<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findAllByUserAlgorithm(userAlgorithm, pageable);
    Long totalElements = favoriteMovieRepository.countByUserAlgorithm(userAlgorithm);

    Set<Long> favoriteMoviesId = favoriteMovies.stream().map(FavoriteMovie::getMovieId).collect(Collectors.toSet());
    Set<MovieDTO> movieDTOS = favoriteMoviesId.stream().map(id -> {
      try {
        return movieService.findById(id);
      } catch (MovieNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    }).collect(Collectors.toSet());

    return new PagedModel<>(new PageImpl<>(movieDTOS.stream().toList(), pageable, totalElements));
  }

  public boolean unfavoriteMovie(Long movieId, User user) throws MovieNotFoundException {
    Optional<UserAlgorithm> userAlgorithmOptional = userAlgorithmRepository.findByUser(user);
    UserAlgorithm userAlgorithm;

    if (userAlgorithmOptional.isPresent()) {
      userAlgorithm = userAlgorithmOptional.get();
    } else {
      userAlgorithm = UserAlgorithm.builder()
          .user(user)
          .build();
      userAlgorithm = userAlgorithmRepository.save(userAlgorithm);
    }

    Optional<FavoriteMovie> favoriteMovieOptional = favoriteMovieRepository.findByMovieIdAndUserAlgorithm(movieId, userAlgorithm);

    if (favoriteMovieOptional.isEmpty()) {
      logger.info(String.format("Movie with Id '%s' is not in the favorites list.", movieId));
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    logger.info(String.format("Movie with Id '%s', marked for removal, is in the favorites list.", favoriteMovieOptional.get().getMovieId()));

    Set<FavoriteMovie> favoriteMovies = userAlgorithm.getFavoriteMovies();

    FavoriteMovie favoriteMovie = favoriteMovieRepository.findByMovieIdAndUserAlgorithm(movieId, userAlgorithm).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    MovieDTO movie = moviesAPI.getMovie(movieId);

    favoriteMovies.remove(favoriteMovie);
    userAlgorithm.setFavoriteMovies(favoriteMovies);

    Set<Long> favoriteMoviesId = favoriteMovies.stream().map(FavoriteMovie::getMovieId).collect(Collectors.toSet());

    logger.info(String.format("Set of Favorite Movies after removal: %s", favoriteMoviesId));

    favoriteMovieRepository.delete(favoriteMovie);

    movie.genres().forEach(
        genreDTO -> userAlgorithmService.improveGenreWeight(genreDTO, user, 0.5)
    );

    return true;
  }
}
