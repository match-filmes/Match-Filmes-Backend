package br.com.matchfilmes.api.infra.errors;


import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    throw new ResponseStatusException(response.getStatusCode());
  }
}
