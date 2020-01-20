package com.work_order.restapi;

import java.util.Date;
import java.util.List;

interface CustomisedWorkOrderRepository {

    List<WorkOrder> sortByRank();

    WorkOrder getTopResult();

    Long getAverageWaitTime(Date date);
}
