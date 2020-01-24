package com.ctoader.learn;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
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
        runJsonBenchmarking();
        runGpbBenchmarking();
    }

    private void runJsonBenchmarking() {
        Flux<String> personFlux = webClient.get()
                .uri("/person/json")
                .retrieve()
                .bodyToFlux(String.class);

        Meter personEntriesPerSecond = metricRegistry.meter("json_person_entries_per_second");
        Meter personBytesPerSecond = metricRegistry.meter("json_person_bytes_per_second");

        personFlux.subscribe(person -> processJsonPersonFlux(personEntriesPerSecond, personBytesPerSecond, person));
    }

    private void runGpbBenchmarking() {
        Flux<PersonWrapper.Person> personFlux = webClient.get()
                .uri("/person/gpb")
                .retrieve()
                .bodyToFlux(PersonWrapper.Person.class);

        Meter personEntriesPerSecond = metricRegistry.meter("gpb_person_entries_per_second");
        Meter personBytesPerSecond = metricRegistry.meter("gpb_person_bytes_per_second");

        personFlux.subscribe(person -> processGpbPersonFlux(personEntriesPerSecond, personBytesPerSecond, person));
    }

    private void processGpbPersonFlux(Meter personEntriesPerSecond,
                                       Meter personBytesPerSecond,
                                       PersonWrapper.Person person) {
        try {
            personEntriesPerSecond.mark();
            personBytesPerSecond.mark(person.toByteArray().length);

        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void processJsonPersonFlux(Meter personEntriesPerSecond,
                                       Meter personBytesPerSecond,
                                       String person) {
        try {
            personEntriesPerSecond.mark();
            personBytesPerSecond.mark(person.getBytes().length);

        } catch (Exception e) {
            log.error("", e);
        }
    }
}
