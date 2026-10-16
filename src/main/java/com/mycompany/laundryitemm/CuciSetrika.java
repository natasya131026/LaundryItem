package com.mycompany.laundryitemm;

/**
 * SUBCLASS 2: CuciSetrika (cuci + setrika + pewangi).
 * MULTIPLE IMPLEMENTS: DapatDiantar dan DapatDinilai sekaligus.
 */
public class CuciSetrika extends Pesanan implements DapatDiantar, DapatDinilai {
    private static final double TAMBAHAN_PER_KG = 4000;

    private String jenisPewangi;
    private boolean lipatRapi;
    private int rating = 0;

    public CuciSetrika(String namaPelanggan, String jenisPakaian, double beratKg,
                       String jenisPewangi, boolean lipatRapi) {
        super(namaPelanggan, jenisPakaian, beratKg);
        setJenisPewangi(jenisPewangi);
        this.lipatRapi = lipatRapi;
    }

    public String getJenisPewangi() { return jenisPewangi; }
    public boolean isLipatRapi() { return lipatRapi; }
    public int getRating() { return rating; }
    public void setLipatRapi(boolean lipatRapi) { this.lipatRapi = lipatRapi; }

    public void setJenisPewangi(String jenisPewangi) {
        if (jenisPewangi == null || jenisPewangi.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis pewangi tidak boleh kosong!");
        }
        this.jenisPewangi = jenisPewangi.trim();
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Setrika";
    }

    @Override
    public String getDetail() {
        return "Pewangi " + jenisPewangi + (lipatRapi ? ", dilipat" : ", digantung")
                + (rating > 0 ? ", rating " + rating : "");
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Setrika] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci, disetrika, diberi pewangi " + jenisPewangi
                + (lipatRapi ? " dan dilipat rapi." : " dan digantung."));
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Setrika: pengerjaan sekitar 36 jam.");
        return 36;
    }

    // ----- kontrak interface DapatDiantar -----
    @Override
    public void antarKeRumah() {
        System.out.println("[Antar] Kurir mengantar pakaian " + getJenisPakaian()
                + " (Cuci Setrika) ke rumah " + getNamaPelanggan() + ".");
    }

    // ----- kontrak interface DapatDinilai -----
    @Override
    public void beriRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus 1 - 5!");
        }
        this.rating = rating;
        System.out.println("[Rating] Order #" + getIdOrder() + " (Cuci Setrika) diberi rating "
                + rating + "/5 oleh " + getNamaPelanggan() + ".");
    }
}