package quanly;

import java.util.List;

/**
 * KẾ THỪA: câu hỏi chọn đúng MỘT đáp án, extends CauHoiCoLuaChon.
 * ĐA HÌNH: override kiemTraDapAn() so sánh 1 ký tự.
 * Dòng file: MOT|ma|noiDung|maMonHoc|doKho|dapAnDung|luaChonA|luaChonB|...
 */
public class CauHoiMotDapAn extends CauHoiCoLuaChon {

    public static final String KY_HIEU_LOAI = "MOT";

    private char dapAnDung;

    public CauHoiMotDapAn() {
        super();
        this.dapAnDung = 'A';
    }

    public CauHoiMotDapAn(String ma, String noiDung, String maMonHoc, int doKho,
                          List<LuaChon> danhSachLuaChon, char dapAnDung) {
        super(ma, noiDung, maMonHoc, doKho, danhSachLuaChon); // gọi constructor lớp cha
        this.dapAnDung = Character.toUpperCase(dapAnDung);
    }

    public char getDapAnDung() {
        return dapAnDung;
    }

    public void setDapAnDung(char dapAnDung) {
        this.dapAnDung = Character.toUpperCase(dapAnDung);
    }

    @Override
    public String getLoai() {
        return "Một đáp án";
    }

    @Override
    public String getKyHieuLoai() {
        return KY_HIEU_LOAI;
    }

    @Override
    public boolean laTraLoiHopLe(String traLoi) {
        return traLoi != null && traLoi.trim().length() == 1 && coKyHieu(traLoi.trim().charAt(0));
    }

    @Override // ĐA HÌNH: cách kiểm tra riêng của câu một đáp án
    public boolean kiemTraDapAn(String traLoi) {
        return laTraLoiHopLe(traLoi) && Character.toUpperCase(traLoi.trim().charAt(0)) == dapAnDung;
    }

    @Override
    public String getHuongDanTraLoi() {
        return "Nhập MỘT ký hiệu lựa chọn, ví dụ: B";
    }

    @Override
    public String getDapAnDungHienThi() {
        return String.valueOf(dapAnDung);
    }

    @Override
    public void hienThi() {
        inPhanChung();
        hienThiCacLuaChon();
        System.out.println("    => Đáp án đúng: " + dapAnDung);
    }

    @Override
    public String toFileString() {
        return thongTinChungFile() + "|" + dapAnDung + "|" + cacLuaChonFile();
    }
}
