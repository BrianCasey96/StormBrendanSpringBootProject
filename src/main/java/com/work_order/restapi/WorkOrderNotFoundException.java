package com.work_order.restapi;

public class WorkOrderNotFoundException extends RuntimeException {

    WorkOrderNotFoundException(Long id) {
        super("Could not find work order " + id);
    }

}
