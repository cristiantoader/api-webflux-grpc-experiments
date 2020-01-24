package com.ctoader.learn.benchmarking;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.ctoader.learn.PersonServiceGrpc;
import com.ctoader.learn.PersonWrapper;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class GrpcBenchmarkingService implements BenchmarkingService {

    private final MetricRegistry metricRegistry;

    @Autowired
    public GrpcBenchmarkingService(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void run() {
        new Thread(() -> runTest()).start();
    }

    private void runTest() {
        ManagedChannel nettyChannel = NettyChannelBuilder.forAddress("localhost", 9111)
                .usePlaintext()
                .build();

        PersonServiceGrpc.PersonServiceBlockingStub personService = PersonServiceGrpc.newBlockingStub(nettyChannel);

        Meter personEntriesPerSecond = metricRegistry.meter("grpc_gpb_person_entries_per_second");

        Iterator<PersonWrapper.Person> people = personService.findPeople(Empty.getDefaultInstance());
        people.forEachRemaining(person -> personEntriesPerSecond.mark());
    }
}
