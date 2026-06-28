package exception;

public class ProdukNotFoundException extends RuntimeException {
    public ProdukNotFoundException(String pesan) {
        super(pesan);
    }
}
