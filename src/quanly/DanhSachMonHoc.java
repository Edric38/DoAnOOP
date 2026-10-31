package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * KẾ THỪA: lớp danh sách môn học, extends DanhSach<MonHoc>.
 * Tìm kiếm: theo mã (timTheoMa kế thừa) và theo tên (timTheoTen).
 */
public class DanhSachMonHoc extends DanhSach<MonHoc> {

    public static final String TEN_FILE = "monhoc.txt";

    public DanhSachMonHoc() {
        super(THU_MUC_MAC_DINH + "/" + TEN_FILE);
    }

    public DanhSachMonHoc(String duongDanFile) {
        super(duongDanFile);
    }

    @Override
    protected String layMa(MonHoc doiTuong) {
        return doiTuong.getMa();
    }

    @Override
    protected MonHoc phanTichDong(String dong) {
        String[] t = tachTruong(dong, 4);
        kiemTraLoai(t[0], MonHoc.KY_HIEU_LOAI);
        int soTinChi = Integer.parseInt(t[3]);
        if (soTinChi < 1 || soTinChi > 10) {
            throw new IllegalArgumentException("số tín chỉ phải từ 1 đến 10");
        }
        return new MonHoc(t[1].toUpperCase(), t[2], soTinChi);
    }

    @Override
    protected String chuyenThanhDong(MonHoc doiTuong) {
        return doiTuong.toFileString();
    }

    @Override
    protected void hienThiPhanTu(MonHoc doiTuong) {
        doiTuong.hienThi();
    }

    @Override
    protected void inTieuDe() {
        MonHoc.inTieuDe();
    }

    /** Tìm theo tên gần đúng, không phân biệt hoa thường và dấu. */
    public List<MonHoc> timTheoTen(String tuKhoa) {
        List<MonHoc> ketQua = new ArrayList<>();
        for (MonHoc mh : danhSach) {
            if (NhapLieu.chuaTuKhoa(mh.getTenMon(), tuKhoa)) {
                ketQua.add(mh);
            }
        }
        return ketQua;
    }
}
