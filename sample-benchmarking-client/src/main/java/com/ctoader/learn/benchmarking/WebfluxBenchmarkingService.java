package com.ctoader.learn.benchmarking;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.ctoader.learn.ByteArrayWrapper;
import com.ctoader.learn.PersonWrapper;
import com.ctoader.learn.StringWrapper;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class WebfluxBenchmarkingService implements BenchmarkingService {

    private final MetricRegistry metricRegistry;
    private final WebClient webClient;

    private static final String GPB_BYTES_PERSON_ENTRIES_PER_SECOND = "webflux_gpb_bytes_person_entries_per_second";
    private static final String JSON_PERSON_ENTRIES_PER_SECOND = "webflux_json_person_entries_per_second";
    private static final String GPB_PERSON_ENTRIES_PER_SECOND = "webflux_gpb_person_entries_per_second";

    @Autowired
    public WebfluxBenchmarkingService(MetricRegistry metricRegistry,
                                      WebClient webClient) {
        this.metricRegistry = metricRegistry;
        this.webClient = webClient;
    }

    @Override
    public void run() {
        runGpbBytesBenchmarking();
        runJsonBenchmarking();
        runGpbBenchmarking();
    }

    private void runGpbBytesBenchmarking() {
        Flux<ByteArrayWrapper> personFlux = webClient.get()
                .uri("/person/bytes")
                .retrieve()
                .bodyToFlux(ByteArrayWrapper.class);

        Meter personEntriesPerSecond = metricRegistry.meter(GPB_BYTES_PERSON_ENTRIES_PER_SECOND);

        personFlux.map(Unchecked.function(it -> PersonWrapper.Person.parseFrom(it.getBytes())))
                .subscribe(it -> personEntriesPerSecond.mark());
    }

    private void runJsonBenchmarking() {
        Flux<StringWrapper> personFlux = webClient.get()
                .uri("/person/json")
                .retrieve()
                .bodyToFlux(StringWrapper.class);

        Meter personEntriesPerSecond = metricRegistry.meter(JSON_PERSON_ENTRIES_PER_SECOND);
        personFlux.subscribe(person -> personEntriesPerSecond.mark());
    }

    private void runGpbBenchmarking() {
        Flux<PersonWrapper.Person> personFlux = webClient.get()
                .uri("/person/gpb")
                .retrieve()
                .bodyToFlux(PersonWrapper.Person.class);

        Meter personEntriesPerSecond = metricRegistry.meter(GPB_PERSON_ENTRIES_PER_SECOND);
        personFlux.subscribe(person -> personEntriesPerSecond.mark());
    }
}
