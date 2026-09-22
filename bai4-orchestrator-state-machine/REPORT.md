# Báo cáo phân tích — Orchestrator Saga với State Machine

## 1. Choreography và Orchestration

Trong Choreography, mỗi service tự lắng nghe event và phát event tiếp theo. Cách này đơn giản khi ít bước, nhưng khi thêm ưu đãi, địa điểm, e-ticket và hủy thì quan hệ topic/event phân nhánh khiến khó biết giao dịch đang dừng ở đâu (*event spaghetti*).

Trong Orchestration, State Machine tập trung là nguồn sự thật của trạng thái. Nó phát command đến activity và chỉ đổi state theo kết quả trả về. Payment và Concert Service không biết hoặc không điều phối các bước còn lại. Vì vậy debug theo `bookingId`, thêm bước mới và theo dõi giao dịch đều rõ ràng hơn.

## 2. State và event

| State | Ý nghĩa | Event hợp lệ | State kế tiếp |
|---|---|---|---|
| `INITIATED` | Đã nhận yêu cầu | `PROCESS_PAYMENT` | `PAYMENT_PENDING` |
| `PAYMENT_PENDING` | Chờ thanh toán | `PAYMENT_SUCCESS`, `PAYMENT_FAILED` | `PAYMENT_COMPLETED`, `CANCELLED` |
| `PAYMENT_COMPLETED` | Đã thanh toán | `RESERVE_SEATS` | `SEAT_RESERVING` |
| `SEAT_RESERVING` | Đang giữ ghế | `RESERVATION_SUCCESS`, `RESERVATION_FAILED` | `BOOKING_CONFIRMED`, `CANCELLED` |
| `BOOKING_CONFIRMED` / `CANCELLED` | Kết thúc | — | — |

Mỗi transition được listener ghi log cùng state cũ, event, state mới và `bookingId`.

## 3. Retry và compensation

Payment timeout được retry tối đa 3 lần, cách nhau 2 giây. Lỗi nghiệp vụ (ví dụ thẻ bị từ chối) không retry và chuyển thẳng `CANCELLED`. Nếu reservation thất bại sau thanh toán thành công, State Machine gọi `refund`; nếu payment thất bại thì không hoàn tiền vì chưa có khoản thanh toán được capture. Compensation nằm trong orchestrator, còn thao tác hoàn tiền nằm trong Payment Service.

## 4. Cài đặt và chạy

Cài Java 17+ và Maven, sau đó chạy `mvn -pl orchestrator-service spring-boot:run` từ thư mục dự án. Profile demo dùng request đã cho: `CONCERT-2026-088`, `LIVE-HCM-2026-ULTRA`, `VIP-2024`, `rika@email.com`, 3 vé, 5.500.000 VND. Kết quả dự kiến là `BOOKING_CONFIRMED` cùng các log state transition.
