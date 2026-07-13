Sample Spring Boot + gRPC
=========================

Struktur singkat:
- src/main/proto/greeting.proto      -> kontrak gRPC (di-generate otomatis jadi kelas Java saat build)
- server/GreetingGrpcService.java    -> implementasi gRPC server (@GrpcService)
- client/GreetingClient.java         -> gRPC client (@GrpcClient) yang memanggil server di atas
- web/GreetingController.java        -> REST endpoint biasa yang memanggil GreetingClient

Alur request:
Browser/Postman -> GET /api/greet (REST, port 8080)
                -> GreetingClient (gRPC client)
                -> GreetingGrpcService (gRPC server, port 9090)

Lokal (perlu Java 17 & Maven):
cd springboot-grpc-sample
mvn spring-boot:run

Coba endpoint-nya:
curl "http://localhost:8080/api/greet?name=Budi"
=> Hello, Budi!

Jalankan test:
mvn test

Docker:
cd springboot-grpc-sample
docker build -t grpc-sample .
docker run -p 8080:8080 -p 9090:9090 grpc-sample
