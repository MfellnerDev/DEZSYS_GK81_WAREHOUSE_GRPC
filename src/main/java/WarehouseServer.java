import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Server of the Warehouse Service
 *
 * @author Manuel Fellner
 * @version 2024-02-20
 */
public class WarehouseServer {
    private static final int PORT = 50022;

    private Server server;

    public void start() throws IOException  {
        // start the server with the given Port & service implementation
        server = ServerBuilder.forPort(PORT)
                .addService(new WarehouseServiceImpl())
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.awaitTermination();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        WarehouseServer server = new WarehouseServer();
        System.out.println("Warehouse Service is running!");
        server.start();
        server.blockUntilShutdown();
    }
}
