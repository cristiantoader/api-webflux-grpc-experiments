package com.ctoader.learn.config;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public ConsoleReporter consoleReporter(MetricRegistry metricRegistry) {
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.start(1, TimeUnit.SECONDS);
        return consoleReporter;
    }
}
