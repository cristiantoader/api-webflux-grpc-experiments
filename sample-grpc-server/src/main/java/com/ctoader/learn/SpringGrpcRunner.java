package com.ctoader.learn;

import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringGrpcRunner implements ApplicationRunner {

    private final Server nettyServer;

    @Autowired
    public SpringGrpcRunner(Server nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            SpringGrpcRunner.this.nettyServer.shutdown();
            System.err.println("Server shut down");
        }));

        nettyServer.awaitTermination();
    }
}
