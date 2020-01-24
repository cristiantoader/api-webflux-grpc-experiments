package com.ctoader.learn;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
@Slf4j
public class NettyServerConfig {

    @Value("${server.port}")
    private Integer port;

    @Value("${server.ssl.certificate-file-path:}")
    private String certChainFilePath;

    @Value("${server.ssl.private-key-file-path:}")
    private String privateKeyFilePath;

    @Value("${server.ssl.trust-cert-collection-file-path:}")
    private String trustCertCollectionFilePath;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(destroyMethod = "shutdown")
    public Server nettyServer() throws IOException {
        log.info("Creating netty server for port {}.", this.port);

        NettyServerBuilder nettyServerBuilder = NettyServerBuilder.forPort(port);
//                .sslContext(getSslContextBuilder().build());

        // adding grpc services to netty server.
        applicationContext.getBeansOfType(BindableService.class)
                .values()
                .stream()
                .forEach(nettyServerBuilder::addService);

        Server nettyServer = nettyServerBuilder.build();
        nettyServer.start();
        log.info("Netty server successfully started on port {}.", this.port);

        return nettyServer;
    }

    /* note: Not used yet. */
    private SslContextBuilder getSslContextBuilder() {
        SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(new File(certChainFilePath), new File(privateKeyFilePath));

        if (trustCertCollectionFilePath != null) {
            sslClientContextBuilder.trustManager(new File(trustCertCollectionFilePath));
            sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
        }

        return GrpcSslContexts.configure(sslClientContextBuilder);
    }
}
