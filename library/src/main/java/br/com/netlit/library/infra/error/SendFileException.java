package br.com.netlit.library.infra.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SendFileException extends RuntimeException {

    public SendFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
