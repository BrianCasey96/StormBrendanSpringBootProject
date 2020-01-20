package com.work_order.restapi;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkOrderResourceAssembler implements RepresentationModelAssembler<WorkOrder, EntityModel<WorkOrder>> {

    @Override
    public EntityModel<WorkOrder> toModel(WorkOrder order) {

      //  WorkOrder e =new WorkOrder();
//        order.setStatus(e.getType(order.getId()));

        System.out.println("waa");

        return new EntityModel<>(order,
                linkTo(methodOn(WorkOrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(WorkOrderController.class).all()).withRel("work-orders"));
    }
}
