package com.work_order.restapi;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Slf4j
class LoadDatabase {

    @Autowired
    DateFormatter dateFormatter;

    @Bean
    CommandLineRunner initDatabase(WorkOrderRepository repository) {

        int min=1;
        int max = 10000;

        return args -> {

            for(int i=0; i<20; i++ ) {

                long randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                Date test = dateFormatter.generateRandomDate();

                System.out.println("Preloading " + repository.save(new WorkOrder(randomNum, test)));
            }
        };
    }
}