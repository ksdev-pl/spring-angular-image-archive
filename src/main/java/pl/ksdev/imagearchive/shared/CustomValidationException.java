package pl.ksdev.imagearchive.shared;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomValidationException extends Exception {

    private String field = "";

    private String code = "";

    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(String message, String field, String code) {
        super(message);
        this.field = field;
        this.code = code;
    }
}
