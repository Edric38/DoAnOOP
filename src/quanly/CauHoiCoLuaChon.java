package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * LỚP TRỪU TƯỢNG trung gian: câu hỏi có danh sách lựa chọn A, B, C...
 * KẾ THỪA: extends CauHoi; là lớp cha của CauHoiMotDapAn và CauHoiNhieuDapAn.
 * Gom phần dùng chung (danh sách lựa chọn, cách hiển thị) để hai lớp con không lặp code.
 */
public abstract class CauHoiCoLuaChon extends CauHoi {

    public static final int SO_LUA_CHON_TOI_THIEU = 2;
    public static final int SO_LUA_CHON_TOI_DA = 6;

    protected ArrayList<LuaChon> danhSachLuaChon;

    public CauHoiCoLuaChon() {
        super();
        this.danhSachLuaChon = new ArrayList<>();
    }

    public CauHoiCoLuaChon(String ma, String noiDung, String maMonHoc, int doKho, List<LuaChon> danhSachLuaChon) {
        super(ma, noiDung, maMonHoc, doKho); // gọi constructor lớp cha CauHoi
        this.danhSachLuaChon = new ArrayList<>(danhSachLuaChon);
    }

    public ArrayList<LuaChon> getDanhSachLuaChon() {
        return new ArrayList<>(danhSachLuaChon);
    }

    public void setDanhSachLuaChon(List<LuaChon> danhSachLuaChon) {
        this.danhSachLuaChon = new ArrayList<>(danhSachLuaChon);
    }

    public boolean coKyHieu(char kyHieu) {
        return LuaChon.coKyHieu(danhSachLuaChon, kyHieu);
    }

    protected void hienThiCacLuaChon() {
        for (LuaChon lc : danhSachLuaChon) {
            lc.hienThi();
        }
    }

    /** Nội dung các lựa chọn nối bằng '|' để ghi file. */
    protected String cacLuaChonFile() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < danhSachLuaChon.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(danhSachLuaChon.get(i).getNoiDung());
        }
        return sb.toString();
    }

    @Override // Override phương thức trừu tượng của CauHoi, dùng chung cho 2 lớp con
    public void hienThiKhiThi(int soThuTu) {
        System.out.println("Câu " + soThuTu + " (" + getLoai() + "): " + noiDung);
        hienThiCacLuaChon();
    }
}
