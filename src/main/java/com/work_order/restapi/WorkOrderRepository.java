package com.work_order.restapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

//removed customised workorderrepositoy
}
