package br.com.matchfilmes.api.dtos;

import java.util.List;

public record ImagesDTO(
    List<String> backdrops,
    List<String> logos,
    List<String> posters
) {
}
