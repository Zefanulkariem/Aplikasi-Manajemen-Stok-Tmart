package laporan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import base.BaseEntity;
import enums.KategoriRetur;
import model.StokToko;

public class LaporanRetur extends BaseEntity {

    private LocalDate tanggal;
    private String idKaryawan;
    private List<ItemRetur> items;
    private KategoriRetur kategoriRetur;
    private String alasanDetail;

    public LaporanRetur(String id, LocalDate tanggal, String idKaryawan,
                        KategoriRetur kategoriRetur, String alasanDetail) {
        super(id);
        this.tanggal       = tanggal;
        this.idKaryawan    = idKaryawan;
        this.kategoriRetur = kategoriRetur;
        this.alasanDetail  = alasanDetail;
        this.items         = new ArrayList<>();
    }

    public LocalDate getTanggal()          {
        return tanggal;
    }
    
    public String getIdKaryawan()          { return idKaryawan; }
    public KategoriRetur getKategoriRetur(){ return kategoriRetur; }
    public String getAlasanDetail()        { return alasanDetail; }
    public List<ItemRetur> getItems()      { return items; }

    public void tambahkanItem(ItemRetur item) {
        items.add(item);
    }

    /**
     * Mengirim laporan retur: mengurangi stok toko sesuai item yang diretur.
     */
    public void kirimLaporan(StokToko stokToko) {
        for (ItemRetur item : items) {
            stokToko.updateStokProduk(
                item.getIdProduk(),
                -item.getJumlahRetur()   // negatif = mengurangi stok
            );
        }
        System.out.println("[NOTIFIKASI] Laporan retur dikirim ke supplier: " + generateReport());
    }

    public String generateReport() {
        return "Retur | " + tanggal +
               " | Kategori: " + kategoriRetur +
               " | Total Item: " + items.size() +
               " | Alasan: " + alasanDetail;
    }

    @Override
    public boolean validate() {
        return tanggal      != null
            && idKaryawan   != null && !idKaryawan.isEmpty()
            && kategoriRetur != null
            && alasanDetail != null && !alasanDetail.isEmpty()
            && items        != null;
    }

    @Override
    public String toString() {
        return "LaporanRetur{" +
               "id='" + getId() + '\'' +
               ", tanggal='" + tanggal + '\'' +
               ", idKaryawan='" + idKaryawan + '\'' +
               ", kategori='" + kategoriRetur + '\'' +
               ", alasan='" + alasanDetail + '\'' +
               ", jumlahItem=" + items.size() +
               '}';
    }
}
