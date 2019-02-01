package pl.ksdev.imagearchive.shared;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
