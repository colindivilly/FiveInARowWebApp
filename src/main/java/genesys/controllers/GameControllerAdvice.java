package genesys.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GameControllerAdvice extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GameControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({GameRuleException.class})
    public ResponseEntity<String> handle(GameRuleException e) {
        logger.error("Exception : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


    // maybe a new error for creating a plaeyer

}
