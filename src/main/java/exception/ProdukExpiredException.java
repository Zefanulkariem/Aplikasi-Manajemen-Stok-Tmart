package exception;

public class ProdukExpiredException extends RuntimeException {
    public ProdukExpiredException(String pesan) {
        super(pesan);
    }
}
