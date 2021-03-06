package com.work_order.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
class CustomisedWorkOrderRepositoryImpl implements CustomisedWorkOrderRepository {

    @Autowired
    DateFormatter dateFormatter;

    @Autowired
    WorkOrderRepository workOrderRepository;



    @Override
    public List<WorkOrder> sortByRank() {

        List<WorkOrder> allOrders = new ArrayList<>();
        HashMap<Long,Long> normalPriorityAndVIP = new HashMap<Long,Long>();
        HashMap<Long,Long> management = new HashMap<Long,Long>();

        List<WorkOrder> list = workOrderRepository.findAll();

        for (WorkOrder l : list){

            Long seconds = dateFormatter.getSecondsSinceCreation(l.getDate());

            switch(l.getType(l.getId())){

                case NORMAL:
                    normalPriorityAndVIP.put(l.getId(), seconds);
                    break;
                case PRIORITY:
                    Long rank = (long) Math.max(3, seconds*(Math.log(seconds)));
                    normalPriorityAndVIP.put(l.getId(), rank);
                    break;
                case VIP:
                    Long rank1 = (long) Math.max(4, (2*seconds)*(Math.log(seconds)));
                    normalPriorityAndVIP.put(l.getId(), rank1);
                    break;
                case MANAGEMENT_OVERRIDE:
                    management.put(l.getId(), seconds);
                    break;
            }

        }


        LinkedHashMap<Long, Long> sortedNormalPriorityAndVIPMap = sortedList(normalPriorityAndVIP);
        LinkedHashMap<Long, Long> sortedManagementMap = sortedList(management);


        for (Map.Entry<Long, Long> entry : sortedManagementMap.entrySet()) {

            WorkOrder order = workOrderRepository.findById(entry.getKey())
                    .orElseThrow(() -> new WorkOrderNotFoundException(entry.getKey()));

            allOrders.add(order);

        }

        for (Map.Entry<Long, Long> entry : sortedNormalPriorityAndVIPMap.entrySet()) {

            WorkOrder order = workOrderRepository.findById(entry.getKey())
                    .orElseThrow(() -> new WorkOrderNotFoundException(entry.getKey()));

            allOrders.add(order);

        }

        return allOrders;

    }

    @Override
    public WorkOrder getTopResult() {

        WorkOrder order = sortByRank().get(0);
        return sortByRank().get(0);

    }

    @Override
    public Long getAverageWaitTime(String date) {
        List<WorkOrder> list = workOrderRepository.findAll();

        Date converted;
        try {
            converted = dateFormatter.stringToDate(date);
            Long seconds = 0L;

            for (WorkOrder l : list) {

                seconds  +=  (converted.getTime()-l.getDate().getTime())/1000;

            }

            return seconds/ list.size();

        }
        catch(Exception dateFromat){

        }

        return  null;

    }


    public LinkedHashMap sortedList(HashMap<Long,Long> hm){
        return  hm.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}

