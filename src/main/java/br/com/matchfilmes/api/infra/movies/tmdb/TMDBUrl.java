package br.com.matchfilmes.api.infra.movies.tmdb;

public class TMDBUrl {
  private final static String BASE_URL = "https://api.themoviedb.org/3";
  private final static String QUERY_PARAMS = "language=pt-BR";

  public static String url(String path) {
    return BASE_URL + path + "?" + QUERY_PARAMS;
  }

  public static String url(String path, String[] params) {
    StringBuilder query = new StringBuilder("?" + QUERY_PARAMS);
    for (String param : params) {
      query.append("&").append(param);
    }
    return BASE_URL + path + query;
  }
}
