package com.work_order.restapi;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkOrderControllerTests {

    MockMvc mockMvc;

    @Autowired
    WorkOrderController workOrderController;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    WorkOrderResourceAssembler assembler;

    @MockBean
    CustomisedWorkOrderRepository customisedWorkOrderRepository;

    @MockBean
    WorkOrderRepository repository;

    @Autowired
    DateFormatter dateFormatter;

    private List<WorkOrder> allOrders;
    private EntityModel<WorkOrder> entityModel;
    private WorkOrder order1;

    @Before
    public void setup() throws Exception {
       // this.mockMvc = standaloneSetup(this.customisedWorkOrderRepository).build();// Standalone context
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();

         mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //mockMvc = MockMvcBuilders.standaloneSetup(new WorkOrderController(repository, assembler, customisedWorkOrderRepository)).build();


        String sdate1 = "15-01-2020 01:12:12";
        Date date1 = dateFormatter.stringToDate(sdate1);
        Long normal= 11L;

        String sdate2 = "07-01-2020 01:12:12";
        Date date2 = dateFormatter.stringToDate(sdate2);
        Long priority= 9L;

        String sdate3 = "05-01-2020 01:12:12";
        Date date3 = dateFormatter.stringToDate(sdate3);
        Long vip= 10L;

        String sdate4 = "03-01-2020 01:12:12";
        Date date4 = dateFormatter.stringToDate(sdate4);
        Long management_override= 15L;

        allOrders  = new ArrayList<>();

         order1 = new WorkOrder(normal, date1);
        WorkOrder order2 = new WorkOrder(priority, date2);
        WorkOrder order3 = new WorkOrder(vip, date3);
        WorkOrder order4 = new WorkOrder(management_override, date4);

        allOrders.add(order1);
        allOrders.add(order2);
        allOrders.add(order3);
        allOrders.add(order4);

       entityModel  = new EntityModel<>(order1);

    }


    @Test
    public void testReturnAllEmployeedRanked() throws Exception {
        // Mocking service

        CustomisedWorkOrderRepository mock = org.mockito.Mockito.mock(CustomisedWorkOrderRepository.class);
       // WorkOrderResourceAssembler ass = org.mockito.Mockito.mock(WorkOrderResourceAssembler.class);

        when(mock.sortByRank()).thenReturn(allOrders);
      //  when(ass.toModel(any())).thenReturn(entityModel);

        mockMvc.perform(get("/work-orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

}
