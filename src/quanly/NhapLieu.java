package quanly;

import java.util.Scanner;

public class NhapLieu {
    private static final Scanner scanner = new Scanner(System.in);

    public static String nhapString(String thongBao) {
        String input;
        while (true) {
            System.out.print(thongBao);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Loi: Khong duoc de trong! Vui long nhap lai.");
        }
    }

    public static int nhapInt(String thongBao) {
        while (true) {
            try {
                String input = nhapString(thongBao);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap vao mot so nguyen hop le!");
            }
        }
    }

    public static double nhapDouble(String thongBao) {
        while (true) {
            try {
                String input = nhapString(thongBao);
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap vao mot so thuc hop le!");
            }
        }
    }

    public static String nhapEmail(String thongBao) {
        while (true) {
            String email = nhapString(thongBao);
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            }
            System.out.println("Loi: Email khong dung dinh dang! Vui long nhap lai.");
        }
    }
}