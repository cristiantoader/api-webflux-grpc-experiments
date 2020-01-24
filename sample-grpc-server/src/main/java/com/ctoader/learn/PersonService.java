package com.ctoader.learn;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@Slf4j
public class PersonService extends PersonServiceGrpc.PersonServiceImplBase {

    @Override
    public void findPeople(Empty request, StreamObserver<PersonWrapper.Person> responseObserver) {
        log.info("Received request to find people.");

        IntStream.iterate(0, i -> i + 1)
                .limit(100_000_000)
                .mapToObj(PersonUtils::makePerson)
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }
}
