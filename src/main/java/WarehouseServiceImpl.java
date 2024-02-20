import com.google.common.collect.ImmutableList;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;

import warehouse.Warehouse;
import warehouse.WarehouseServiceGrpc;

/**
 * Implementation of the Warehouse grpc Service
 *
 * @author Manuel Fellner
 * @version 2024-02-20
 */

public class WarehouseServiceImpl extends WarehouseServiceGrpc.WarehouseServiceImplBase {
    @Override
    public void getWarehouseData(Warehouse.WarehouseRequest request, StreamObserver<Warehouse.WarehouseResponse> responseObserver) {
        System.out.println("Handling warehouse endpoint" + request.toString());

        String warehouseUUID = request.getUuid();

        System.out.println("Getting data of warehouse with uuid=" + warehouseUUID + "...");

        // create a few dummy product objects

        Warehouse.Product product1 = Warehouse.Product.newBuilder()
                .setProductId("e940f2af-182d-4b49-940b-723908f53a77")
                .setProductName("Brot")
                .setProductCategory("Brot und Backwaren")
                .setProductQuantity(105)
                .setProductUnit("500ML/Packung")
                .build();
        Warehouse.Product product2 = Warehouse.Product.newBuilder()
                .setProductId("df2a7d5e-97f9-4545-91d5-6229ba0acb05")
                .setProductName("Milch")
                .setProductCategory("Milchprodukte")
                .setProductQuantity(167)
                .setProductUnit("1L/Packung")
                .build();
        Warehouse.Product product3 = Warehouse.Product.newBuilder()
                .setProductId("42c9feae-ee65-4e4b-ac45-c1bc5ac7355b")
                .setProductName("Kartoffeln")
                .setProductCategory("Gemüse")
                .setProductQuantity(123)
                .setProductUnit("3KG/Packung")
                .build();


        // now create the warehouse response object
        Warehouse.WarehouseResponse response = Warehouse.WarehouseResponse.newBuilder()
                .setWarehouseId(warehouseUUID)
                .setWarehouseName("Linz Bahnhof")
                .setWarehouseAddress("WhoKnows Straße 12")
                .setWarehousePostalCode(4000)
                .setWarehouseCity("Linz")
                .setWarehouseCountry("AUSTRIA")
                .setTimestamp(LocalDateTime.now().toString())
                .addAllProductData(ImmutableList.of(product1, product2, product3))
                .build();

        // send the response to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
