package com.work_order.restapi;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
class WorkOrder {


    private @Id Long id;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date date;

    WorkOrder() {}

    WorkOrder(Long id, Date date) {
        this.id=id;
        this.date = date;

    }

    public IDType getType(Long id){

            if ((id % 3 == 0) && (id % 5 == 0)) {
                return IDType.MANAGEMENT_OVERRIDE;
            }

            else if (id % 3 == 0) {
                return IDType.PRIORITY;
            }

            else if (id % 5 == 0) {
                return IDType.VIP;
            }

            else{
                return IDType.NORMAL;
            }
    }

}

