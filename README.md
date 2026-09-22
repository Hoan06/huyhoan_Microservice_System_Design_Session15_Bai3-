# Choreography Saga với Apache Kafka

Ba service chỉ giao tiếp qua Kafka:

`ConcertBookingService -> concert-events -> SeatAssignmentService -> seat-events -> NotificationService`

`correlationId` được giữ nguyên từ `ConcertBookingEvent` đến `SeatReservedEvent`.

## Chạy thử

1. Cài Java 17+ và Maven 3.9+.
2. Khởi động Kafka: `docker compose up -d`
3. Trong ba cửa sổ terminal riêng, chạy lần lượt:

```powershell
mvn -pl notification-service spring-boot:run
mvn -pl seat-assignment-service spring-boot:run
mvn -pl concert-booking-service spring-boot:run
```

Service booking mặc định publish một sự kiện mẫu khi khởi động. Để tắt, dùng `--app.demo.publish-on-startup=false`.

Sự kiện mẫu có `correlationId` là `CONCERT-2024-999`; log hai service sau sẽ hiển thị các dòng theo đề bài.
