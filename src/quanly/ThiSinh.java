package quanly;

public class ThiSinh extends NguoiDung {
    private String lop;

    public ThiSinh() {
        super();
    }

    public ThiSinh(String maNguoiDung, String hoTen, String email, String soDienThoai, String lop) {
        super(maNguoiDung, hoTen, email, soDienThoai);
        this.lop = lop;
    }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    @Override
    public void hienThiThongTin() {
        System.out.println("[Thi Sinh] Ma: " + maNguoiDung + " | Ho ten: " + hoTen + 
                           " | Email: " + email + " | SĐT: " + soDienThoai + " | Lop: " + lop);
    }

    @Override
    public String toString() {
        return "TS," + maNguoiDung + "," + hoTen + "," + email + "," + soDienThoai + "," + lop;
    }
}