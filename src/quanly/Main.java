package quanly;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Điểm khởi động chương trình.
 */
public class Main {

    public static void main(String[] args) {
        // In tiếng Việt UTF-8 ra console (Windows cần chạy "chcp 65001" trước)
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        QuanLyThiTracNghiem chuongTrinh = new QuanLyThiTracNghiem();
        chuongTrinh.chay();
    }
}
