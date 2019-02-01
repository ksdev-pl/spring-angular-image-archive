package pl.ksdev.imagearchive.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class Responses {

    public static <T> ResponseEntity<T> optionalResponse(Optional<T> optionalResponse) {
        return optionalResponse.isPresent()
            ? new ResponseEntity<>(optionalResponse.get(), HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
