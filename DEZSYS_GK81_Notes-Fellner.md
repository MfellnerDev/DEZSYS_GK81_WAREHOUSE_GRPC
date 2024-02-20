***

**Author**: Manuel Fellner
**Version**: 20.02.2024

# 1. Tasks for GKü requirements

So, as stated in the Task description, we would need to setup the `HelloWorld` Server & Client using a programming language of our choice. After ~4 years of constant exposure to Java, I think that I'll just stick with this option for now.

***
## 1.1 Answers to the questions in the task description

**1. What is gRPC and why does it work across languages and platforms?**

gRPC is an open-source, high-performance Remote Procedure Call (RPC) framework developed by Google. It enables client and server applications to communicate remotely and transparently regardless of their programming languages or platforms. 

- gRPC uses **Protocol Buffers (Protobuf)** as its interface definition language (IDL). Protobuf allows the user to create the structure of services/data and to generate code from it for different programming languages.
- Using the defined Protobuf IDL, gRPC generates client and server code for various languages (e.g., C++, Java, Python, Go). This generated code handles serialization/deserialization of data using the predefined format, enabling seamless communication.

**2. Describe the RPC life cycle starting with the RPC client?**

1. **Client Initialization:** The RPC client creates a stub object representing the remote service. This stub is generated from the Protobuf IDL.
2. **Method Invocation:** The client invokes a method on the stub object, passing relevant data as arguments. This data is translated from the client's native format to Protobuf format by the generated code.
3. **Serialization and Sending:** The serialized data is sent to the server.
4. **Server-Side Processing:** The server receives the message, deserializes it using the generated code and calls the corresponding implementation of the invoked method.
5. **Response Processing:** The server generates a response and sends it back to the client after serialization.
6. **Client-Side Deserialization and Data Use:** The client receives the response, deserializes it, and uses the data.

e.g. (we have a Go client there!):
![](https://uploads.mfellner.com/oJg92NEjR6mh.webp)



**3. Describe the workflow of Protocol Buffers?**

1. **Definition:** You define data structures (messages) and services (RPC interfaces) using a `.proto` file. This file specifies field names, types, and service methods with request and response messages.
2. **Compilation:** You compile the `.proto` file using the Protobuf compiler (`protoc`). This will generate the needed files.
3. **Client and Server Integration:** The generated code is integrated into your client and server applications. It handles serialization/deserialization of data based on the defined message formats.
4. **RPC Communication:** Client and server use the generated code to communicate with messages being serialized and deserialized.


**4. What are the benefits of using Protocol Buffers?**

- **Language and Platform Independence:** Enables cross-language and cross-platform communication.
- **Compactness and Efficiency:** Serialized Protobuf messages are often smaller than alternative formats like JSON or XML, leading to faster data transfer and improved performance.
- **Automatic Code Generation:** Simplifies development by generating boilerplate code for serialization/deserialization.

**5. When is the use of Protocol Buffers not recommended?**

- **Simple Data Structures:** For very simple data without complex structures, other formats like JSON might be easier to handle.
- **Frequent Schema Changes:** If your data schema changes frequently, managing versioning and compatibility might become bothersome.


**6. List 3 different data types that can be used with Protocol Buffers:**

1. **Scalar Types:** Basic data types like integers, floats, strings, booleans, bytes.
2. **Composite Types:** Messages that group other data types together to represent complex structures.
3. **Enumeration Types:** Define sets of named constants to represent possible values for a field.

***

Tutorial followed: https://intuting.medium.com/implement-grpc-service-using-java-gradle-7a54258b60b8 (really old, wouldn't recommend)
## 1.2 Create our hello.proto file


To have a good data structure for our `HelloWorld` Service, we'll use the following proto file:

```proto
syntax = "proto3";  
  
service HelloWorldService {  
  rpc hello(HelloRequest) returns (HelloResponse) {}  
}  
  
message HelloRequest {  
  string firstname = 1;  
  string lastname = 2;  
  
}  
  
message HelloResponse {  
  string text = 1;  
}
```
*(main/proto/hello.proto)*

It's relatively easy to read, the specs are:
- We have a `HelloWorldService` Service. With this service, we can send `HelloRequests` objects to the Server, which the Server responds with a `HelloResponse` object.
- `HelloRequest` message Object:
	- `firstname : string`: firstname, positioned first in the request (`1`)
	- `lastname : string`: lastname, positioned second in the request (`2`)
- `HelloResponse`: message Object:
	- `text : string`: response text, positioned first in the response (`1`)

## 1.3 Generate the code from the proto file

After defining the proto file, we can switch to the process of generating the code for our Services Server-Code.

**! Attention !**
The `gradle.build` file from the Tutorial above is NOT up to date and uses deprecated Gradle features.

I found the following newer file from the official  [grpc Github Account](https://github.com/grpc/grpc-java/blob/master/examples/build.gradle):

```gradle
plugins {  
    // Provide convenience executables for trying out the examples.  
    id 'application'  
    id 'com.google.protobuf' version '0.9.4'  
    // Generate IntelliJ IDEA's .idea & .iml project files  
    id 'idea'  
}  
  
repositories {  
    maven { // The google mirror is less flaky than mavenCentral()  
        url "https://maven-central.storage-download.googleapis.com/maven2/" }  
    mavenCentral()  
    mavenLocal()  
}  
  
java {  
    sourceCompatibility = JavaVersion.VERSION_1_8  
    targetCompatibility = JavaVersion.VERSION_1_8  
}  
  
// IMPORTANT: You probably want the non-SNAPSHOT version of gRPC. Make sure you  
// are looking at a tagged version of the example and not "master"!  
  
// Feel free to delete the comment at the next line. It is just for safely  
// updating the version in our release process.  
def grpcVersion = '1.61.1'  
def protobufVersion = '3.25.1'  
def protocVersion = protobufVersion  
  
dependencies {  
    implementation "io.grpc:grpc-protobuf:${grpcVersion}"  
    implementation "io.grpc:grpc-services:${grpcVersion}"  
    implementation "io.grpc:grpc-stub:${grpcVersion}"  
    compileOnly "org.apache.tomcat:annotations-api:6.0.53"  
  
    // examples/advanced need this for JsonFormat  
    implementation "com.google.protobuf:protobuf-java-util:${protobufVersion}"  
  
    runtimeOnly "io.grpc:grpc-netty-shaded:${grpcVersion}"  
  
    testImplementation "io.grpc:grpc-testing:${grpcVersion}"  
    testImplementation "io.grpc:grpc-inprocess:${grpcVersion}"  
    testImplementation "junit:junit:4.13.2"  
    testImplementation "org.mockito:mockito-core:4.4.0"  
}  
  
protobuf {  
    protoc { artifact = "com.google.protobuf:protoc:${protocVersion}" }  
    plugins {  
        grpc { artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}" }  
    }    generateProtoTasks {  
        all()*.plugins { grpc {} }  
    }}  
  
  
  
// Note: for IntelliJ IDE to mark the generated files as source.  
sourceSets {  
    src {  
        main {  
            java {  
                srcDirs 'build/generated/source/proto/main/grpc'  
                srcDirs 'build/generated/source/proto/main/java'  
            }  
        }    }}
```
*(gradle.build)*

As you can see; compared to the original, I removed the unnecessary parts of the script.

Now, refresh the build (in IntelliJ: click the button on the top right corner of the file) and build it:

![](https://uploads.mfellner.com/1JjLmB19nmRK.png)

```shell
$ ./gradlew build
``` 

You should get some output like this:

![](https://uploads.mfellner.com/nhTGPSO0vEHA.png)

## 1.4 Verify code generation & start service

To verify that the generation was successful, go to the following directory:
`build/generated/source/proto/main`

Now, if you type `ls`, you should see the following two files:

![](https://uploads.mfellner.com/PgLyr0i1uhX1.png)

Seeing them means that the code generation likely was successful. :)

Now, start the Server & Client!
- Go to `HelloWorldServer`
- Press SHIFT + F10 (or click on the START button)
- Go to `HelloWorldClient`
- Press SHIFT + F10 (or click on the START button)

Now, if you look into the Console of the server, you should see the following:

![](https://uploads.mfellner.com/W3YZ1dX53MZS.png)


## 1.5 How the server & client work

So, now we have the Server & Client running, but how does this implementation work?

1. `HelloWorldServiceImpl.java`:
![](https://uploads.mfellner.com/NrIBGUVYeVRo.png)
- After the shown steps, it just continues to process the method with the logic that our usse case has.


2. `HelloWorldServer.java`:

![](https://uploads.mfellner.com/zoCkUcQkfpLk.png)

3. `HelloWrodlClient.java`:
![](https://uploads.mfellner.com/0MZZ5OvD0NlP.png)




# 2. Tasks for GKv requirements

The requirements for a GKv are, that we implement the exchange of something like the Data of a warehouse over this Implementation.

## 2.1 Writing a protocol buffer file

Because we want to send and receive warehouse data, we need a new protocol buffer file that suits our use case-

We can take the following json document as an example:


```json
{
    "warehouseID": "469d7240-b974-441d-9562-2c56a7b28767",
    "warehouseName": "Linz Bahnhof",
    "warehouseAddress": "WhoKnows Straße 12",
    "warehousePostalCode": 4000,
    "warehouseCity": "Linz",
    "warehouseCountry": "Austria",
    "timestamp": "2023-09-19 16:36:56.933",
    "productData": [
        {
            "productID": "e940f2af-182d-4b49-940b-723908f53a77",
            "productName": "Brot",
            "productCategory": "Brot und Backwaren",
            "productQuantity": 105,
            "productUnit": "500ML/Packung"
        },
        {
            "productID": "df2a7d5e-97f9-4545-91d5-6229ba0acb05",
            "productName": "Milch",
            "productCategory": "Milchprodukte",
            "productQuantity": 167,
            "productUnit": "1L/Packung"
        },
        {
            "productID": "42c9feae-ee65-4e4b-ac45-c1bc5ac7355b",
            "productName": "Kartoffeln",
            "productCategory": "Gemüse",
            "productQuantity": 123,
            "productUnit": "3KG/Packung"
        },
        {
            "productID": "4c54e706-95bf-4602-b4a1-c152cd73eda4",
            "productName": "Reis",
            "productCategory": "Getreide und Reis",
            "productQuantity": 92,
            "productUnit": "500ML/Packung"
        }
    ]
}
```

So, after inspecting the required data structure, I came to the following proto file:

```proto
syntax = "proto3";  
  
package warehouse;  
  
service WarehouseService  {  
  rpc getWarehouseData(WarehouseRequest) returns (WarehouseResponse) {}  
}  
  
message WarehouseRequest  {  
  string uuid = 1;  
}  
  
message WarehouseResponse {  
  string warehouse_id = 1;  
  string warehouse_name = 2;  
  string warehouse_address = 3;  
  int32 warehouse_postal_code = 4;  
  string warehouse_city = 5;  
  string warehouse_country = 6;  
  string timestamp = 7;  
  
  // -> this field type can be repeated zero or more times in a well-formed message. The order of the repeated values will be preserved.  
  repeated Product product_data = 8;  
}  
  
message Product {  
  string product_id = 1;  
  string product_name = 2;  
  string product_category = 3;  
  int32 product_quantity = 4;  
  string product_unit = 5;  
}
```

- `WarehouseService`: A service that has a `getWarehouseData` Method with the `WarehouseRequest` parameter. It returns `WarehouseResponse`
- `WarehouseRequest`: The client just needs to send the uuid of the warehouse he wants to request the data from
- `WarehouseResponse`: More detailed response structure, including the warehouse_id - the timestamp.
	- The `repeated Product product_data = 8;` line is basically just some decorator that declares that this can be repeated more times. Perfect for our use case with more products.

Now, after writing this file, we can generate the source code with the `./gradlew build` command:

![](https://uploads.mfellner.com/F5mrXc5rcPNL.png)

![](https://uploads.mfellner.com/GVnayQicLjYy.png)

## 2.2 Implementing the Service

Now we need to create a `WarehouseServiceImpl.java` file which will include the actual implementation of our Service.

The service needs to include the following components:

- Overwritten `getWarehouseData` method with the correct parameters (see `warehouse.proto`)
- Get the warehouse uuid from the request
- create dummy products
- build warehouse response
- send response to client

```java
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
```

What we did was:

1. Override the `getWarehouseData()` Method that we declared in the `warehouse.proto` file with the correct Request and Response parameters
2. Create a new dummy product objects with the `Product.newBuilder` 
3. Create a new WarehouseResponse object with the `WarehouseResponse.newBuilder()`. Here we include the Warehouse details
4. After that, we just send the response to the client

With this, our service is finished.

## 2.3 Implementing the Server

After implementing the Service, we need to implement the Server.

It contains three key methods:

1. `start()`: Takes the `WarehouseServiceImpl()` Class and builds a server instance with it
2. `blockUntilShutdown()`: Shutdown the server if `server` is null, else, wait for termination
3. `main()`: Starts the server

It looks like this:

```java
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
```


## 2.4 Implementing the Client

Now comes the easiest part: Implementing the Client that just sends a warehouse UUID to the Server.

```java
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
```

The Client includes the following elements:
- Creates a `ManagedChannel` object to connect to the gRPC server running on `localhost` port `500022`
- The `usePlaintext()` indicates that we will use insecure communication (don't use in production!)
- Creates a `WarehouseServiceBlockingStub` object from the connected channel
- stub provides methods for sending gRPC requests
- Sending request
- Processing response

> - What is a stub?
> In our case: A stub is a client-side object that represents a remote service. It provides an interface for clients to interact with the service by calling methods defined in the service's proto file.

## 2.5 Verify the functionality

Now, after a lot of programming, it's time to view the result.

We'll just start the `WarehouseServer` and `WarehouseClient`:

Server:

![](https://uploads.mfellner.com/bQfVz77rWXYe.png)


Client:

![](https://uploads.mfellner.com/PwXZTuevP192.png)

Well, there we have it! 
In the Client console, we get the following Output:

```text
warehouse_id: "469d7240-b974-441d-9562-2c56a7b28767"
warehouse_name: "Linz Bahnhof"
warehouse_address: "WhoKnows Stra\303\237e 12"
warehouse_postal_code: 4000
warehouse_city: "Linz"
warehouse_country: "AUSTRIA"
timestamp: "2024-02-20T16:14:25.452826369"
product_data {
  product_id: "e940f2af-182d-4b49-940b-723908f53a77"
  product_name: "Brot"
  product_category: "Brot und Backwaren"
  product_quantity: 105
  product_unit: "500ML/Packung"
}
product_data {
  product_id: "df2a7d5e-97f9-4545-91d5-6229ba0acb05"
  product_name: "Milch"
  product_category: "Milchprodukte"
  product_quantity: 167
  product_unit: "1L/Packung"
}
product_data {
  product_id: "42c9feae-ee65-4e4b-ac45-c1bc5ac7355b"
  product_name: "Kartoffeln"
  product_category: "Gem\303\274se"
  product_quantity: 123
  product_unit: "3KG/Packung"
}
```


# 3. Sources


- https://intuting.medium.com/implement-grpc-service-using-java-gradle-7a54258b60b8
- https://protobuf.dev/programming-guides/proto3/
- https://github.com/grpc/grpc-java/blob/master/examples/build.gradle
- https://medium.com/@sourabh1024/grpc-for-dummies-9569193ad3e5
- https://grpc.io/
- https://grpc.io/docs/languages/java/quickstart/
- https://www.baeldung.com/google-protocol-buffer
- https://protobuf.dev/overview/

