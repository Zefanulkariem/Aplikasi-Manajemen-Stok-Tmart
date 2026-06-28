package exception;

public class InputTidakValidException extends RuntimeException {
    public InputTidakValidException(String pesan) {
        super(pesan);
    }
}
