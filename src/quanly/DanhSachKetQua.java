package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * KẾ THỪA: lớp danh sách kết quả thi, extends DanhSach<KetQuaThi>.
 * Tìm kiếm: theo mã, theo thí sinh, theo đề thi, theo khoảng điểm.
 */
public class DanhSachKetQua extends DanhSach<KetQuaThi> {

    public static final String TEN_FILE = "ketqua.txt";

    public DanhSachKetQua() {
        super(THU_MUC_MAC_DINH + "/" + TEN_FILE);
    }

    public DanhSachKetQua(String duongDanFile) {
        super(duongDanFile);
    }

    @Override
    protected String layMa(KetQuaThi doiTuong) {
        return doiTuong.getMa();
    }

    @Override
    protected KetQuaThi phanTichDong(String dong) {
        String[] t = tachTruong(dong, 8);
        kiemTraLoai(t[0], KetQuaThi.KY_HIEU_LOAI);
        double diem = Double.parseDouble(t[4]);
        int soCauDung = Integer.parseInt(t[5]);
        int tongSoCau = Integer.parseInt(t[6]);
        if (diem < 0 || diem > 10) {
            throw new IllegalArgumentException("điểm phải từ 0 đến 10");
        }
        if (soCauDung < 0 || tongSoCau < 0 || soCauDung > tongSoCau) {
            throw new IllegalArgumentException("số câu đúng/tổng số câu không hợp lệ");
        }
        return new KetQuaThi(t[1].toUpperCase(), t[2].toUpperCase(), t[3].toUpperCase(),
                diem, soCauDung, tongSoCau, NhapLieu.docNgay(t[7]));
    }

    @Override
    protected String chuyenThanhDong(KetQuaThi doiTuong) {
        return doiTuong.toFileString();
    }

    @Override
    protected void hienThiPhanTu(KetQuaThi doiTuong) {
        doiTuong.hienThi();
    }

    @Override
    protected void inTieuDe() {
        KetQuaThi.inTieuDe();
    }

    public List<KetQuaThi> timTheoThiSinh(String maThiSinh) {
        List<KetQuaThi> ketQua = new ArrayList<>();
        for (KetQuaThi kq : danhSach) {
            if (kq.getMaThiSinh().equalsIgnoreCase(maThiSinh.trim())) {
                ketQua.add(kq);
            }
        }
        return ketQua;
    }

    public List<KetQuaThi> timTheoDeThi(String maDeThi) {
        List<KetQuaThi> ketQua = new ArrayList<>();
        for (KetQuaThi kq : danhSach) {
            if (kq.getMaDeThi().equalsIgnoreCase(maDeThi.trim())) {
                ketQua.add(kq);
            }
        }
        return ketQua;
    }

    public List<KetQuaThi> timTheoKhoangDiem(double tu, double den) {
        List<KetQuaThi> ketQua = new ArrayList<>();
        for (KetQuaThi kq : danhSach) {
            if (kq.getDiem() >= tu && kq.getDiem() <= den) {
                ketQua.add(kq);
            }
        }
        return ketQua;
    }
}
