package com.mycompany.laundryitemm;

/**
 * SUBCLASS 1: CuciKering (cuci + pengeringan mesin).
 * Implements DapatDinilai.
 */
public class CuciKering extends Pesanan implements DapatDinilai {
    private static final double TAMBAHAN_PER_KG = 3000;

    private int suhuPengering;   // derajat Celsius
    private int durasiMenit;
    private int rating = 0;      // 0 = belum dinilai

    public CuciKering(String namaPelanggan, String jenisPakaian, double beratKg,
                      int suhuPengering, int durasiMenit) {
        super(namaPelanggan, jenisPakaian, beratKg);   // super pada constructor
        setSuhuPengering(suhuPengering);
        setDurasiMenit(durasiMenit);
    }

    public int getSuhuPengering() { return suhuPengering; }
    public int getDurasiMenit() { return durasiMenit; }
    public int getRating() { return rating; }

    public void setSuhuPengering(int suhuPengering) {
        if (suhuPengering < 40 || suhuPengering > 90) {
            throw new IllegalArgumentException("Suhu pengering harus 40 - 90 derajat Celsius!");
        }
        this.suhuPengering = suhuPengering;
    }

    public void setDurasiMenit(int durasiMenit) {
        if (durasiMenit < 10 || durasiMenit > 120) {
            throw new IllegalArgumentException("Durasi pengeringan harus 10 - 120 menit!");
        }
        this.durasiMenit = durasiMenit;
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Kering";
    }

    @Override
    public String getDetail() {
        return suhuPengering + " C, " + durasiMenit + " menit"
                + (rating > 0 ? ", rating " + rating : "");
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Kering] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci, lalu dikeringkan mesin " + suhuPengering + " C selama " + durasiMenit + " menit.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Kering: pengerjaan sekitar 24 jam.");
        return 24;
    }

    // ----- kontrak interface DapatDinilai -----
    @Override
    public void beriRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus 1 - 5!");
        }
        this.rating = rating;
        System.out.println("[Rating] Order #" + getIdOrder() + " (Cuci Kering) diberi rating "
                + rating + "/5 oleh " + getNamaPelanggan() + ".");
    }
}