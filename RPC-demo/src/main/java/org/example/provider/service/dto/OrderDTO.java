package org.example.provider.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDTO implements Serializable {

    private String orderId;

    private String uuid;

    private Date gmtCreate;

}
