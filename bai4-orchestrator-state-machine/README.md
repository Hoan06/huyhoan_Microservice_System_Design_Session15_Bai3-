# Bài tập 4 — Orchestrator Saga với State Machine

Luồng thành công: `INITIATED → PAYMENT_PENDING → PAYMENT_COMPLETED → SEAT_RESERVING → BOOKING_CONFIRMED`.

Orchestrator là nơi duy nhất quản lý trạng thái. Payment và Concert service chỉ thực hiện activity nghiệp vụ; phiên bản minh hoạ gọi chúng qua interface local để chạy demo không cần hạ tầng ngoài. Khi tách deploy thực tế, thay hai adapter này bằng HTTP/gRPC/Kafka command client mà không đổi State Machine.

## Chạy demo

Yêu cầu: Java 17+ và Maven 3.9+.

Mở ba terminal tại thư mục `bai4-orchestrator-state-machine`, chạy theo thứ tự:

```powershell
mvn -pl payment-service spring-boot:run
mvn -pl concert-service spring-boot:run
mvn -pl orchestrator-service spring-boot:run
```

Sẽ in các log chuyển state yêu cầu với booking `CONCERT-2026-088`. Có thể build tất cả module bằng `mvn clean package`.

Xem phân tích tại [REPORT.md](REPORT.md).
