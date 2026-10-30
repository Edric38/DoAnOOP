package quanly;

import java.util.ArrayList;
import java.util.List;

public abstract class DanhSach<T> implements IFileIO {
    protected List<T> danhSach;

    public DanhSach() {
        this.danhSach = new ArrayList<>();
    }

    public void them(T obj) {
        danhSach.add(obj);
    }

    public List<T> getDanhSach() {
        return danhSach;
    }

    public void setDanhSach(List<T> danhSach) {
        this.danhSach = danhSach;
    }

    public void hienThiDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sach hien tai dang trong!");
            return;
        }
        for (T item : danhSach) {
            System.out.println(item);
        }
    }

    @Override
    public abstract void docFile(String tenFile);

    @Override
    public abstract void ghiFile(String tenFile);
}