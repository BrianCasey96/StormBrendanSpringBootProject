package com.work_order.restapi;

import com.fasterxml.jackson.databind.util.JSONPObject;
import net.minidev.json.JSONObject;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

        //check if user already exists
        if (repository.findById(newOrder.getId()) == null){

            EntityModel<WorkOrder> entityModel = assembler.toModel(repository.save(newOrder));
            return ResponseEntity
                    .created(new URI(entityModel.getRequiredLink("self").getHref()))
                    .body(entityModel);

        }

        else{
            return  ResponseEntity.badRequest().body("User with " + newOrder.getId() + " already has a work order in the queue");
        }

    }


    @GetMapping("/work-orders/{id}")
    EntityModel<WorkOrder> one(@PathVariable Long id) {

        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @GetMapping("/work-orders/position/{id}")
    String position(@PathVariable Long id) {

        //check that the work order can be found or else throw exception
        WorkOrder order = repository.findById(id)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));

        List<WorkOrder> orders = customisedWorkOrderRepository.sortByRank();

        Long num = null;

        for (int i=0; i<orders.size(); i++){
            if (orders.get(i) == order){
                num = new Long (i);
            }
        }

        return "In position: " + num.toString();
    }

    @GetMapping("/work-orders/averageWaitTime")
    ResponseEntity averageWaitTime(@RequestBody String date) {

        Long avgWaitTime = customisedWorkOrderRepository.getAverageWaitTime(date);

        if(avgWaitTime != null) {

            return ResponseEntity.ok(avgWaitTime.toString() + " seconds");
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new VndErrors.VndError("Date format not correct", "Use this format: dd-MM-yyyy HH:mm:ss"));
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
    String deleteTopOrder() {

        WorkOrder order = customisedWorkOrderRepository.getTopResult();

        // if(order == null)
        repository.delete(order);

        WorkOrder newTopOrder = customisedWorkOrderRepository.getTopResult();

        return "Deleted order is:  ID: " + order.getId() + " Date: " + order.getDate() + "\nNew top order is " + newTopOrder.getId() + ". Date: " + newTopOrder.getDate()+ "\n";

    }


}


