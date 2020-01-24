package com.ctoader.learn;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ReactiveClientRunner implements ApplicationRunner {

    private final MetricRegistry metricRegistry;
    private final WebClient webClient;

    @Autowired
    public ReactiveClientRunner(MetricRegistry metricRegistry,
                                WebClient webClient) {
        this.metricRegistry = metricRegistry;
        this.webClient = webClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        runGpbBytesBenchmarking();
        runJsonBenchmarking();
        runGpbBenchmarking();
    }

    private void runGpbBytesBenchmarking() {
        Flux<ByteArrayWrapper> personFlux = webClient.get()
                .uri("/person/bytes")
                .retrieve()
                .bodyToFlux(ByteArrayWrapper.class);

        Meter personEntriesPerSecond = metricRegistry.meter("gpb_bytes_person_entries_per_second");

        personFlux.map(Unchecked.function(it -> PersonWrapper.Person.parseFrom(it.getBytes())))
                  .subscribe(it -> personEntriesPerSecond.mark());
    }

    private void runJsonBenchmarking() {
        Flux<String> personFlux = webClient.get()
                .uri("/person/json")
                .retrieve()
                .bodyToFlux(String.class);

        Meter personEntriesPerSecond = metricRegistry.meter("json_person_entries_per_second");
        personFlux.subscribe(person -> personEntriesPerSecond.mark());
    }

    private void runGpbBenchmarking() {
        Flux<PersonWrapper.Person> personFlux = webClient.get()
                .uri("/person/gpb")
                .retrieve()
                .bodyToFlux(PersonWrapper.Person.class);

        Meter personEntriesPerSecond = metricRegistry.meter("gpb_person_entries_per_second");
        personFlux.subscribe(person -> personEntriesPerSecond.mark());
    }

}
