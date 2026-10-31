package quanly;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Lớp tiện ích nhập liệu và kiểm tra dữ liệu.
 * STATIC: mọi phương thức đều là static và dùng chung MỘT Scanner static,
 * tránh tạo nhiều Scanner trên System.in (gây mất dữ liệu đệm).
 * Mọi phương thức nhập đều bắt lỗi, báo lỗi tiếng Việt và cho nhập lại.
 */
public class NhapLieu {

    // THUỘC TÍNH STATIC: Scanner dùng chung cho toàn chương trình
    private static Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

    // THUỘC TÍNH STATIC: định dạng ngày dd/MM/yyyy, chế độ STRICT để chặn ngày sai như 31/02
    public static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final Pattern MAU_EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$");
    private static final Pattern MAU_MA = Pattern.compile("^[A-Z0-9_]{2,15}$");

    /** Constructor không tham số: giữ nguyên Scanner dùng chung hiện tại. */
    public NhapLieu() {
        this(sc);
    }

    /** Constructor có tham số: cho phép thay nguồn nhập dùng chung (ví dụ khi kiểm thử). */
    public NhapLieu(Scanner nguonNhap) {
        if (nguonNhap != null) {
            sc = nguonNhap;
        }
    }

    // ================== PHƯƠNG THỨC STATIC ĐỌC DÒNG ==================

    /** Đọc một dòng; nếu hết dữ liệu nhập (Ctrl+D / Ctrl+Z) thì kết thúc chương trình êm đẹp. */
    private static String docDong() {
        if (!sc.hasNextLine()) {
            System.out.println();
            System.out.println("Không còn dữ liệu nhập. Chương trình kết thúc.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // ================== PHƯƠNG THỨC STATIC KIỂM TRA DỮ LIỆU ==================

    /** Ký tự '|' dùng làm dấu ngăn trường trong file nên không được xuất hiện trong dữ liệu nhập. */
    public static boolean chuaKyTuCam(String s) {
        return s != null && s.contains("|");
    }

    public static boolean kiemTraEmail(String email) {
        return email != null && MAU_EMAIL.matcher(email).matches();
    }

    /** Mã hợp lệ: 2-15 ký tự gồm chữ in hoa, số, gạch dưới. */
    public static boolean kiemTraMa(String ma) {
        return ma != null && MAU_MA.matcher(ma).matches();
    }

    // ================== NHẬP CHUỖI ==================

    /** Nhập chuỗi bắt buộc, không rỗng, không chứa '|'. */
    public static String nhapChuoi(String nhanDe) {
        while (true) {
            System.out.print(nhanDe + ": ");
            String s = docDong();
            if (s.isEmpty()) {
                System.out.println("Lỗi: không được để trống, vui lòng nhập lại.");
            } else if (chuaKyTuCam(s)) {
                System.out.println("Lỗi: không được chứa ký tự '|', vui lòng nhập lại.");
            } else {
                return s;
            }
        }
    }

    /** Nhập chuỗi khi sửa: Enter để giữ giá trị cũ. */
    public static String nhapChuoiTuyChon(String nhanDe, String macDinh) {
        while (true) {
            System.out.print(nhanDe + " [" + macDinh + "]: ");
            String s = docDong();
            if (s.isEmpty()) {
                return macDinh;
            }
            if (chuaKyTuCam(s)) {
                System.out.println("Lỗi: không được chứa ký tự '|', vui lòng nhập lại.");
            } else {
                return s;
            }
        }
    }

    /** Nhập chuỗi được phép rỗng (dùng khi thí sinh bỏ qua câu hỏi). */
    public static String nhapChuoiCoTheRong(String nhanDe) {
        while (true) {
            System.out.print(nhanDe + ": ");
            String s = docDong();
            if (chuaKyTuCam(s)) {
                System.out.println("Lỗi: không được chứa ký tự '|', vui lòng nhập lại.");
            } else {
                return s;
            }
        }
    }

    // ================== NHẬP MÃ ==================

    /** Nhập mã bắt buộc, tự đổi sang chữ in hoa và kiểm tra định dạng. */
    public static String nhapMa(String nhanDe) {
        while (true) {
            String ma = nhapChuoi(nhanDe).toUpperCase();
            if (kiemTraMa(ma)) {
                return ma;
            }
            System.out.println("Lỗi: mã chỉ gồm 2-15 ký tự chữ, số hoặc '_' (ví dụ TS006).");
        }
    }

    /** Nhập mã, Enter để dùng mã mặc định (ví dụ mã tự sinh). */
    public static String nhapMaTuyChon(String nhanDe, String macDinh) {
        while (true) {
            String ma = nhapChuoiTuyChon(nhanDe, macDinh).toUpperCase();
            if (kiemTraMa(ma)) {
                return ma;
            }
            System.out.println("Lỗi: mã chỉ gồm 2-15 ký tự chữ, số hoặc '_' (ví dụ TS006).");
        }
    }

    // ================== NHẬP SỐ NGUYÊN ==================

    /** Chuyển chuỗi thành số nguyên trong [min, max]; lỗi thì in thông báo và trả về null. */
    private static Integer chuyenSoNguyen(String s, int min, int max) {
        try {
            int so = Integer.parseInt(s);
            if (so < min || so > max) {
                System.out.println("Lỗi: giá trị phải trong khoảng " + min + " đến " + max + ".");
                return null;
            }
            return so;
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: \"" + s + "\" không phải số nguyên hợp lệ, vui lòng nhập lại.");
            return null;
        }
    }

    public static int nhapSoNguyen(String nhanDe, int min, int max) {
        while (true) {
            System.out.print(nhanDe + ": ");
            Integer so = chuyenSoNguyen(docDong(), min, max);
            if (so != null) {
                return so;
            }
        }
    }

    public static int nhapSoNguyenTuyChon(String nhanDe, int min, int max, int macDinh) {
        while (true) {
            System.out.print(nhanDe + " [" + macDinh + "]: ");
            String s = docDong();
            if (s.isEmpty()) {
                return macDinh;
            }
            Integer so = chuyenSoNguyen(s, min, max);
            if (so != null) {
                return so;
            }
        }
    }

    // ================== NHẬP SỐ THỰC ==================

    /** Chuyển chuỗi thành số thực trong [min, max], chấp nhận cả dấu phẩy thập phân. */
    private static Double chuyenSoThuc(String s, double min, double max) {
        try {
            double so = Double.parseDouble(s.replace(',', '.'));
            if (!Double.isFinite(so) || so < min || so > max) {
                System.out.println("Lỗi: giá trị phải trong khoảng " + min + " đến " + max + ".");
                return null;
            }
            return so;
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: \"" + s + "\" không phải số hợp lệ, vui lòng nhập lại.");
            return null;
        }
    }

    public static double nhapSoThuc(String nhanDe, double min, double max) {
        while (true) {
            System.out.print(nhanDe + ": ");
            Double so = chuyenSoThuc(docDong(), min, max);
            if (so != null) {
                return so;
            }
        }
    }

    public static double nhapSoThucTuyChon(String nhanDe, double min, double max, double macDinh) {
        while (true) {
            System.out.print(nhanDe + " [" + macDinh + "]: ");
            String s = docDong();
            if (s.isEmpty()) {
                return macDinh;
            }
            Double so = chuyenSoThuc(s, min, max);
            if (so != null) {
                return so;
            }
        }
    }

    // ================== NHẬP NGÀY ==================

    /** Chuyển chuỗi dd/MM/yyyy thành ngày; ngày phải từ năm 1900 và không sau hôm nay. */
    private static LocalDate chuyenNgay(String s) {
        try {
            LocalDate ngay = LocalDate.parse(s, DINH_DANG_NGAY);
            if (ngay.isAfter(LocalDate.now())) {
                System.out.println("Lỗi: ngày không được sau ngày hôm nay.");
                return null;
            }
            if (ngay.getYear() < 1900) {
                System.out.println("Lỗi: năm phải từ 1900 trở đi.");
                return null;
            }
            return ngay;
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi: ngày \"" + s + "\" không hợp lệ (định dạng dd/MM/yyyy, ví dụ 05/09/2004).");
            return null;
        }
    }

    public static LocalDate nhapNgay(String nhanDe) {
        while (true) {
            System.out.print(nhanDe + " (dd/MM/yyyy): ");
            LocalDate ngay = chuyenNgay(docDong());
            if (ngay != null) {
                return ngay;
            }
        }
    }

    public static LocalDate nhapNgayTuyChon(String nhanDe, LocalDate macDinh) {
        while (true) {
            System.out.print(nhanDe + " (dd/MM/yyyy) [" + dinhDangNgay(macDinh) + "]: ");
            String s = docDong();
            if (s.isEmpty()) {
                return macDinh;
            }
            LocalDate ngay = chuyenNgay(s);
            if (ngay != null) {
                return ngay;
            }
        }
    }

    /** Đọc ngày từ file (ném ngoại lệ nếu sai để lớp danh sách bỏ qua dòng lỗi). */
    public static LocalDate docNgay(String s) {
        return LocalDate.parse(s.trim(), DINH_DANG_NGAY);
    }

    public static String dinhDangNgay(LocalDate ngay) {
        return ngay == null ? "" : ngay.format(DINH_DANG_NGAY);
    }

    // ================== NHẬP EMAIL ==================

    public static String nhapEmail(String nhanDe) {
        while (true) {
            String email = nhapChuoi(nhanDe);
            if (kiemTraEmail(email)) {
                return email;
            }
            System.out.println("Lỗi: email không hợp lệ (ví dụ ten@truong.edu.vn).");
        }
    }

    public static String nhapEmailTuyChon(String nhanDe, String macDinh) {
        while (true) {
            String email = nhapChuoiTuyChon(nhanDe, macDinh);
            if (kiemTraEmail(email)) {
                return email;
            }
            System.out.println("Lỗi: email không hợp lệ (ví dụ ten@truong.edu.vn).");
        }
    }

    // ================== XÁC NHẬN ==================

    /** Hỏi xác nhận y/n, nhập sai thì hỏi lại. */
    public static boolean xacNhan(String cauHoi) {
        while (true) {
            System.out.print(cauHoi + " (y/n): ");
            String s = docDong().toLowerCase();
            if (s.equals("y")) {
                return true;
            }
            if (s.equals("n")) {
                return false;
            }
            System.out.println("Lỗi: chỉ nhập y (đồng ý) hoặc n (không).");
        }
    }

    public static void dungManHinh() {
        System.out.print("Nhấn Enter để tiếp tục...");
        docDong();
    }

    // ================== TIỆN ÍCH STATIC KHÁC ==================

    /** Bỏ dấu tiếng Việt và đổi sang chữ thường, để tìm "nguyen" ra "Nguyễn". */
    public static String boDau(String s) {
        if (s == null) {
            return "";
        }
        String khongDau = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return khongDau.replace('đ', 'd').replace('Đ', 'D').toLowerCase();
    }

    /** Tìm gần đúng: không phân biệt hoa thường và dấu. */
    public static boolean chuaTuKhoa(String nguon, String tuKhoa) {
        return boDau(nguon).contains(boDau(tuKhoa).trim());
    }

    /** STATIC hỗ trợ sinh mã: lấy phần số của mã, ví dụ ("TS012", "TS") -> 12; sai dạng -> -1. */
    public static int maThanhSo(String ma, String tienTo) {
        if (ma == null || !ma.toUpperCase().startsWith(tienTo)) {
            return -1;
        }
        String phanSo = ma.substring(tienTo.length());
        if (phanSo.isEmpty() || phanSo.length() > 9) {
            return -1;
        }
        for (char c : phanSo.toCharArray()) {
            if (c < '0' || c > '9') {
                return -1;
            }
        }
        return Integer.parseInt(phanSo);
    }

    /** STATIC sinh mã: ("TS", 6) -> "TS006". */
    public static String taoMa(String tienTo, int so) {
        return String.format("%s%03d", tienTo, so);
    }

    public static double lamTron2(double so) {
        return Math.round(so * 100.0) / 100.0;
    }

    /** Rút gọn chuỗi dài khi in bảng. */
    public static String rutGon(String s, int doDaiToiDa) {
        if (s == null || s.length() <= doDaiToiDa) {
            return s;
        }
        return s.substring(0, doDaiToiDa - 3) + "...";
    }
}
