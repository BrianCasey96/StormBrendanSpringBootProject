package com.work_order.restapi;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)

@WebMvcTest(WorkOrderController.class)
//@SpringBootTest(classes = StormBrendanApplication.class)
public class WorkOrderControllerTests {

    @Autowired private MockMvc mvc;

    @MockBean private WorkOrderRepository repository;

    @MockBean private WorkOrderResourceAssembler assembler;

    @Autowired @MockBean private CustomisedWorkOrderRepository repo;

    @MockBean private  DateFormatter dateFormatter;

    @MockBean
    private WorkOrderController controller;



    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    public void sortOrdersByRank() throws Exception {


        String sdate1 = "15-01-2020 01:12:12";
        Date date1 = dateFormatter.stringToDate(sdate1);
        Long normal= 11L;

        String sdate2 = "07-01-2020 01:12:12";
        Date date2 = dateFormatter.stringToDate(sdate1);
        Long priority= 9L;

        String sdate3 = "05-01-2020 01:12:12";
        Date date3 = dateFormatter.stringToDate(sdate1);
        Long vip= 10L;

        String sdate4 = "03-01-2020 01:12:12";
        Date date4 = dateFormatter.stringToDate(sdate1);
        Long management_override= 15L;


        List<WorkOrder> allOrders = new ArrayList<>();

        WorkOrder order1 = new WorkOrder(normal, date1);
        WorkOrder order2 = new WorkOrder(priority, date2);
        WorkOrder order3 = new WorkOrder(vip, date3);
        WorkOrder order4 = new WorkOrder(management_override, date4);

        allOrders.add(order1);
        allOrders.add(order2);
        allOrders.add(order3);
        allOrders.add(order4);

        given(repo.sortByRank()).willReturn(allOrders);


//        given(repository.findAll()).willReturn( //
//                LinkedHashMap( //
//                        new WorkOrder(normal, date1), //
//                        new WorkOrder(priority, date2),
//                        new WorkOrder(vip, date3),
//                        new WorkOrder(management_override, date4)
//                        ));

        mvc.perform(get("/work-orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
           //     .andExpect(jsonPath("$[0].id", is(15L)));


        // when
      //  List<WorkOrder> result = repo.sortByRank();

        // then
//        assertThat(result.size()).isEqualTo(2);
//
//        assertThat(result.getEmployeeList().get(0).getFirstName())
//                .isEqualTo(employee1.getFirstName());
//
//        assertThat(result.getEmployeeList().get(1).getFirstName())
//                .isEqualTo(employee2.getFirstName());

    }
}
