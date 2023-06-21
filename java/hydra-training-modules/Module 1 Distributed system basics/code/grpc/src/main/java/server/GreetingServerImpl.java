package server;

import io.grpc.stub.StreamObserver;
import proto.greeting.GreetingRequest;
import proto.greeting.GreetingResponse;
import proto.greeting.GreetingServiceGrpc;

public class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
        responseObserver.onCompleted();
    }
}
