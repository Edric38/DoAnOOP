package quanly;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Chức năng THỐNG KÊ - BÁO CÁO.
 * Dùng chung 5 danh sách với chương trình quản lý (quan hệ has-a), chỉ ĐỌC dữ liệu, không sửa.
 * Các nhóm kết quả (theo đề, môn, lớp, tháng, thí sinh) được cộng dồn bằng đối tượng ThongKeDiem.
 * Phương thức inXxx() chỉ in, không hỏi nhập, nên có thể dùng lại để xuất báo cáo ra file.
 */
public class ThongKe {

    public static final String TEN_FILE_BAO_CAO = "baocao_thongke.txt";
    private static final int DO_RONG_THANH = 30;
    private static final String KHONG_RO = "(không rõ)";
    private static final String DINH_DANG_NHOM = "%-8s %-34s %5s %6s %6s %6s %7s %5s %5s %5s %5s%n";

    private DanhSachMonHoc dsMonHoc;
    private DanhSachNguoiDung dsNguoiDung;
    private DanhSachCauHoi dsCauHoi;
    private DanhSachDeThi dsDeThi;
    private DanhSachKetQua dsKetQua;

    /** Constructor không tham số: tự tạo các danh sách và đọc dữ liệu từ thư mục data/. */
    public ThongKe() {
        this(new DanhSachMonHoc(), new DanhSachNguoiDung(), new DanhSachCauHoi(),
                new DanhSachDeThi(), new DanhSachKetQua());
        dsMonHoc.docLai();
        dsNguoiDung.docLai();
        dsCauHoi.docLai();
        dsDeThi.docLai();
        dsKetQua.docLai();
    }

    /** Constructor có tham số: dùng chung các danh sách với chương trình quản lý. */
    public ThongKe(DanhSachMonHoc dsMonHoc, DanhSachNguoiDung dsNguoiDung, DanhSachCauHoi dsCauHoi,
                   DanhSachDeThi dsDeThi, DanhSachKetQua dsKetQua) {
        this.dsMonHoc = dsMonHoc;
        this.dsNguoiDung = dsNguoiDung;
        this.dsCauHoi = dsCauHoi;
        this.dsDeThi = dsDeThi;
        this.dsKetQua = dsKetQua;
    }

    // ======================= MENU =======================

    public void menu() {
        while (true) {
            System.out.println();
            System.out.println("----- THỐNG KÊ - BÁO CÁO -----");
            System.out.println("1. Tổng quan hệ thống");
            System.out.println("2. Phổ điểm và xếp loại (toàn bộ kết quả)");
            System.out.println("3. Thống kê theo đề thi");
            System.out.println("4. Thống kê theo môn học");
            System.out.println("5. Bảng xếp hạng thí sinh");
            System.out.println("6. Thống kê theo lớp");
            System.out.println("7. Thống kê theo tháng thi");
            System.out.println("8. Thống kê ngân hàng câu hỏi");
            System.out.println("9. Xem chi tiết một thí sinh");
            System.out.println("10. Xuất toàn bộ báo cáo ra file");
            System.out.println("0. Quay lại");
            int chon = NhapLieu.nhapSoNguyen("Chọn", 0, 10);
            switch (chon) {
                case 0 -> {
                    return;
                }
                case 1 -> inTongQuan();
                case 2 -> inPhoDiem();
                case 3 -> inTheoDeThi();
                case 4 -> inTheoMonHoc();
                case 5 -> xepHangThiSinh();
                case 6 -> inTheoLop();
                case 7 -> inTheoThang();
                case 8 -> inThongKeCauHoi();
                case 9 -> chiTietThiSinh();
                case 10 -> xuatBaoCao(DanhSach.THU_MUC_MAC_DINH + "/" + TEN_FILE_BAO_CAO);
                default -> System.out.println("Chức năng không hợp lệ.");
            }
        }
    }

    // ======================= 1. TỔNG QUAN =======================

    public void inTongQuan() {
        inTieuDeMuc("TỔNG QUAN HỆ THỐNG");
        int soThiSinh = dsNguoiDung.layDanhSachThiSinh().size();
        int soMonCoCauHoi = 0;
        for (MonHoc mh : dsMonHoc.getTatCa()) {
            if (!dsCauHoi.timTheoMonHoc(mh.getMa()).isEmpty()) {
                soMonCoCauHoi++;
            }
        }
        System.out.printf("Môn học     : %d (trong đó %d môn đã có câu hỏi)%n", dsMonHoc.soLuong(), soMonCoCauHoi);
        System.out.printf("Người dùng  : %d (%d thí sinh, %d giáo viên)%n", dsNguoiDung.soLuong(),
                soThiSinh, dsNguoiDung.soLuong() - soThiSinh);
        System.out.printf("Câu hỏi     : %d (%d một đáp án, %d nhiều đáp án, %d đúng/sai)%n", dsCauHoi.soLuong(),
                dsCauHoi.timTheoLoai(CauHoiMotDapAn.KY_HIEU_LOAI).size(),
                dsCauHoi.timTheoLoai(CauHoiNhieuDapAn.KY_HIEU_LOAI).size(),
                dsCauHoi.timTheoLoai(CauHoiDungSai.KY_HIEU_LOAI).size());
        System.out.printf("Đề thi      : %d%n", dsDeThi.soLuong());
        System.out.printf("Kết quả thi : %d lượt thi%n", dsKetQua.soLuong());
        if (dsKetQua.soLuong() == 0) {
            System.out.println("(Chưa có kết quả thi nào để thống kê điểm)");
            return;
        }
        ThongKeDiem tk = thongKeTatCa();
        Map<String, ThongKeDiem> theoThiSinh = nhomKetQua(KetQuaThi::getMaThiSinh);
        Map<String, ThongKeDiem> theoDe = nhomKetQua(KetQuaThi::getMaDeThi);
        System.out.printf("Số thí sinh đã dự thi : %d/%d%n", theoThiSinh.size(), soThiSinh);
        System.out.printf("Điểm trung bình chung : %.2f (độ lệch chuẩn %.2f)%n", tk.getTrungBinh(), tk.getDoLechChuan());
        System.out.printf("Điểm cao nhất / thấp nhất: %.2f / %.2f%n", tk.getDiemCaoNhat(), tk.getDiemThapNhat());
        System.out.printf("Tỉ lệ đạt (>= %.1f)    : %.1f%% (%d/%d lượt)%n", ThongKeDiem.DIEM_DAT,
                tk.getTiLeDat(), tk.getSoDat(), tk.getSoLuot());

        ThongKeDiem deNhieuNhat = timNhom(theoDe, Comparator.comparingInt(ThongKeDiem::getSoLuot));
        DeThi de = dsDeThi.timTheoMa(deNhieuNhat.getTenNhom());
        System.out.printf("Đề được thi nhiều nhất : %s - %s (%d lượt)%n", deNhieuNhat.getTenNhom(),
                de == null ? KHONG_RO : de.getTenDe(), deNhieuNhat.getSoLuot());
        ThongKeDiem tsGioiNhat = timNhom(theoThiSinh, Comparator.comparingDouble(ThongKeDiem::getTrungBinh));
        System.out.printf("Thí sinh có điểm TB cao nhất: %s - %s (%.2f)%n", tsGioiNhat.getTenNhom(),
                tenThiSinh(tsGioiNhat.getTenNhom()), tsGioiNhat.getTrungBinh());
        LocalDate dau = null;
        LocalDate cuoi = null;
        for (KetQuaThi kq : dsKetQua.getTatCa()) {
            if (dau == null || kq.getNgayThi().isBefore(dau)) {
                dau = kq.getNgayThi();
            }
            if (cuoi == null || kq.getNgayThi().isAfter(cuoi)) {
                cuoi = kq.getNgayThi();
            }
        }
        System.out.printf("Khoảng thời gian thi  : %s - %s%n", NhapLieu.dinhDangNgay(dau), NhapLieu.dinhDangNgay(cuoi));
    }

    // ======================= 2. PHỔ ĐIỂM =======================

    public void inPhoDiem() {
        inTieuDeMuc("PHỔ ĐIỂM VÀ XẾP LOẠI");
        if (!kiemTraCoKetQua()) {
            return;
        }
        ThongKeDiem tk = thongKeTatCa();
        System.out.println("Xếp loại (Giỏi >= 8, Khá >= 6.5, Trung bình >= 5, Yếu < 5):");
        for (String xl : ThongKeDiem.CAC_XEP_LOAI) {
            inDongBieuDo(xl, tk.getSoTheoXepLoai(xl), tk.getSoLuot());
        }

        System.out.println();
        System.out.println("Phổ điểm theo khoảng:");
        int[] dem = new int[5];
        String[] nhan = {"0 - <2", "2 - <4", "4 - <6", "6 - <8", "8 - 10"};
        for (KetQuaThi kq : dsKetQua.getTatCa()) {
            dem[Math.min(4, (int) (kq.getDiem() / 2))]++;
        }
        for (int i = 0; i < dem.length; i++) {
            inDongBieuDo(nhan[i], dem[i], tk.getSoLuot());
        }

        System.out.println();
        System.out.printf("Đạt: %d lượt (%.1f%%) | Không đạt: %d lượt (%.1f%%)%n", tk.getSoDat(), tk.getTiLeDat(),
                tk.getSoKhongDat(), 100 - tk.getTiLeDat());
        System.out.printf("Trung bình %.2f | Cao nhất %.2f | Thấp nhất %.2f | Độ lệch chuẩn %.2f%n",
                tk.getTrungBinh(), tk.getDiemCaoNhat(), tk.getDiemThapNhat(), tk.getDoLechChuan());
    }

    // ======================= 3. THEO ĐỀ THI =======================

    public void inTheoDeThi() {
        inTieuDeMuc("THỐNG KÊ THEO ĐỀ THI");
        if (!kiemTraCoKetQua()) {
            return;
        }
        Map<String, ThongKeDiem> theoDe = nhomKetQua(KetQuaThi::getMaDeThi);
        inTieuDeBangNhom("Mã đề", "Tên đề");
        int deChuaThi = 0;
        for (DeThi de : dsDeThi.getTatCa()) {
            ThongKeDiem tk = theoDe.remove(de.getMa());
            if (tk == null) {
                deChuaThi++;
            } else {
                inDongNhom(de.getMa(), de.getTenDe(), tk);
            }
        }
        for (ThongKeDiem tk : theoDe.values()) { // đề đã bị xóa nhưng vẫn còn kết quả
            inDongNhom(tk.getTenNhom(), "(đề đã bị xóa)", tk);
        }
        System.out.println("Số đề chưa có lượt thi nào: " + deChuaThi);
    }

    // ======================= 4. THEO MÔN HỌC =======================

    public void inTheoMonHoc() {
        inTieuDeMuc("THỐNG KÊ THEO MÔN HỌC");
        if (!kiemTraCoKetQua()) {
            return;
        }
        Map<String, ThongKeDiem> theoMon = nhomKetQua(kq -> {
            DeThi de = dsDeThi.timTheoMa(kq.getMaDeThi());
            return de == null ? KHONG_RO : de.getMaMonHoc();
        });
        inTieuDeBangNhom("Mã môn", "Tên môn");
        int monChuaThi = 0;
        for (MonHoc mh : dsMonHoc.getTatCa()) {
            ThongKeDiem tk = theoMon.remove(mh.getMa());
            if (tk == null) {
                monChuaThi++;
            } else {
                inDongNhom(mh.getMa(), mh.getTenMon(), tk);
                System.out.printf("%-8s   (ngân hàng: %d câu hỏi, %d đề thi)%n", "",
                        dsCauHoi.timTheoMonHoc(mh.getMa()).size(), dsDeThi.timTheoMonHoc(mh.getMa()).size());
            }
        }
        for (ThongKeDiem tk : theoMon.values()) {
            inDongNhom(tk.getTenNhom(), "(môn không còn tồn tại)", tk);
        }
        System.out.println("Số môn học chưa có lượt thi nào: " + monChuaThi + "/" + dsMonHoc.soLuong());
    }

    // ======================= 5. XẾP HẠNG THÍ SINH =======================

    private void xepHangThiSinh() {
        if (dsKetQua.soLuong() == 0) {
            inTieuDeMuc("BẢNG XẾP HẠNG THÍ SINH");
            kiemTraCoKetQua();
            return;
        }
        int soDaThi = nhomKetQua(KetQuaThi::getMaThiSinh).size();
        int topN = NhapLieu.nhapSoNguyen("Hiển thị bao nhiêu thí sinh đứng đầu (1-" + soDaThi + ")", 1, soDaThi);
        inXepHangThiSinh(topN);
    }

    /** Xếp hạng theo điểm trung bình giảm dần; bằng nhau thì xét điểm cao nhất, rồi số lượt thi. */
    public void inXepHangThiSinh(int topN) {
        inTieuDeMuc("BẢNG XẾP HẠNG THÍ SINH (theo điểm trung bình)");
        if (!kiemTraCoKetQua()) {
            return;
        }
        List<ThongKeDiem> ds = new ArrayList<>(nhomKetQua(KetQuaThi::getMaThiSinh).values());
        ds.sort(Comparator.comparingDouble(ThongKeDiem::getTrungBinh)
                .thenComparingDouble(ThongKeDiem::getDiemCaoNhat)
                .thenComparingInt(ThongKeDiem::getSoLuot)
                .reversed());
        String dinhDang = "%-5s %-7s %-24s %-8s %5s %6s %6s %6s  %s%n";
        System.out.printf(dinhDang, "Hạng", "Mã", "Họ tên", "Lớp", "Lượt", "TB", "Cao", "Thấp", "Xếp loại TB");
        System.out.println("-".repeat(90));
        for (int i = 0; i < Math.min(topN, ds.size()); i++) {
            ThongKeDiem tk = ds.get(i);
            NguoiDung nd = dsNguoiDung.timTheoMa(tk.getTenNhom());
            String lop = nd instanceof ThiSinh ? ((ThiSinh) nd).getLop() : "";
            System.out.printf(dinhDang, i + 1, tk.getTenNhom(), NhapLieu.rutGon(tenThiSinh(tk.getTenNhom()), 24), lop,
                    tk.getSoLuot(), dinh2(tk.getTrungBinh()), dinh2(tk.getDiemCaoNhat()), dinh2(tk.getDiemThapNhat()),
                    KetQuaThi.xepLoai(tk.getTrungBinh()));
        }
        System.out.println("Hiển thị " + Math.min(topN, ds.size()) + "/" + ds.size() + " thí sinh đã dự thi.");

        List<String> chuaThi = new ArrayList<>();
        Map<String, ThongKeDiem> theoThiSinh = nhomKetQua(KetQuaThi::getMaThiSinh);
        for (ThiSinh ts : dsNguoiDung.layDanhSachThiSinh()) {
            if (!theoThiSinh.containsKey(ts.getMa())) {
                chuaThi.add(ts.getMa() + " (" + ts.getHoTen() + ")");
            }
        }
        System.out.println("Thí sinh chưa dự thi lần nào: " + (chuaThi.isEmpty() ? "không có" : String.join(", ", chuaThi)));
    }

    // ======================= 6. THEO LỚP =======================

    public void inTheoLop() {
        inTieuDeMuc("THỐNG KÊ THEO LỚP");
        if (!kiemTraCoKetQua()) {
            return;
        }
        Map<String, ThongKeDiem> theoLop = new TreeMap<>(nhomKetQua(kq -> {
            NguoiDung nd = dsNguoiDung.timTheoMa(kq.getMaThiSinh());
            return nd instanceof ThiSinh ? ((ThiSinh) nd).getLop() : KHONG_RO;
        }));
        Map<String, Integer> siSo = new TreeMap<>();
        for (ThiSinh ts : dsNguoiDung.layDanhSachThiSinh()) {
            siSo.merge(ts.getLop(), 1, Integer::sum);
        }
        inTieuDeBangNhom("Lớp", "Sĩ số");
        for (ThongKeDiem tk : theoLop.values()) {
            Integer n = siSo.get(tk.getTenNhom());
            inDongNhom(tk.getTenNhom(), (n == null ? 0 : n) + " thí sinh", tk);
        }
        ThongKeDiem lopTot = timNhom(theoLop, Comparator.comparingDouble(ThongKeDiem::getTrungBinh));
        System.out.printf("Lớp có điểm trung bình cao nhất: %s (%.2f)%n", lopTot.getTenNhom(), lopTot.getTrungBinh());
    }

    // ======================= 7. THEO THÁNG =======================

    public void inTheoThang() {
        inTieuDeMuc("THỐNG KÊ THEO THÁNG THI");
        if (!kiemTraCoKetQua()) {
            return;
        }
        DateTimeFormatter khoaThang = DateTimeFormatter.ofPattern("uuuu-MM");
        DateTimeFormatter hienThang = DateTimeFormatter.ofPattern("MM/uuuu");
        // TreeMap theo khóa "yyyy-MM" để tự sắp xếp tăng dần theo thời gian
        Map<String, ThongKeDiem> theoThang = new TreeMap<>(nhomKetQua(kq -> kq.getNgayThi().format(khoaThang)));
        int nhieuNhat = 0;
        for (ThongKeDiem tk : theoThang.values()) {
            nhieuNhat = Math.max(nhieuNhat, tk.getSoLuot());
        }
        System.out.printf("%-8s %5s %6s %7s  %s%n", "Tháng", "Lượt", "TB", "Đạt %", "Số lượt thi");
        System.out.println("-".repeat(65));
        for (ThongKeDiem tk : theoThang.values()) {
            String thang = LocalDate.parse(tk.getTenNhom() + "-01").format(hienThang);
            System.out.printf("%-8s %5d %6s %6.1f%%  %s%n", thang, tk.getSoLuot(), dinh2(tk.getTrungBinh()),
                    tk.getTiLeDat(), thanh(tk.getSoLuot(), nhieuNhat));
        }
    }

    // ======================= 8. NGÂN HÀNG CÂU HỎI =======================

    public void inThongKeCauHoi() {
        inTieuDeMuc("THỐNG KÊ NGÂN HÀNG CÂU HỎI");
        int tong = dsCauHoi.soLuong();
        if (tong == 0) {
            System.out.println("(Chưa có câu hỏi nào)");
            return;
        }
        System.out.println("Theo loại câu hỏi:");
        inDongBieuDo("Một đáp án", dsCauHoi.timTheoLoai(CauHoiMotDapAn.KY_HIEU_LOAI).size(), tong);
        inDongBieuDo("Nhiều đáp án", dsCauHoi.timTheoLoai(CauHoiNhieuDapAn.KY_HIEU_LOAI).size(), tong);
        inDongBieuDo("Đúng/Sai", dsCauHoi.timTheoLoai(CauHoiDungSai.KY_HIEU_LOAI).size(), tong);
        System.out.println("Theo độ khó:");
        for (int doKho = 1; doKho <= 3; doKho++) {
            inDongBieuDo(CauHoi.tenDoKho(doKho), dsCauHoi.timTheoDoKho(doKho).size(), tong);
        }

        // Số lần mỗi câu hỏi xuất hiện trong các đề thi
        Map<String, Integer> soLanDung = new LinkedHashMap<>();
        for (CauHoi ch : dsCauHoi.getTatCa()) {
            soLanDung.put(ch.getMa(), dsDeThi.timDeChuaCauHoi(ch.getMa()).size());
        }

        System.out.println();
        System.out.println("Theo môn học (chỉ liệt kê môn đã có câu hỏi):");
        String dinhDang = "%-7s %-34s %5s %5s %6s %8s %4s %4s %4s %8s%n";
        System.out.printf(dinhDang, "Mã môn", "Tên môn", "Tổng", "Một", "Nhiều", "Đúng/Sai", "Dễ", "TB", "Khó", "Trong đề");
        System.out.println("-".repeat(95));
        Map<String, List<CauHoi>> theoMon = new LinkedHashMap<>();
        for (CauHoi ch : dsCauHoi.getTatCa()) {
            theoMon.computeIfAbsent(ch.getMaMonHoc(), k -> new ArrayList<>()).add(ch);
        }
        for (Map.Entry<String, List<CauHoi>> e : theoMon.entrySet()) {
            int[] loai = new int[3];
            int[] doKho = new int[3];
            int trongDe = 0;
            for (CauHoi ch : e.getValue()) {
                String kh = ch.getKyHieuLoai(); // ĐA HÌNH
                loai[kh.equals(CauHoiMotDapAn.KY_HIEU_LOAI) ? 0 : kh.equals(CauHoiNhieuDapAn.KY_HIEU_LOAI) ? 1 : 2]++;
                doKho[Math.max(1, Math.min(3, ch.getDoKho())) - 1]++;
                if (soLanDung.get(ch.getMa()) > 0) {
                    trongDe++;
                }
            }
            MonHoc mh = dsMonHoc.timTheoMa(e.getKey());
            System.out.printf(dinhDang, e.getKey(), NhapLieu.rutGon(mh == null ? KHONG_RO : mh.getTenMon(), 34),
                    e.getValue().size(), loai[0], loai[1], loai[2], doKho[0], doKho[1], doKho[2],
                    trongDe + "/" + e.getValue().size());
        }

        System.out.println();
        List<Map.Entry<String, Integer>> sapXep = new ArrayList<>(soLanDung.entrySet());
        sapXep.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        System.out.println("5 câu hỏi được dùng trong nhiều đề nhất:");
        for (int i = 0; i < Math.min(5, sapXep.size()); i++) {
            CauHoi ch = dsCauHoi.timTheoMa(sapXep.get(i).getKey());
            System.out.printf("  %-6s %2d đề  %s%n", ch.getMa(), sapXep.get(i).getValue(), NhapLieu.rutGon(ch.getNoiDung(), 60));
        }
        List<String> chuaDung = new ArrayList<>();
        for (Map.Entry<String, Integer> e : soLanDung.entrySet()) {
            if (e.getValue() == 0) {
                chuaDung.add(e.getKey());
            }
        }
        System.out.println("Câu hỏi chưa nằm trong đề nào (" + chuaDung.size() + "): "
                + (chuaDung.isEmpty() ? "không có" : String.join(", ", chuaDung)));
    }

    // ======================= 9. CHI TIẾT MỘT THÍ SINH =======================

    private void chiTietThiSinh() {
        String ma = NhapLieu.nhapMa("Mã thí sinh");
        NguoiDung nd = dsNguoiDung.timTheoMa(ma);
        if (!(nd instanceof ThiSinh)) {
            System.out.println(nd == null ? "Không tìm thấy thí sinh " + ma + "." : ma + " không phải thí sinh.");
            return;
        }
        inChiTietThiSinh((ThiSinh) nd);
    }

    public void inChiTietThiSinh(ThiSinh ts) {
        inTieuDeMuc("KẾT QUẢ CỦA THÍ SINH " + ts.getMa() + " - " + ts.getHoTen() + " (lớp " + ts.getLop() + ")");
        List<KetQuaThi> ds = new ArrayList<>(dsKetQua.timTheoThiSinh(ts.getMa()));
        if (ds.isEmpty()) {
            System.out.println("(Thí sinh chưa dự thi lần nào)");
            return;
        }
        ds.sort(Comparator.comparing(KetQuaThi::getNgayThi));
        String dinhDang = "%-11s %-7s %-34s %6s %8s  %s%n";
        System.out.printf(dinhDang, "Ngày thi", "Mã đề", "Tên đề", "Điểm", "Đúng", "Xếp loại");
        System.out.println("-".repeat(85));
        ThongKeDiem tk = new ThongKeDiem(ts.getMa());
        for (KetQuaThi kq : ds) {
            DeThi de = dsDeThi.timTheoMa(kq.getMaDeThi());
            System.out.printf(dinhDang, NhapLieu.dinhDangNgay(kq.getNgayThi()), kq.getMaDeThi(),
                    NhapLieu.rutGon(de == null ? KHONG_RO : de.getTenDe(), 34), dinh2(kq.getDiem()),
                    kq.getSoCauDung() + "/" + kq.getTongSoCau(), KetQuaThi.xepLoai(kq.getDiem()));
            tk.them(kq);
        }
        System.out.printf("Số lần thi: %d | TB: %.2f | Cao nhất: %.2f | Thấp nhất: %.2f | Đạt: %d/%d%n",
                tk.getSoLuot(), tk.getTrungBinh(), tk.getDiemCaoNhat(), tk.getDiemThapNhat(), tk.getSoDat(), tk.getSoLuot());
        if (ds.size() >= 2) {
            double chenh = ds.get(ds.size() - 1).getDiem() - ds.get(0).getDiem();
            System.out.printf("So với lần thi đầu tiên: %s%.2f điểm (%s)%n", chenh >= 0 ? "+" : "", chenh,
                    chenh > 0 ? "tiến bộ" : chenh < 0 ? "giảm" : "không đổi");
        }
        // Xếp hạng của thí sinh trong toàn bộ thí sinh đã thi
        List<ThongKeDiem> tatCa = new ArrayList<>(nhomKetQua(KetQuaThi::getMaThiSinh).values());
        int hang = 1;
        for (ThongKeDiem khac : tatCa) {
            if (khac.getTrungBinh() > tk.getTrungBinh()) {
                hang++;
            }
        }
        System.out.println("Xếp hạng theo điểm trung bình: " + hang + "/" + tatCa.size());
    }

    // ======================= 10. XUẤT BÁO CÁO =======================

    /** Ghi toàn bộ các bảng thống kê ra file text UTF-8 (đổi tạm System.out sang file). */
    public boolean xuatBaoCao(String duongDan) {
        Path p = Paths.get(duongDan);
        PrintStream manHinh = System.out;
        try {
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            try (PrintStream file = new PrintStream(new FileOutputStream(p.toFile()), true, StandardCharsets.UTF_8)) {
                System.setOut(file);
                System.out.println("BÁO CÁO THỐNG KÊ - QUẢN LÝ THI TRẮC NGHIỆM");
                System.out.println("Thời điểm xuất: "
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss")));
                inTongQuan();
                inPhoDiem();
                inTheoDeThi();
                inTheoMonHoc();
                inXepHangThiSinh(Integer.MAX_VALUE);
                inTheoLop();
                inTheoThang();
                inThongKeCauHoi();
            } finally {
                System.setOut(manHinh);
            }
            System.out.println("Đã xuất báo cáo thống kê ra file " + duongDan + ".");
            return true;
        } catch (IOException e) {
            System.setOut(manHinh);
            System.out.println("Lỗi ghi file báo cáo " + duongDan + ": " + e.getMessage());
            return false;
        }
    }

    // ======================= HỖ TRỢ =======================

    /** Gom kết quả thi theo một khóa (mã đề, mã thí sinh, lớp, tháng...), giữ thứ tự xuất hiện. */
    public Map<String, ThongKeDiem> nhomKetQua(Function<KetQuaThi, String> layKhoa) {
        Map<String, ThongKeDiem> nhom = new LinkedHashMap<>();
        for (KetQuaThi kq : dsKetQua.getTatCa()) {
            String khoa = layKhoa.apply(kq);
            nhom.computeIfAbsent(khoa, ThongKeDiem::new).them(kq);
        }
        return nhom;
    }

    public ThongKeDiem thongKeTatCa() {
        ThongKeDiem tk = new ThongKeDiem("Tất cả");
        for (KetQuaThi kq : dsKetQua.getTatCa()) {
            tk.them(kq);
        }
        return tk;
    }

    /** Lấy nhóm có giá trị lớn nhất theo tiêu chí so sánh. */
    private static ThongKeDiem timNhom(Map<String, ThongKeDiem> nhom, Comparator<ThongKeDiem> tieuChi) {
        ThongKeDiem tot = null;
        for (ThongKeDiem tk : nhom.values()) {
            if (tot == null || tieuChi.compare(tk, tot) > 0) {
                tot = tk;
            }
        }
        return tot;
    }

    private String tenThiSinh(String ma) {
        NguoiDung nd = dsNguoiDung.timTheoMa(ma);
        return nd == null ? "(đã xóa)" : nd.getHoTen();
    }

    private boolean kiemTraCoKetQua() {
        if (dsKetQua.soLuong() == 0) {
            System.out.println("(Chưa có kết quả thi nào để thống kê)");
            return false;
        }
        return true;
    }

    private static void inTieuDeMuc(String tieuDe) {
        System.out.println();
        System.out.println("=".repeat(95));
        System.out.println("  " + tieuDe);
        System.out.println("=".repeat(95));
    }

    private static void inTieuDeBangNhom(String cotMa, String cotTen) {
        System.out.printf(DINH_DANG_NHOM, cotMa, cotTen, "Lượt", "TB", "Cao", "Thấp", "Đạt %", "Giỏi", "Khá", "TB", "Yếu");
        System.out.println("-".repeat(102));
    }

    private static void inDongNhom(String ma, String ten, ThongKeDiem tk) {
        System.out.printf(DINH_DANG_NHOM, ma, NhapLieu.rutGon(ten, 34), tk.getSoLuot(), dinh2(tk.getTrungBinh()),
                dinh2(tk.getDiemCaoNhat()), dinh2(tk.getDiemThapNhat()), String.format("%.1f", tk.getTiLeDat()),
                tk.getSoTheoXepLoai("Giỏi"), tk.getSoTheoXepLoai("Khá"),
                tk.getSoTheoXepLoai("Trung bình"), tk.getSoTheoXepLoai("Yếu"));
    }

    /** In một dòng biểu đồ ngang: nhãn, số lượng, phần trăm và thanh '#'. */
    private static void inDongBieuDo(String nhan, int soLuong, int tong) {
        double phanTram = tong == 0 ? 0 : soLuong * 100.0 / tong;
        System.out.printf("  %-12s %4d (%5.1f%%) %s%n", nhan, soLuong, phanTram, thanh(soLuong, tong));
    }

    /** STATIC: thanh biểu đồ dài tỉ lệ với giaTri/toiDa. */
    public static String thanh(int giaTri, int toiDa) {
        if (toiDa <= 0 || giaTri <= 0) {
            return "";
        }
        int doDai = (int) Math.round(giaTri * (double) DO_RONG_THANH / toiDa);
        return "#".repeat(Math.max(1, doDai));
    }

    private static String dinh2(double so) {
        return String.format("%.2f", so);
    }
}
