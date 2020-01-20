package com.work_order.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WorkOrderNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(WorkOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String workOrderNotFoundHandler(WorkOrderNotFoundException ex) {
        return ex.getMessage();
    }
}
