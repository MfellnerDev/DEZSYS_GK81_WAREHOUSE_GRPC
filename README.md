# DEZSYS_GK81_WAREHOUSE_GRPC

This lesson introduces the Remote Procedure Technology (RPC).
Introduction

This exercise is intended to demonstrate the functionality and implementation of Remote Procedure Call (RPC) technology using the Open Source High Performance gRPC Framework gRPC Frameworks (https://grpc.io). It shows that this framework can be used to develop a middleware system for connecting several services developed with different programming languages.

Document all individual implementation steps and any problems that arise in a log (Markdown).
Create a GITHUB repository for this project and add the link to it in the comments.
Assessment

- Group size: 1 Person
- Result by protocol and delivery meeting (in English)
- Requirements **GK überwiegend erfüllt**
  - Answer the questions below about the gRPC framework 
  - Use one of the tutorials listed under "Links & Further Resources" to create a simple HelloWorld application using the gRPC framework 
  - Create first the Proto-file where you define your gRPC Service and its data structures 
  - Implement a gRPC server and gRPC client in a programming language of your choice
  - Document each single development step in your protocol and describe the most important code snippets in few sentences. Furthermore, the output of the application and any problems that occur should be documented in submission document.
- Requirements **GK vollständig erfüllt**
  - Customize the service so that a simple DataWarehouse record can be transferred
  - Document which parts of the program need to be adapted
- Extend Requirements **EK überwiegend erfüllt**
  - Develop the DataWarehouse client in another programming language
  - Document the new DataWarehouse client

## Questions

- What is gRPC and why does it work accross languages and platforms?
- Describe the RPC life cycle starting with the RPC client?
- Describe the workflow of Protocol Buffers?
- What are the benefits of using protocol buffers?
- When is the use of protocol not recommended?
- List 3 different data types that can be used with protocol buffers?

## Links & Further Resources

- https://grpc.io/
- https://grpc.io/docs/languages/java/quickstart/
- https://yidongnan.github.io/grpc-spring-boot-starter/en/
- https://blog.shiftasia.com/introduction-grpc-and-implement-with-spring-boot/
- https://www.baeldung.com/grpc-introduction
- https://intuting.medium.com/implement-grpc-service-using-java-gradle-7a54258b60b8
- Find my HelloWorld source files here: https://elearning.tgm.ac.at/mod/resource/view.php?id=96896
- Youtube: https://www.youtube.com/watch?v=QX2AAY_hAQI 