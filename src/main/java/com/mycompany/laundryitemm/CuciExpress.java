package com.mycompany.laundryitemm;

/**
 * SUBCLASS 3 (BARU - Modul 6): CuciExpress (selesai cepat, biaya prioritas).
 * Implements DapatDiantar.
 */
public class CuciExpress extends Pesanan implements DapatDiantar {
    private static final double TAMBAHAN_PER_KG = 6000;

    private int targetJam;   // target selesai (jam)

    public CuciExpress(String namaPelanggan, String jenisPakaian, double beratKg, int targetJam) {
        super(namaPelanggan, jenisPakaian, beratKg);
        setTargetJam(targetJam);
    }

    public int getTargetJam() { return targetJam; }

    public void setTargetJam(int targetJam) {
        if (targetJam < 3 || targetJam > 12) {
            throw new IllegalArgumentException("Target selesai harus 3 - 12 jam!");
        }
        this.targetJam = targetJam;
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Express";
    }

    @Override
    public String getDetail() {
        return "Selesai dalam " + targetJam + " jam";
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Express] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " diproses PRIORITAS, ditargetkan selesai dalam " + targetJam + " jam.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Express: target selesai " + targetJam + " jam.");
        return targetJam;
    }

    // ----- kontrak interface DapatDiantar -----
    @Override
    public void antarKeRumah() {
        System.out.println("[Antar] Kurir motor mengantar pakaian " + getJenisPakaian()
                + " (Cuci Express) langsung ke " + getNamaPelanggan() + ".");
    }
}