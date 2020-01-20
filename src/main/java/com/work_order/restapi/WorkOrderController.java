package com.work_order.restapi;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.web.bind.annotation.*;


@RestController
public class WorkOrderController {

    private final WorkOrderRepository repository;

    private final WorkOrderResourceAssembler assembler;

    private  final CustomisedWorkOrderRepository customisedWorkOrderRepository;

    WorkOrderController (WorkOrderRepository repository, WorkOrderResourceAssembler assembler, CustomisedWorkOrderRepository customisedWorkOrderRepository ) {
        this.repository = repository;
        this.assembler = assembler;
        this.customisedWorkOrderRepository = customisedWorkOrderRepository;
    }


    @GetMapping("/work-orders")
    CollectionModel<EntityModel<WorkOrder>> all() {

        List<EntityModel<WorkOrder>> orders = customisedWorkOrderRepository.sortByRank().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(orders);
    }

    @PostMapping("/work-orders")
    ResponseEntity<?> newWorkOrder(@RequestBody WorkOrder newOrder) throws URISyntaxException {


        //  newOrder.setStatus(e.getType(newOrder.getId()));

        EntityModel<WorkOrder> entityModel = assembler.toModel(repository.save(newOrder));

        return ResponseEntity
                .created(new URI(entityModel.getRequiredLink("self").getHref()))
                .body(entityModel);
    }


    @GetMapping("/work-orders/{id}")
    EntityModel<WorkOrder> one(@PathVariable Long id) {

        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @GetMapping("/work-orders/position/{id}")
    String position(@PathVariable Long id) {

        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));

        List<WorkOrder> orders = customisedWorkOrderRepository.sortByRank();

        Long num = null;

        for (int i=0; i<orders.size(); i++){
            if (orders.get(i) == order){
                 num = new Long (i);
            }
        }

        return num.toString() + " seconds";
    }

    @GetMapping("/work-orders/averageWaitTime")
    String averageWaitTime(@PathVariable Date date) {


        Long avgWaitTime = customisedWorkOrderRepository.getAverageWaitTime(date);


        return avgWaitTime.toString();
    }



    @DeleteMapping("/work-orders/delete/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id) {

        //repository.deleteById(id);
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));

        repository.delete(order);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/work-orders/delete/top")
    ResponseEntity<WorkOrder> deleteTopOrder() {

        WorkOrder order = customisedWorkOrderRepository.getTopResult();

       // if(order == null)
        repository.delete(order);

        return ResponseEntity.noContent().build();
    }


}


