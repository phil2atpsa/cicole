package pro.novatech.solutions.kelasiapi.v1;

/**
 * Created by p.lukengu on 4/1/2017.
 */

public class KelasiApiException extends Exception {
    private static final long serialVersionUID = 1L;

    public KelasiApiException() {
        super();
    }

    public KelasiApiException(String message) {
        super(message);
    }

    public KelasiApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public KelasiApiException(Throwable cause) {
        super(cause);
    }
}
