# ĐỒ ÁN: ỨNG DỤNG QUẢN LÝ THI TRẮC NGHIỆM (Java 17, console)

> Đã biên dịch bằng `javac --release 17` và chạy thử toàn bộ kịch bản demo, kể cả các trường hợp nhập sai, file dữ liệu lỗi và thư mục `data/` chưa tồn tại.
> Tổng cộng **23 lớp** (không tính 3 interface và `Main`), khoảng 3.983 dòng mã.
>
> **Bổ sung:** chức năng **Thống kê - báo cáo** (menu 7, lớp `ThongKe` + `ThongKeDiem`) và bộ dữ liệu mẫu **≥ 50 bản ghi cho mỗi file** (50 môn học, 60 người dùng = 50 thí sinh + 10 giáo viên, 80 câu hỏi, 50 đề thi, 150 kết quả thi).

## 1. Hướng dẫn chạy chương trình

### 1.1. Chuẩn bị

- Cài **JDK 17 trở lên** (ví dụ Eclipse Temurin 17 hoặc Oracle JDK 17/21).
- Kiểm tra đã cài đúng chưa: mở PowerShell gõ hai lệnh sau, cả hai đều phải ra số phiên bản (17 trở lên):

```powershell
java -version
javac -version
```

> **Quan trọng:** luôn chạy chương trình khi đang đứng ở **thư mục gốc của đồ án** (thư mục có chứa `src/`, `data/`, `README.md`). Chương trình đọc dữ liệu theo đường dẫn `data/...`, nếu đứng sai thư mục sẽ thấy báo "Đã đọc: 0 môn học, 0 người dùng..." và dữ liệu trống.

### 1.2. Chạy bằng PowerShell trên Windows (khuyên dùng)

**Bước 1 – Mở PowerShell tại thư mục đồ án.** Cách nhanh: mở thư mục đồ án trong File Explorer, bấm vào thanh địa chỉ, gõ `powershell` rồi Enter. Hoặc dùng lệnh `cd` (đường dẫn có dấu cách thì phải để trong ngoặc kép):

```powershell
cd "C:\Users\Admin\OneDrive - Saigon University\Documents\QuanLyThiTracNghiem"
```

**Bước 2 – Bật tiếng Việt (UTF-8) cho cửa sổ PowerShell.** Mỗi lần mở cửa sổ mới đều phải chạy lại:

```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding  = [System.Text.Encoding]::UTF8
```

**Bước 3 – Biên dịch** (chỉ cần làm lại khi có sửa code). Các file `.class` được tạo trong thư mục `out/`:

```powershell
javac -encoding UTF-8 -d out src\quanly\*.java
```

Biên dịch thành công thì không in ra gì (có thể có 1 dòng `warning`, không sao).

**Bước 4 – Chạy chương trình:**

```powershell
java "-Dfile.encoding=UTF-8" -cp out quanly.Main
```

Màn hình hiện dòng `Đã đọc: 50 môn học, 60 người dùng, 80 câu hỏi, 50 đề thi, 150 kết quả.` và menu chính là chạy đúng. Gõ số tương ứng rồi Enter để chọn chức năng, gõ `0` để thoát.

### 1.3. Chạy bằng Command Prompt (cmd)

```bat
cd /d "C:\Users\Admin\OneDrive - Saigon University\Documents\QuanLyThiTracNghiem"
chcp 65001
javac -encoding UTF-8 -d out src\quanly\*.java
java -Dfile.encoding=UTF-8 -cp out quanly.Main
```

`chcp 65001` chuyển cmd sang UTF-8 để hiện tiếng Việt.

### 1.4. Chạy bằng VS Code hoặc IntelliJ IDEA

- **VS Code:** cài extension *Extension Pack for Java* → `File > Open Folder...` chọn **thư mục gốc của đồ án** → mở `src/quanly/Main.java` → bấm **Run** phía trên hàm `main`. Nếu gõ tiếng Việt có dấu bị lỗi, mở Terminal của VS Code và chạy theo mục 1.2.
- **IntelliJ IDEA:** `File > Open...` chọn thư mục đồ án → chuột phải thư mục `src` → *Mark Directory as > Sources Root* → mở `Main.java` bấm nút ▶. Nếu báo đọc được 0 bản ghi: vào *Run > Edit Configurations...* và đặt **Working directory** là thư mục gốc của đồ án.

### 1.5. macOS / Linux

```bash
cd đường/dẫn/tới/QuanLyThiTracNghiem
javac -encoding UTF-8 -d out src/quanly/*.java
java -cp out quanly.Main
```

### 1.6. Lỗi thường gặp

| Hiện tượng | Nguyên nhân | Cách sửa |
|---|---|---|
| `'javac' is not recognized...` / `javac : The term 'javac' is not recognized` | Chưa cài JDK hoặc chưa thêm vào PATH | Cài JDK 17+, khi cài chọn *Add to PATH* (hoặc thêm `...\jdk-17\bin` vào biến môi trường Path), rồi mở lại PowerShell |
| Chữ tiếng Việt hiện thành `?` hoặc ký tự lạ | Console chưa ở chế độ UTF-8 | Chạy lại Bước 2 (PowerShell) hoặc `chcp 65001` (cmd) trước khi chạy |
| `Đã đọc: 0 môn học, 0 người dùng, ...` | Đang đứng sai thư mục | `cd` về thư mục gốc đồ án (có `data/`) rồi chạy lại |
| `Error: Could not find or load main class quanly.Main` | Chưa biên dịch hoặc sai `-cp` | Chạy lại Bước 3, kiểm tra có thư mục `out\quanly\` |
| `UnsupportedClassVersionError` | Lệnh `java` cũ hơn `javac` (máy có nhiều bản Java) | Kiểm tra `java -version` và `javac -version` phải cùng bản 17+, hoặc biên dịch lại |
| `Cảnh báo: bỏ qua dòng N trong data/...` | Dòng trong file dữ liệu bị sai định dạng | Sửa lại dòng đó (lưu file dạng UTF-8), các dòng khác vẫn đọc bình thường |

### 1.7. Khôi phục dữ liệu mẫu

Mọi thao tác thêm/sửa/xóa/làm bài thi đều **ghi thẳng vào file** trong `data/`. Nên **sao lưu thư mục `data/` trước khi demo**; muốn quay về dữ liệu ban đầu thì chép đè lại thư mục `data/` từ bản sao lưu (hoặc `git checkout -- data` nếu dùng git). Nếu xóa hẳn thư mục `data/`, chương trình tự tạo 5 file rỗng, không báo lỗi.

## 2. Cây thư mục project

```
QuanLyThiTracNghiem/
├── src/
│   └── quanly/
│       ├── IQuanLy.java              (interface)
│       ├── IFileIO.java              (interface)
│       ├── IChamDiem.java            (interface)
│       ├── NhapLieu.java             (1)
│       ├── NguoiDung.java            (2)  abstract
│       ├── ThiSinh.java              (3)
│       ├── GiaoVien.java             (4)
│       ├── MonHoc.java               (5)
│       ├── LuaChon.java              (6)
│       ├── CauHoi.java               (7)  abstract, implements IChamDiem
│       ├── CauHoiCoLuaChon.java      (8)  abstract
│       ├── CauHoiMotDapAn.java       (9)
│       ├── CauHoiNhieuDapAn.java     (10)
│       ├── CauHoiDungSai.java        (11)
│       ├── DeThi.java                (12)
│       ├── KetQuaThi.java            (13)
│       ├── DanhSach.java             (14) abstract, implements IQuanLy<T>, IFileIO
│       ├── DanhSachMonHoc.java       (15)
│       ├── DanhSachNguoiDung.java    (16)
│       ├── DanhSachCauHoi.java       (17)
│       ├── DanhSachDeThi.java        (18)
│       ├── DanhSachKetQua.java       (19)
│       ├── BuoiThi.java              (20)
│       ├── ThongKeDiem.java          (21) bộ cộng dồn số liệu điểm
│       ├── ThongKe.java              (22) thống kê - báo cáo
│       ├── QuanLyThiTracNghiem.java  (23)
│       └── Main.java                 (không tính)
└── data/
    ├── monhoc.txt
    ├── nguoidung.txt
    ├── cauhoi.txt
    ├── dethi.txt
    ├── ketqua.txt
    └── baocao_thongke.txt        (tạo ra khi chọn 7 → 10 Xuất báo cáo)
```

## 3. Bảng đối chiếu yêu cầu

| # | Yêu cầu | Lớp / phương thức đáp ứng | Giải thích ngắn |
|---|---|---|---|
| 1 | ≥ 15 lớp | 23 lớp liệt kê ở mục 2 | Không tính `IQuanLy`, `IFileIO`, `IChamDiem`, `Main`. |
| 2 | Constructor không tham số + có tham số, lớp con gọi `super(...)` | Tất cả 23 lớp, ví dụ `ThiSinh()` → `super()`, `ThiSinh(ma, hoTen, ...)` → `super(ma, hoTen, ngaySinh, email)`; `DanhSachCauHoi()` → `super(THU_MUC_MAC_DINH + "/" + TEN_FILE)`; `NhapLieu()` → `this(sc)` | Constructor không tham số đều gán giá trị mặc định hợp lệ hoặc gọi `this(...)`. |
| 3 | ≥ 2 cây kế thừa có nghĩa nghiệp vụ | (a) `NguoiDung` → `ThiSinh`, `GiaoVien`; (b) `CauHoi` → `CauHoiCoLuaChon` → `CauHoiMotDapAn`, `CauHoiNhieuDapAn`; `CauHoi` → `CauHoiDungSai`; (c) `DanhSach<T>` → 5 lớp danh sách | 3 cây; cây câu hỏi có 3 tầng. |
| 4 | Đa hình | `ArrayList<CauHoi>` trong `BuoiThi.lamBai()` gọi `hienThiKhiThi()`, `laTraLoiHopLe()`, `kiemTraDapAn()`, `chamDiem()`; `ArrayList<NguoiDung>` trong `DanhSachNguoiDung` gọi `hienThi()`, `toFileString()`, `getVaiTro()` | Một danh sách kiểu lớp cha chứa nhiều lớp con; Java chọn phương thức theo đối tượng thực tế lúc chạy. |
| 5 | Lớp mảng đối tượng dùng `ArrayList` | `DanhSach<T>.danhSach` (`ArrayList<T>`) và 5 lớp con `DanhSachMonHoc`, `DanhSachNguoiDung`, `DanhSachCauHoi`, `DanhSachDeThi`, `DanhSachKetQua` | Mỗi thực thể chính có một lớp danh sách. |
| 6 | Đọc/ghi file UTF-8 cho tất cả danh sách, tự đọc khi khởi động, tự ghi sau thêm/sửa/xóa | `DanhSach.docFile()`, `DanhSach.ghiFile()`; `them()`, `sua()`, `xoa()` đều gọi `luu()`; `QuanLyThiTracNghiem` constructor gọi `taiDuLieu()` | File chưa có thì tự tạo; dòng lỗi bị bỏ qua và báo số dòng. |
| 7 | Thuộc tính static + phương thức static | `boDem` (static) và `sinhMa()`, `capNhatBoDem()` trong `ThiSinh`, `GiaoVien`, `MonHoc`, `CauHoi`, `DeThi`, `KetQuaThi`; `Scanner sc` static và toàn bộ `NhapLieu.nhapXxx()`, `kiemTraEmail()`, `kiemTraMa()` | Bộ đếm dùng chung cho mọi đối tượng cùng lớp để sinh mã tự động. |
| 8 | Lớp trừu tượng + phương thức trừu tượng | `NguoiDung` (`getVaiTro`, `hienThi`, `toFileString`), `CauHoi` (`kiemTraDapAn`, `hienThi`, `toFileString`, ...), `CauHoiCoLuaChon`, `DanhSach<T>` (`layMa`, `phanTichDong`, `chuyenThanhDong`, `hienThiPhanTu`, `inTieuDe`) | 4 lớp abstract. |
| 9 | ≥ 2 interface được implement thật | `IQuanLy<T>` và `IFileIO` do `DanhSach<T>` cài đặt; `IChamDiem` do `CauHoi` cài đặt và `CauHoiNhieuDapAn` override | `BuoiThi` dùng biến `IChamDiem boChamDiem = ch;` để chấm điểm. |
| 10 | Đủ 5 chức năng, tìm ≥ 2 tiêu chí | Menu con trong `QuanLyThiTracNghiem`: Môn học (mã, tên); Người dùng (mã, tên, vai trò, email); Câu hỏi (mã, từ khóa, môn, loại, độ khó); Đề thi (mã, tên, môn); Kết quả (mã, thí sinh, đề, khoảng điểm) | Tìm theo tên không phân biệt dấu (`NhapLieu.boDau`). |
| 11 | **Thống kê** (bổ sung) | `ThongKe` (menu 7) dùng `ThongKeDiem` | 10 chức năng: tổng quan, phổ điểm & xếp loại, theo đề thi, theo môn học, bảng xếp hạng thí sinh (top N), theo lớp, theo tháng, ngân hàng câu hỏi (loại/độ khó/môn, câu chưa dùng), chi tiết một thí sinh, xuất toàn bộ báo cáo ra `data/baocao_thongke.txt`. |
| 12 | **Dữ liệu ≥ 50 bản ghi** (bổ sung) | 5 file trong `data/` | 50 môn, 60 người dùng, 80 câu hỏi, 50 đề, 150 kết quả. Điểm trong `ketqua.txt` được chấm bằng chính `chamDiem()` của chương trình nên khớp với số câu đúng. |

**Ràng buộc thêm:** không cho trùng mã (`nhapMaMoi`, `DanhSach.them`); xóa luôn hỏi y/n (`NhapLieu.xacNhan`); xóa môn học hoặc câu hỏi đang được đề thi dùng thì in **CẢNH BÁO** (`xoaMonHoc`, `xoaCauHoi`); không cho nhập ký tự `|` vào dữ liệu.

## 4. Sơ đồ kế thừa

```
«interface» IQuanLy<T>     «interface» IFileIO        «interface» IChamDiem
          \                     /                              |
           \                   /                               |
     «abstract» DanhSach<T> ---                         «abstract» CauHoi
        ├── DanhSachMonHoc        (T = MonHoc)             ├── «abstract» CauHoiCoLuaChon   (chứa ArrayList<LuaChon>)
        ├── DanhSachNguoiDung     (T = NguoiDung)          │      ├── CauHoiMotDapAn
        ├── DanhSachCauHoi        (T = CauHoi)             │      └── CauHoiNhieuDapAn     (override chamDiem: điểm từng phần)
        ├── DanhSachDeThi         (T = DeThi)              └── CauHoiDungSai
        └── DanhSachKetQua        (T = KetQuaThi)

«abstract» NguoiDung
        ├── ThiSinh
        └── GiaoVien

Lớp độc lập: MonHoc, LuaChon, DeThi, KetQuaThi, NhapLieu, BuoiThi, ThongKe, ThongKeDiem, QuanLyThiTracNghiem, Main
Quan hệ chứa: QuanLyThiTracNghiem có 5 DanhSach + 1 BuoiThi + 1 ThongKe; BuoiThi dùng chung 4 DanhSach;
              ThongKe dùng chung 5 DanhSach và tạo nhiều ThongKeDiem (mỗi nhóm đề/môn/lớp/tháng/thí sinh một đối tượng);
              DeThi chứa danh sách mã CauHoi; KetQuaThi tham chiếu mã ThiSinh và mã DeThi.
```

## 5. Dữ liệu mẫu trong `data/`

Mỗi file có **ít nhất 50 bản ghi**, lưu dạng **UTF-8**. Các bản ghi gốc (MH001-003, TS001-005, GV001-002, CH001-015, DT001-002, KQ001-004) được giữ nguyên.

| File | Số bản ghi | Nội dung |
|---|---|---|
| `monhoc.txt` | 50 | MH001-MH050, các môn của ngành CNTT và môn đại cương |
| `nguoidung.txt` | 60 | 50 thí sinh TS001-TS050 thuộc 6 lớp (CNTT01, CNTT02, KTPM01, KTPM02, HTTT01, ATTT01) + 10 giáo viên GV001-GV010 |
| `cauhoi.txt` | 80 | 10 câu cho mỗi môn MH001-MH008; 43 câu một đáp án, 17 câu nhiều đáp án, 20 câu đúng/sai; 32 dễ, 33 trung bình, 15 khó |
| `dethi.txt` | 50 | DT001-DT050: đề 15 phút, giữa kỳ, cuối kỳ, ôn tập, thi thử của 8 môn có câu hỏi |
| `ketqua.txt` | 150 | KQ001-KQ150, thi từ 03/2026 đến 10/2026; mọi thí sinh đều có ít nhất 1 lượt thi |

Kết quả KQ005-KQ150 được tạo bằng cách mô phỏng thí sinh trả lời từng câu rồi chấm bằng chính `chamDiem()` của các lớp câu hỏi (có cả trường hợp bỏ câu và chọn thiếu đáp án ở câu nhiều đáp án được điểm từng phần), nên điểm luôn khớp với số câu đúng và số câu của đề.

### `data/monhoc.txt` (50 dòng, trích 5 dòng đầu)

```text
MONHOC|MH001|Lập trình hướng đối tượng|3
MONHOC|MH002|Cơ sở dữ liệu|3
MONHOC|MH003|Mạng máy tính|2
MONHOC|MH004|Cấu trúc dữ liệu và giải thuật|3
MONHOC|MH005|Hệ điều hành|3
...
```

### `data/nguoidung.txt` (60 dòng, trích 8 dòng đầu)

```text
THISINH|TS001|Nguyễn Văn An|12/03/2005|an.nguyen@sv.edu.vn|CNTT01
THISINH|TS002|Trần Thị Bình|25/07/2005|binh.tran@sv.edu.vn|CNTT01
THISINH|TS003|Lê Hoàng Cường|08/11/2004|cuong.le@sv.edu.vn|CNTT02
THISINH|TS004|Phạm Thu Dung|19/01/2005|dung.pham@sv.edu.vn|CNTT02
THISINH|TS005|Võ Minh Em|30/05/2004|em.vo@sv.edu.vn|KTPM01
THISINH|TS006|Trương Phương Ngân|08/10/2005|ngan.truong@sv.edu.vn|ATTT01
THISINH|TS007|Mai Hữu Bảo|25/10/2005|bao.mai@sv.edu.vn|CNTT01
THISINH|TS008|Lâm Ngọc Ngân|10/02/2005|ngan.lam@sv.edu.vn|CNTT02
...
```

### `data/cauhoi.txt` (80 dòng, trích 5 dòng đầu)

```text
MOT|CH001|Từ khóa nào dùng để kế thừa một lớp trong Java?|MH001|1|B|implements|extends|inherits|super
MOT|CH002|Phạm vi truy cập nào chỉ cho phép truy cập bên trong chính lớp đó?|MH001|1|C|public|protected|private|default
MOT|CH003|Lớp nào là lớp cha của mọi lớp trong Java?|MH001|2|A|Object|Class|System|Main
NHIEU|CH004|Những phát biểu nào đúng về lớp trừu tượng (abstract class)?|MH001|2|AC|Có thể chứa phương thức trừu tượng|Có thể tạo đối tượng trực tiếp bằng new|Có thể có constructor|Phải khai báo bằng từ khóa interface
NHIEU|CH005|Những kỹ thuật nào thể hiện tính đa hình trong Java?|MH001|3|AB|Ghi đè phương thức (override)|Nạp chồng phương thức (overload)|Khai báo biến static|Khai báo hằng final
...
```

### `data/dethi.txt` (50 dòng, trích 5 dòng đầu)

```text
DETHI|DT001|Kiểm tra giữa kỳ Lập trình OOP|MH001|15|CH001,CH002,CH003,CH004,CH005,CH006,CH007
DETHI|DT002|Kiểm tra nhanh Cơ sở dữ liệu|MH002|10|CH008,CH009,CH010,CH011,CH012
DETHI|DT003|15 phút Lập trình OOP - Đề 1|MH001|15|CH002,CH003,CH005,CH007,CH017,CH018
DETHI|DT004|15 phút Cơ sở dữ liệu - Đề 1|MH002|15|CH008,CH009,CH010,CH011,CH020,CH021
DETHI|DT005|15 phút Mạng máy tính - Đề 1|MH003|15|CH013,CH014,CH015,CH024,CH030
...
```

### `data/ketqua.txt` (150 dòng, trích 8 dòng đầu)

```text
KETQUA|KQ001|TS001|DT001|8.57|6|7|20/09/2026
KETQUA|KQ002|TS002|DT001|5.71|4|7|20/09/2026
KETQUA|KQ003|TS003|DT002|10.0|5|5|25/09/2026
KETQUA|KQ004|TS003|DT001|3.57|2|7|15/10/2026
KETQUA|KQ005|TS001|DT025|7.0|7|10|28/03/2026
KETQUA|KQ006|TS002|DT039|7.22|4|6|18/04/2026
KETQUA|KQ007|TS003|DT030|7.5|6|8|25/09/2026
KETQUA|KQ008|TS004|DT029|7.5|6|8|31/07/2026
...
```

## 6. Kịch bản demo

Mỗi bước ghi theo dạng *menu chính → menu con → dữ liệu nhập*. Dòng chữ nghiêng là điều cần chỉ cho giảng viên thấy.

**Bước 0 – Khởi động.** Chạy chương trình theo mục 1.
*Dòng đầu tiên "Đã đọc: 50 môn học, 60 người dùng, 80 câu hỏi, 50 đề thi, 150 kết quả." chứng minh tự đọc file khi khởi động.*

**Bước 1 – Xem**
1. `1` (Môn học) → `1` (Xem) → bảng 50 môn → `0`.
2. `2` (Người dùng) → `1` → bảng 60 người, cột Vai trò hiện "Thí sinh"/"Giáo viên".
   *Một danh sách `ArrayList<NguoiDung>` in ra hai kiểu khác nhau nhờ đa hình `hienThi()`.* → `0`.
3. `3` (Câu hỏi) → `1` → 80 câu, mỗi loại in đáp án theo cách riêng → `0`.

**Bước 2 – Thêm (kèm nhập sai)**
1. `2` → `2` → `1` (Thí sinh) → Enter (nhận mã tự sinh **TS051**) → `Hồ Văn Mới` → nhập sai `31/02/2005` (báo lỗi) → `15/08/2005` → nhập sai `abc@` (báo lỗi) → `moi.ho@sv.edu.vn` → `CNTT03`.
   *Mã TS051 do phương thức static `ThiSinh.sinhMa()` sinh ra.*
2. Thử thêm lần nữa và gõ mã `TS001` → báo "mã đã tồn tại" → chứng minh không trùng mã. → `0`.
3. `3` → `2` → `1` (Một đáp án) → Enter (**CH081**) → `Từ khóa nào dùng để khai báo hằng số trong Java?` → `MH001` → `1` → `4` → `static`, `final`, `const`, `var` → `B` → `0`.

**Bước 3 – Sửa**
`2` → `3` → `TS051` → Enter, Enter, Enter (giữ nguyên họ tên, ngày sinh, email) → `KTPM02` → "Đã sửa ... và ghi file". → `0`.

**Bước 4 – Xóa (kèm cảnh báo)**
1. `1` → `4` → `MH001` → hiện **CẢNH BÁO** môn đang dùng bởi 7 đề thi và 11 câu hỏi → `n` → "Đã hủy xóa". → `0`.
2. `3` → `4` → `CH002` → **CẢNH BÁO** câu hỏi nằm trong 6 đề (DT001, ...) → `y` → câu hỏi bị xóa và tự gỡ khỏi các đề đó. → `0`.

**Bước 5 – Tìm kiếm**
1. `2` → `5` → `2` (theo họ tên) → gõ `nguyen` (không dấu) → ra "Nguyễn Văn An" và giáo viên "Nguyễn Thành Trung". → `0`.
2. `3` → `5` → `4` (theo loại) → `3` (Đúng/Sai) → ra 20 câu Đúng/Sai. → `0`.
3. (Tùy chọn) `5` → `5` → `4` (khoảng điểm) → `8` → `10`.

**Bước 6 – Làm bài thi**
`6` → gõ `GV001` (báo "là giáo viên, không phải thí sinh") → `TS051` → `DT002` → `y` → lần lượt trả lời `C`, `B`, `A,C`, `D`, `A`.
Có thể thử nhập sai (ví dụ `X` ở câu Đúng/Sai) để thấy báo lỗi, hoặc chỉ chọn `A` ở câu nhiều đáp án để thấy **điểm từng phần**.
*Bảng kết quả in Loại / Trả lời / Đáp án / Điểm từng câu; điểm 10.00 và "Đã lưu kết quả KQ151".*

**Bước 7 – Đọc/ghi file**
1. `5` → `1` → thấy dòng **KQ151** vừa thi ở cuối bảng. → `0`.
2. Mở `data/ketqua.txt` bằng Notepad → thấy dòng `KETQUA|KQ151|TS051|DT002|10.0|5|5|...` (đã ghi ngay sau khi thi).
3. Sửa tay một điểm trong file, lưu lại (UTF-8). Về chương trình: `8` → `3` (Đọc lại toàn bộ) → `5` → `1` → thấy điểm đã đổi.
4. Thêm một dòng rác (ví dụ `ABC`) vào file rồi đọc lại → chương trình in "Cảnh báo: bỏ qua dòng ..." và vẫn chạy bình thường.
5. `8` → `1` xem đường dẫn và số bản ghi của 5 file; `0`.

**Bước 8 – Thống kê (chức năng bổ sung)**
1. `7` → `1` (Tổng quan): số lượng từng loại dữ liệu, điểm TB chung, tỉ lệ đạt, đề được thi nhiều nhất, thí sinh giỏi nhất.
2. `2` (Phổ điểm): biểu đồ ngang bằng ký tự `#` cho 4 mức xếp loại và 5 khoảng điểm.
3. `3` (Theo đề thi) và `4` (Theo môn học): mỗi dòng có số lượt, TB, cao nhất, thấp nhất, % đạt, số bài Giỏi/Khá/TB/Yếu.
   *Mỗi dòng là một đối tượng `ThongKeDiem`; `ThongKe.nhomKetQua(...)` gom kết quả theo khóa truyền vào (mã đề, mã môn, lớp, tháng...).*
4. `5` (Xếp hạng) → `10` → top 10 thí sinh theo điểm trung bình.
5. `6` (Theo lớp), `7` (Theo tháng), `8` (Ngân hàng câu hỏi: theo loại, độ khó, môn, 5 câu dùng nhiều nhất, câu chưa vào đề nào).
6. `9` → `TS003` → lịch sử thi theo ngày, điểm TB, mức tiến bộ so với lần đầu, thứ hạng.
7. `10` → "Đã xuất báo cáo thống kê ra file data/baocao_thongke.txt" → mở file bằng Notepad để xem toàn bộ báo cáo.
8. `0` → `0` thoát.

## 7. 17 câu hỏi vấn đáp thường gặp

**1. Đa hình nằm ở đâu trong đồ án?**
Rõ nhất ở `BuoiThi.lamBai()`: `ArrayList<CauHoi> cacCauHoi` chứa lẫn `CauHoiMotDapAn`, `CauHoiNhieuDapAn`, `CauHoiDungSai`. Vòng lặp gọi `ch.hienThiKhiThi()`, `ch.laTraLoiHopLe()`, `ch.kiemTraDapAn()`, `ch.chamDiem()`; Java chọn phiên bản theo đối tượng thực tế lúc chạy (dynamic binding). Tương tự `DanhSachNguoiDung` gọi `nd.hienThi()` cho cả `ThiSinh` lẫn `GiaoVien`.

**2. Vì sao `CauHoi` là abstract?**
Một "câu hỏi chung chung" không biết đáp án dạng gì nên không thể cài đặt `kiemTraDapAn()`. Đánh dấu abstract để cấm `new CauHoi()` và buộc mọi lớp con phải tự cài đặt các phương thức trừu tượng.

**3. Tại sao có thêm lớp `CauHoiCoLuaChon`?**
Câu một đáp án và nhiều đáp án đều có danh sách `LuaChon` và cách in giống nhau. Gom vào lớp cha trung gian để không lặp code; `CauHoiDungSai` không có lựa chọn nên kế thừa thẳng `CauHoi`.

**4. Abstract class khác interface thế nào? Đồ án dùng mỗi loại ở đâu?**
Abstract class có thuộc tính, constructor và code dùng chung, lớp con chỉ được `extends` một lớp. Interface là hợp đồng "biết làm gì", một lớp `implements` được nhiều interface. Ví dụ: `DanhSach<T>` là abstract class (có `ArrayList`, đường dẫn file, code CRUD chung) và cùng lúc `implements IQuanLy<T>, IFileIO`.

**5. Interface `IChamDiem` được dùng thật ở đâu?**
`CauHoi implements IChamDiem` cài đặt `chamDiem()` mặc định (đúng được trọn điểm). `CauHoiNhieuDapAn` override lại để cho điểm từng phần. Trong `BuoiThi.lamBai()` có dòng `IChamDiem boChamDiem = ch; boChamDiem.chamDiem(traLoi, diemMoiCau);`.

**6. Thuộc tính static dùng để làm gì?**
`boDem` trong `ThiSinh`, `CauHoi`, `DeThi`... là bộ đếm dùng chung cho mọi đối tượng cùng lớp, để `sinhMa()` trả về mã kế tiếp (TS006, CH016). `NhapLieu.sc` là Scanner static dùng chung cho cả chương trình.

**7. Vì sao phương thức trong `NhapLieu` đều là static?**
Chúng không phụ thuộc trạng thái của một đối tượng cụ thể nên gọi thẳng `NhapLieu.nhapSoNguyen(...)` mà không cần tạo đối tượng. Dùng chung một Scanner tránh lỗi mất dữ liệu đệm khi tạo nhiều Scanner trên `System.in`.

**8. Sinh mã tự động có bị trùng sau khi tắt mở lại chương trình không?**
Không. Constructor có tham số của mỗi thực thể gọi `capNhatBoDem(ma)`; khi `docFile()` tạo lại đối tượng từ file, bộ đếm được cập nhật bằng số lớn nhất đã có. Ngoài ra `DanhSach.them()` vẫn kiểm tra trùng mã lần cuối.

**9. Làm sao biết một dòng trong file là lớp con nào?**
Trường đầu dòng là ký hiệu loại. `DanhSachCauHoi.phanTichDong()` đọc `MOT` / `NHIEU` / `DUNGSAI` để `new` đúng lớp con; `DanhSachNguoiDung.phanTichDong()` đọc `THISINH` / `GIAOVIEN`. Khi ghi, mỗi đối tượng tự trả về dòng của mình qua `toFileString()` (đa hình).

**10. File dữ liệu bị sai hoặc chưa tồn tại thì sao?**
`DanhSach.docFile()` tự tạo thư mục và file rỗng nếu chưa có. Mỗi dòng được bọc `try/catch`; dòng sai (thiếu trường, sai số, sai ngày, trùng mã) bị bỏ qua kèm "Cảnh báo: bỏ qua dòng N", các dòng còn lại vẫn được đọc. Lỗi `IOException` được bắt và báo bằng tiếng Việt.

**11. Khi nào dữ liệu được ghi xuống file?**
Ngay sau mỗi thao tác thêm/sửa/xóa: `DanhSach.them()`, `sua()`, `xoa()` đều gọi `luu()` → `ghiFile()`. Kết quả thi được ghi khi `BuoiThi` gọi `dsKetQua.them(ketQua)`.

**12. Chương trình chống nhập sai thế nào?**
Mọi lần nhập đi qua `NhapLieu`: bắt `NumberFormatException`, `DateTimeParseException`, kiểm tra khoảng giá trị, email bằng regex, ngày dùng `ResolverStyle.STRICT` (chặn 31/02), cấm ký tự `|`. Sai thì báo lỗi tiếng Việt và cho nhập lại. Menu chính còn có `try/catch` cuối cùng để không bao giờ dừng đột ngột.

**13. Tại sao `DanhSach` dùng generic `<T>`?**
Để viết code thêm/sửa/xóa/tìm/đọc/ghi một lần cho mọi loại danh sách. Phần khác nhau giữa các loại được tách thành phương thức trừu tượng (`layMa`, `phanTichDong`, `chuyenThanhDong`, `hienThiPhanTu`), lớp con chỉ cần cài đặt các phần đó.

**14. Override và overload khác nhau ra sao? Chỉ ví dụ.**
Override: lớp con viết lại phương thức cùng chữ ký của lớp cha, ví dụ `CauHoiNhieuDapAn.chamDiem()` ghi đè `CauHoi.chamDiem()`; được quyết định lúc chạy. Overload: cùng tên khác tham số trong một lớp, ví dụ hai constructor `ThiSinh()` và `ThiSinh(ma, hoTen, ngaySinh, email, lop)`; được quyết định lúc biên dịch.

**15. Tính đóng gói thể hiện ở đâu? Vì sao getter danh sách trả về bản sao?**
Thuộc tính khai báo `private`/`protected`, truy cập qua getter/setter. `DeThi.getDanhSachMaCauHoi()` trả về bản sao và `DanhSach.getTatCa()` trả về danh sách chỉ đọc, để bên ngoài không sửa trực tiếp làm lệch dữ liệu với file; mọi thay đổi phải qua `them/sua/xoa` (có ghi file).

**16. Chức năng thống kê được thiết kế thế nào?**
`ThongKe` không giữ dữ liệu riêng mà dùng chung 5 danh sách với `QuanLyThiTracNghiem` (truyền qua constructor có tham số), nên vừa thi xong hay vừa sửa dữ liệu là thống kê thấy ngay. Lớp `ThongKeDiem` là bộ cộng dồn của một nhóm: mỗi lần `them(diem)` cập nhật số lượt, tổng, cao nhất, thấp nhất, số bài đạt và số bài theo xếp loại; điểm trung bình, tỉ lệ đạt, độ lệch chuẩn được tính từ các giá trị đó. `nhomKetQua(Function<KetQuaThi, String> layKhoa)` gom kết quả vào `LinkedHashMap<String, ThongKeDiem>` theo khóa bất kỳ, nhờ vậy một hàm dùng được cho thống kê theo đề, môn, thí sinh, lớp và tháng.

**17. Xuất báo cáo ra file làm thế nào mà không viết lại code in?**
Các hàm `inXxx()` chỉ in, không hỏi nhập. `xuatBaoCao()` tạm đổi `System.out` sang một `PrintStream` ghi file UTF-8, gọi lần lượt các hàm in, rồi trả `System.out` về màn hình trong khối `finally` (kể cả khi có lỗi). Nhờ vậy nội dung file giống hệt những gì thấy trên màn hình.
