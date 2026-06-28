package exception;

public class PemesananNotFoundException extends RuntimeException {
    public PemesananNotFoundException(String pesan) {
        super(pesan);
    }
}
