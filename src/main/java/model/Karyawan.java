package model;

import java.util.ArrayList;

import laporan.LaporanRetur;
import transaksi.Pemesanan;

public class Karyawan extends base.BaseEntity {

    private String name;
    private String email;
    private String username;
    private String password;
    private String noTelp;
    private String alamat;
    private String namaToko;
    private ArrayList<Pemesanan> daftarPemesanan;
    private ArrayList<LaporanRetur> daftarLaporanRetur;

    public Karyawan(String id, String name, String email, String username,
                    String password, String noTelp, String alamat, String namaToko) {
        super(id);
        this.name     = name;
        this.email    = email;
        this.username = username;
        this.password = password;
        this.noTelp   = noTelp;
        this.alamat   = alamat;
        this.namaToko = namaToko;
        this.daftarPemesanan    = new ArrayList<>();
        this.daftarLaporanRetur = new ArrayList<>();
    }

    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getUsername() { return username; }
    public String getNoTelp()   { return noTelp; }
    public String getAlamat()   { return alamat; }
    public String getNamaToko() { return namaToko; }

    public ArrayList<Pemesanan> getDaftarPemesanan() {
        return daftarPemesanan;
    }

    public void tambahPemesanan(Pemesanan pemesanan) {
        daftarPemesanan.add(pemesanan);
    }

    public ArrayList<LaporanRetur> getDaftarLaporanRetur() {
        return daftarLaporanRetur;
    }

    public void tambahLaporan(LaporanRetur laporan) {
        daftarLaporanRetur.add(laporan);
    }

    public boolean login(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            System.out.println("\n[Login Success] Selamat Datang " + name + "!");
            return true;
        }
        System.out.println("\nUsername atau password salah!");
        return false;
    }

    public void logout() {
        System.out.println("Sampai jumpa, " + name);
    }

    @Override
    public boolean validate() {
        return name     != null && !name.isEmpty()
            && username != null && !username.isEmpty()
            && password != null && !password.isEmpty();
    }

    @Override
    public String toString() {
        return "\nID Karyawan : " + getId()   +
               "\nNama        : " + name      +
               "\nEmail       : " + email     +
               "\nNo. Telp    : " + noTelp    +
               "\nAlamat      : " + alamat    +
               "\nToko        : " + namaToko;
    }
}
