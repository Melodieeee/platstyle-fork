package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderDateTime;
    private String note;
    private String address;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    private int rate;
    private double totalPrice;
    private int status;
}
