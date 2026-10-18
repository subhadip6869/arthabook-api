package app.netlify.subhacodes.arthabookapi.common.exceptions;

public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(String message) {
        super(message);
    }
}
