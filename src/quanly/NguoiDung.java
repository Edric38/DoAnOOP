package quanly;

public abstract class NguoiDung {
    protected String maNguoiDung;
    protected String hoTen;
    protected String email;
    protected String soDienThoai;

    public NguoiDung() {}

    public NguoiDung(String maNguoiDung, String hoTen, String email, String soDienThoai) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    // Phương thức abstract để các lớp con ghi đè (tính đa hình)
    public abstract void hienThiThongTin();
}