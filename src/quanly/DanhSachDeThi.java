package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * KẾ THỪA: lớp danh sách đề thi, extends DanhSach<DeThi>.
 * Tìm kiếm: theo mã, theo tên, theo môn học. Có thêm hàm kiểm tra câu hỏi đang được đề nào dùng.
 */
public class DanhSachDeThi extends DanhSach<DeThi> {

    public static final String TEN_FILE = "dethi.txt";

    public DanhSachDeThi() {
        super(THU_MUC_MAC_DINH + "/" + TEN_FILE);
    }

    public DanhSachDeThi(String duongDanFile) {
        super(duongDanFile);
    }

    @Override
    protected String layMa(DeThi doiTuong) {
        return doiTuong.getMa();
    }

    @Override
    protected DeThi phanTichDong(String dong) {
        String[] t = tachTruong(dong, 6);
        kiemTraLoai(t[0], DeThi.KY_HIEU_LOAI);
        int thoiGian = Integer.parseInt(t[4]);
        if (thoiGian < 1 || thoiGian > 180) {
            throw new IllegalArgumentException("thời gian làm bài phải từ 1 đến 180 phút");
        }
        List<String> cacMa = new ArrayList<>();
        for (String m : t[5].split(",")) {
            String maCH = m.trim().toUpperCase();
            if (!maCH.isEmpty() && !cacMa.contains(maCH)) {
                cacMa.add(maCH);
            }
        }
        return new DeThi(t[1].toUpperCase(), t[2], t[3].toUpperCase(), thoiGian, cacMa);
    }

    @Override
    protected String chuyenThanhDong(DeThi doiTuong) {
        return doiTuong.toFileString();
    }

    @Override
    protected void hienThiPhanTu(DeThi doiTuong) {
        doiTuong.hienThi();
    }

    @Override
    protected void inTieuDe() {
        DeThi.inTieuDe();
    }

    public List<DeThi> timTheoTen(String tuKhoa) {
        List<DeThi> ketQua = new ArrayList<>();
        for (DeThi de : danhSach) {
            if (NhapLieu.chuaTuKhoa(de.getTenDe(), tuKhoa)) {
                ketQua.add(de);
            }
        }
        return ketQua;
    }

    public List<DeThi> timTheoMonHoc(String maMonHoc) {
        List<DeThi> ketQua = new ArrayList<>();
        for (DeThi de : danhSach) {
            if (de.getMaMonHoc().equalsIgnoreCase(maMonHoc.trim())) {
                ketQua.add(de);
            }
        }
        return ketQua;
    }

    /** Các đề đang chứa câu hỏi (để cảnh báo khi xóa câu hỏi). */
    public List<DeThi> timDeChuaCauHoi(String maCauHoi) {
        List<DeThi> ketQua = new ArrayList<>();
        for (DeThi de : danhSach) {
            if (de.coCauHoi(maCauHoi)) {
                ketQua.add(de);
            }
        }
        return ketQua;
    }

    /** Gỡ câu hỏi khỏi mọi đề rồi ghi file; trả về số đề bị ảnh hưởng. */
    public int goCauHoiKhoiCacDe(String maCauHoi) {
        int dem = 0;
        for (DeThi de : danhSach) {
            if (de.xoaCauHoi(maCauHoi)) {
                dem++;
            }
        }
        if (dem > 0) {
            luu();
        }
        return dem;
    }
}
