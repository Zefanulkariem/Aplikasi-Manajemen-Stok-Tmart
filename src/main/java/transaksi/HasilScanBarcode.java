package transaksi;

import java.time.LocalDate;

public class HasilScanBarcode {

    private String idProduk;
    private String namaProduk;
    private int jumlahDiterima;
    private LocalDate tanggalExpiredInput;
    private boolean sesuaiPesanan;

    public HasilScanBarcode(String idProduk, String namaProduk, int jumlahDiterima,
                            LocalDate tanggalExpiredInput, boolean sesuaiPesanan) {
        this.idProduk           = idProduk;
        this.namaProduk         = namaProduk;
        this.jumlahDiterima     = jumlahDiterima;
        this.tanggalExpiredInput = tanggalExpiredInput;
        this.sesuaiPesanan      = sesuaiPesanan;
    }

    public String getIdProduk()              { return idProduk; }
    public String getNamaProduk()            { return namaProduk; }
    public int getJumlahDiterima()           { return jumlahDiterima; }
    public LocalDate getTanggalExpiredInput(){ return tanggalExpiredInput; }
    public boolean isSesuaiPesanan()         { return sesuaiPesanan; }
}
