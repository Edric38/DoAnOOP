package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * KẾ THỪA: lớp danh sách câu hỏi, extends DanhSach<CauHoi>.
 * ĐA HÌNH: một ArrayList<CauHoi> chứa lẫn CauHoiMotDapAn, CauHoiNhieuDapAn, CauHoiDungSai.
 * Tìm kiếm: theo mã, theo từ khóa nội dung, theo môn học, theo loại, theo độ khó.
 */
public class DanhSachCauHoi extends DanhSach<CauHoi> {

    public static final String TEN_FILE = "cauhoi.txt";

    public DanhSachCauHoi() {
        super(THU_MUC_MAC_DINH + "/" + TEN_FILE);
    }

    public DanhSachCauHoi(String duongDanFile) {
        super(duongDanFile);
    }

    @Override
    protected String layMa(CauHoi doiTuong) {
        return doiTuong.getMa();
    }

    /**
     * Ký hiệu đầu dòng quyết định tạo lớp con nào:
     * MOT|...  -> CauHoiMotDapAn, NHIEU|... -> CauHoiNhieuDapAn, DUNGSAI|... -> CauHoiDungSai.
     */
    @Override
    protected CauHoi phanTichDong(String dong) {
        String[] t = tachTruong(dong, 6);
        String loai = t[0].toUpperCase();
        String ma = t[1].toUpperCase();
        String noiDung = t[2];
        String maMon = t[3].toUpperCase();
        int doKho = Integer.parseInt(t[4]);
        if (doKho < 1 || doKho > 3) {
            throw new IllegalArgumentException("độ khó phải từ 1 đến 3");
        }
        String dapAn = t[5].toUpperCase();

        if (loai.equals(CauHoiMotDapAn.KY_HIEU_LOAI)) {
            List<LuaChon> ds = docCacLuaChon(t, 6);
            if (dapAn.length() != 1 || !LuaChon.coKyHieu(ds, dapAn.charAt(0))) {
                throw new IllegalArgumentException("đáp án đúng không nằm trong các lựa chọn");
            }
            return new CauHoiMotDapAn(ma, noiDung, maMon, doKho, ds, dapAn.charAt(0));
        }
        if (loai.equals(CauHoiNhieuDapAn.KY_HIEU_LOAI)) {
            List<LuaChon> ds = docCacLuaChon(t, 6);
            if (!CauHoiNhieuDapAn.laChuoiDapAnHopLe(dapAn, ds)
                    || CauHoiNhieuDapAn.chuanHoaDapAn(dapAn).length() < CauHoiNhieuDapAn.SO_DAP_AN_TOI_THIEU) {
                throw new IllegalArgumentException("câu nhiều đáp án cần ít nhất 2 đáp án đúng nằm trong các lựa chọn");
            }
            return new CauHoiNhieuDapAn(ma, noiDung, maMon, doKho, ds, dapAn);
        }
        if (loai.equals(CauHoiDungSai.KY_HIEU_LOAI)) {
            Boolean giaTri = CauHoiDungSai.docTraLoi(dapAn);
            if (giaTri == null) {
                throw new IllegalArgumentException("đáp án Đúng/Sai phải là D hoặc S");
            }
            return new CauHoiDungSai(ma, noiDung, maMon, doKho, giaTri);
        }
        throw new IllegalArgumentException("loại câu hỏi không hợp lệ: " + t[0]);
    }

    /** STATIC: đọc các lựa chọn từ vị trí batDau, gán ký hiệu A, B, C... */
    private static List<LuaChon> docCacLuaChon(String[] t, int batDau) {
        int soLuong = t.length - batDau;
        if (soLuong < CauHoiCoLuaChon.SO_LUA_CHON_TOI_THIEU || soLuong > CauHoiCoLuaChon.SO_LUA_CHON_TOI_DA) {
            throw new IllegalArgumentException("số lựa chọn phải từ 2 đến 6");
        }
        List<LuaChon> ds = new ArrayList<>();
        for (int i = 0; i < soLuong; i++) {
            if (t[batDau + i].isEmpty()) {
                throw new IllegalArgumentException("lựa chọn " + (char) ('A' + i) + " bị trống");
            }
            ds.add(new LuaChon((char) ('A' + i), t[batDau + i]));
        }
        return ds;
    }

    @Override
    protected String chuyenThanhDong(CauHoi doiTuong) {
        return doiTuong.toFileString(); // ĐA HÌNH
    }

    @Override
    protected void hienThiPhanTu(CauHoi doiTuong) {
        doiTuong.hienThi(); // ĐA HÌNH: mỗi loại câu hỏi tự in theo cách riêng
        System.out.println("-".repeat(80));
    }

    @Override
    protected void inTieuDe() {
        System.out.println("=".repeat(80));
    }

    public List<CauHoi> timTheoTuKhoa(String tuKhoa) {
        List<CauHoi> ketQua = new ArrayList<>();
        for (CauHoi ch : danhSach) {
            if (NhapLieu.chuaTuKhoa(ch.getNoiDung(), tuKhoa)) {
                ketQua.add(ch);
            }
        }
        return ketQua;
    }

    public List<CauHoi> timTheoMonHoc(String maMonHoc) {
        List<CauHoi> ketQua = new ArrayList<>();
        for (CauHoi ch : danhSach) {
            if (ch.getMaMonHoc().equalsIgnoreCase(maMonHoc.trim())) {
                ketQua.add(ch);
            }
        }
        return ketQua;
    }

    /** Tìm theo loại: gọi getKyHieuLoai() ĐA HÌNH. */
    public List<CauHoi> timTheoLoai(String kyHieuLoai) {
        List<CauHoi> ketQua = new ArrayList<>();
        for (CauHoi ch : danhSach) {
            if (ch.getKyHieuLoai().equalsIgnoreCase(kyHieuLoai)) {
                ketQua.add(ch);
            }
        }
        return ketQua;
    }

    public List<CauHoi> timTheoDoKho(int doKho) {
        List<CauHoi> ketQua = new ArrayList<>();
        for (CauHoi ch : danhSach) {
            if (ch.getDoKho() == doKho) {
                ketQua.add(ch);
            }
        }
        return ketQua;
    }
}
