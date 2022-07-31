package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    // 0 without payment
    // 1 waiting stylist
    /*
    customer 			stylist
1. 				   	  confirm request
2. confirm arrive
3. 				      setPrice
4. confirm Price
5.					  confirm service
6. confirm service
7. complete feedback
8. complete no feedback button
9. cancel
*/

    @OneToMany(mappedBy="order",cascade = CascadeType.PERSIST)
    private List<Order_service> services;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Payment payment;
}
