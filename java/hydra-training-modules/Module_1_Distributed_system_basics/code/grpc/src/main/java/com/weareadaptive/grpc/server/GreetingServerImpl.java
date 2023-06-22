package com.weareadaptive.grpc.server;

import io.grpc.stub.StreamObserver;
import proto.greeting.GreetingRequest;
import proto.greeting.GreetingResponse;
import proto.greeting.GreetingServiceGrpc;

public class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase
{

    @Override
    public void greet(final GreetingRequest request, final StreamObserver<GreetingResponse> responseObserver)
    {
        responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
        responseObserver.onCompleted();
    }
}
