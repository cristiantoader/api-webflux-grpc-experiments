package com.ctoader.learn;

import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.ctoader.learn.PersonUtils.makePerson;

@RestController
@RequestMapping(value = "/person")
@Slf4j
public class PersonController {

    // knows by default to use ProtobufHttpMessageConverter (content type application/x-protobuf)
    @GetMapping(value = "/gpb")
    public Flux<PersonWrapper.Person> generatePeopleGpbFormat() {
        log.info("Received request to find all people via gpbs.");

        return Flux.range(0, 100_000_000)
                   .map(index -> makePerson(index));
    }

    @GetMapping(value = "/bytes", produces = "application/stream+json")
    public Flux<ByteArrayWrapper> generatePeopleGpbBytesFormat() {
        log.info("Received request to find all people via gpb bytes.");

        return Flux.range(0, 100_000_000)
                   .map(index -> makePerson(index))
                   .map(person -> new ByteArrayWrapper(person.toByteArray()));
    }

    // knows by default to use application/stream+json)
    @GetMapping(value = "/json", produces = "application/stream+json")
    public Flux<StringWrapper> generatePeopleJsonFormat() {
        log.info("Received request to find all people via json string.");

        return Flux.range(0, 100_000_000)
                .map(index -> makePerson(index))
                .map(Unchecked.function(it -> JsonFormat.printer().print(it)))
                .map(StringWrapper::new);
    }


}
