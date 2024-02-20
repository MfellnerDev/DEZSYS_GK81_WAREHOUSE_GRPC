import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import warehouse.Warehouse;
import warehouse.WarehouseServiceGrpc;

/**
 * Client of the Warehouse service
 *
 * @author Manuel Fellner
 * @version 2024-02-20
 */
public class WarehouseClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50022)
                .usePlaintext()
                .build();

        WarehouseServiceGrpc.WarehouseServiceBlockingStub stub = WarehouseServiceGrpc.newBlockingStub(channel);

        Warehouse.WarehouseResponse warehouseResponse = stub.getWarehouseData(Warehouse.WarehouseRequest.newBuilder()
                .setUuid("469d7240-b974-441d-9562-2c56a7b28767")
                .build());

        System.out.println(warehouseResponse.toString());

        channel.shutdown();
    }
}
