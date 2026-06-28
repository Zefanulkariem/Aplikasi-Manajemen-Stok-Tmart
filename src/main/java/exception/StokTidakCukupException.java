package exception;

public class StokTidakCukupException extends RuntimeException {
    public StokTidakCukupException(String pesan) {
        super(pesan);
    }
}
