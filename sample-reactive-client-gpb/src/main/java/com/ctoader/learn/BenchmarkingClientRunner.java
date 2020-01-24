package com.ctoader.learn;

import com.ctoader.learn.benchmarking.BenchmarkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BenchmarkingClientRunner implements ApplicationRunner {

    private final ApplicationContext applicationContext;

    @Autowired
    public BenchmarkingClientRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        applicationContext.getBeansOfType(BenchmarkingService.class).values()
                          .forEach(BenchmarkingService::run);
    }


}
