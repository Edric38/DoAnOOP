package quanly;

import java.util.List;

/**
 * Một lựa chọn (phương án) của câu hỏi trắc nghiệm, ví dụ "B. extends".
 * Câu hỏi có lựa chọn CHỨA một danh sách LuaChon (quan hệ has-a / thành phần).
 */
public class LuaChon {

    private char kyHieu;
    private String noiDung;

    public LuaChon() {
        this('A', "");
    }

    public LuaChon(char kyHieu, String noiDung) {
        this.kyHieu = Character.toUpperCase(kyHieu);
        this.noiDung = noiDung;
    }

    public char getKyHieu() {
        return kyHieu;
    }

    public void setKyHieu(char kyHieu) {
        this.kyHieu = Character.toUpperCase(kyHieu);
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void hienThi() {
        System.out.println("      " + kyHieu + ". " + noiDung);
    }

    /** STATIC: kiểm tra ký hiệu có nằm trong danh sách lựa chọn không. */
    public static boolean coKyHieu(List<LuaChon> danhSach, char kyHieu) {
        char k = Character.toUpperCase(kyHieu);
        for (LuaChon lc : danhSach) {
            if (lc.getKyHieu() == k) {
                return true;
            }
        }
        return false;
    }
}
