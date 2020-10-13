package br.com.netlit.library.infra.error;

import br.com.netlit.library.infra.utils.HttpRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Log4j2
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String ERROR_MESSAGE_TEMPLATE = "%s, %s";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    final Stream<String> fieldErrors =
        ex.getBindingResult()
            .getFieldErrors().stream()
            .map(error -> format(ERROR_MESSAGE_TEMPLATE, error.getField(), error.getDefaultMessage()));
    final Stream<String> globalErrors =
        ex.getBindingResult()
            .getGlobalErrors().stream()
            .map(error -> format(ERROR_MESSAGE_TEMPLATE, error.getObjectName(), error.getDefaultMessage()));

    final List<String> errors = Stream.concat(fieldErrors, globalErrors).collect(Collectors.toList());
    log.info("Invalid arguments on " + HttpRequest.getUrl() + ": " + errors);
    final InvalidArgumentResponse errorResponseMessage = new InvalidArgumentResponse(errors);
    return new ResponseEntity<>(errorResponseMessage, headers, status);
  }
}
