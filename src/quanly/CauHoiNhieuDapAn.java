package quanly;

import java.util.List;
import java.util.TreeSet;

/**
 * KẾ THỪA: câu hỏi có NHIỀU đáp án đúng, extends CauHoiCoLuaChon.
 * ĐA HÌNH: override kiemTraDapAn() so sánh tập đáp án, và override chamDiem() để chấm điểm từng phần.
 * Dòng file: NHIEU|ma|noiDung|maMonHoc|doKho|AC|luaChonA|luaChonB|...
 */
public class CauHoiNhieuDapAn extends CauHoiCoLuaChon {

    public static final String KY_HIEU_LOAI = "NHIEU";
    public static final int SO_DAP_AN_TOI_THIEU = 2;

    private String dapAnDung; // đã chuẩn hóa, ví dụ "AC"

    public CauHoiNhieuDapAn() {
        super();
        this.dapAnDung = "AB";
    }

    public CauHoiNhieuDapAn(String ma, String noiDung, String maMonHoc, int doKho,
                            List<LuaChon> danhSachLuaChon, String dapAnDung) {
        super(ma, noiDung, maMonHoc, doKho, danhSachLuaChon); // gọi constructor lớp cha
        this.dapAnDung = chuanHoaDapAn(dapAnDung);
    }

    /** STATIC: chuẩn hóa "c, a" -> "AC" (in hoa, bỏ trùng, sắp xếp). */
    public static String chuanHoaDapAn(String s) {
        TreeSet<Character> tap = new TreeSet<>();
        for (char c : s.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                tap.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : tap) {
            sb.append(c);
        }
        return sb.toString();
    }

    /** STATIC: chuỗi chỉ gồm ký hiệu có trong danh sách lựa chọn, dấu phẩy và khoảng trắng. */
    public static boolean laChuoiDapAnHopLe(String s, List<LuaChon> danhSachLuaChon) {
        if (s == null || s.isBlank()) {
            return false;
        }
        for (char c : s.toUpperCase().toCharArray()) {
            if (c == ',' || c == ' ') {
                continue;
            }
            if (!LuaChon.coKyHieu(danhSachLuaChon, c)) {
                return false;
            }
        }
        return !chuanHoaDapAn(s).isEmpty();
    }

    public String getDapAnDung() {
        return dapAnDung;
    }

    public void setDapAnDung(String dapAnDung) {
        this.dapAnDung = chuanHoaDapAn(dapAnDung);
    }

    @Override
    public String getLoai() {
        return "Nhiều đáp án";
    }

    @Override
    public String getKyHieuLoai() {
        return KY_HIEU_LOAI;
    }

    @Override
    public boolean laTraLoiHopLe(String traLoi) {
        return laChuoiDapAnHopLe(traLoi, danhSachLuaChon);
    }

    @Override // ĐA HÌNH: phải chọn ĐÚNG và ĐỦ tập đáp án
    public boolean kiemTraDapAn(String traLoi) {
        return laTraLoiHopLe(traLoi) && chuanHoaDapAn(traLoi).equals(dapAnDung);
    }

    /**
     * OVERRIDE phương thức chamDiem() của lớp cha CauHoi (interface IChamDiem):
     * chọn sai bất kỳ phương án nào -> 0 điểm; chỉ chọn đúng nhưng chưa đủ -> điểm theo tỉ lệ.
     */
    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        if (!laTraLoiHopLe(traLoi)) {
            return 0.0;
        }
        String daChon = chuanHoaDapAn(traLoi);
        for (char c : daChon.toCharArray()) {
            if (dapAnDung.indexOf(c) < 0) {
                return 0.0;
            }
        }
        return diemToiDa * daChon.length() / dapAnDung.length();
    }

    @Override
    public String getHuongDanTraLoi() {
        return "Nhập các ký hiệu đúng, cách nhau dấu phẩy, ví dụ: A,C";
    }

    @Override
    public String getDapAnDungHienThi() {
        return String.join(",", dapAnDung.split(""));
    }

    @Override
    public void hienThi() {
        inPhanChung();
        hienThiCacLuaChon();
        System.out.println("    => Đáp án đúng: " + getDapAnDungHienThi());
    }

    @Override
    public String toFileString() {
        return thongTinChungFile() + "|" + dapAnDung + "|" + cacLuaChonFile();
    }
}
