package laporan;

public class ItemRetur {

    private String idProduk;
    private String namaProduk;
    private int jumlahRetur;

    public ItemRetur(String idProduk, String namaProduk, int jumlahRetur) {
        this.idProduk    = idProduk;
        this.namaProduk  = namaProduk;
        this.jumlahRetur = jumlahRetur;
    }

    public String getIdProduk()   { return idProduk; }
    public String getNamaProduk() { return namaProduk; }
    public int getJumlahRetur()   { return jumlahRetur; }

    public boolean validate() {
        return idProduk   != null && !idProduk.isEmpty()
            && namaProduk != null && !namaProduk.isEmpty()
            && jumlahRetur > 0;
    }

    @Override
    public String toString() {
        return "ItemRetur{" +
               "idProduk=" + idProduk +
               ", namaProduk=" + namaProduk +
               ", jumlahRetur=" + jumlahRetur +
               '}';
    }
}
