package com.mycompany.laundryitemm;

/**
 * SUBCLASS 0: CuciReguler (layanan dasar).
 * Dulu Pesanan sendiri; kini jadi subclass karena superclass abstract.
 */
public class CuciReguler extends Pesanan {

    public CuciReguler(String namaPelanggan, String jenisPakaian, double beratKg) {
        super(namaPelanggan, jenisPakaian, beratKg);
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Reguler";
    }

    @Override
    public void prosesLayanan() {
        System.out.println("[Reguler] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci biasa lalu dijemur di bawah sinar matahari.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Reguler: pengerjaan sekitar 48 jam.");
        return 48;
    }
}