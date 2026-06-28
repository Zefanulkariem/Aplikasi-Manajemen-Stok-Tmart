package transaksi;

import model.Produk;

public final class ItemPemesanan {

    private String idProduk;
    private String namaProduk;
    private String kodeBarcode;
    private int jumlahPesan;
    private double hargaSatuan;

    public ItemPemesanan(String idProduk, String namaProduk,
                         int jumlahPesan, double hargaSatuan, String kodeBarcode) {
        this.idProduk    = idProduk;
        this.namaProduk  = namaProduk;
        this.jumlahPesan = jumlahPesan;
        this.hargaSatuan = hargaSatuan;
        this.kodeBarcode = kodeBarcode;
    }

    public String getIdProduk()    { return idProduk; }
    public String getNamaProduk()  { return namaProduk; }
    public int getJumlahPesan()    { return jumlahPesan; }
    public String getKodeBarcode() { return kodeBarcode; }

    public double getSubtotal() {
        return hargaSatuan * jumlahPesan;
    }

    @Override
    public String toString() {
        return "ID Produk : " + idProduk +
               "\nNama      : " + namaProduk +
               "\nJumlah    : " + jumlahPesan +
               "\nHarga     : Rp " + hargaSatuan +
               "\nSubtotal  : Rp " + getSubtotal();
    }
    
}
