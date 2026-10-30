package quanly;

public class GiaoVien extends NguoiDung {
    private String boMon;

    public GiaoVien() {
        super();
    }

    public GiaoVien(String maNguoiDung, String hoTen, String email, String soDienThoai, String boMon) {
        super(maNguoiDung, hoTen, email, soDienThoai);
        this.boMon = boMon;
    }

    public String getBoMon() { return boMon; }
    public void setBoMon(String boMon) { this.boMon = boMon; }

    @Override
    public void hienThiThongTin() {
        System.out.println("[Giao Vien] Ma: " + maNguoiDung + " | Ho ten: " + hoTen + 
                           " | Email: " + email + " | SĐT: " + soDienThoai + " | Bo mon: " + boMon);
    }

    @Override
    public String toString() {
        return "GV," + maNguoiDung + "," + hoTen + "," + email + "," + soDienThoai + "," + boMon;
    }
}