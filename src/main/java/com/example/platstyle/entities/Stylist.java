package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stylist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User uid;
    @Column(name="NAME", length=50)
    private String name;
    @Column(name="PHONE", length=20)
    private String phone;
    @Column(name="EMAIL", length=50)
    private String email;
    @Column(name="CITY", length=50)
    private String city;
    @Column(name="IDENTITYDOCUMENT", length=50)
    private String identityDocument;
    @Column(name="WORKPERMIT", length=50)
    private String workPermit;
    private String photo;
    private boolean verify;
    @Column(columnDefinition = "double default 0")
    private double balance;
    @OneToMany(mappedBy="stylist",cascade = CascadeType.ALL)
    private List<Feedback>  feedbacks;

    @OneToMany(mappedBy="stylist",cascade = CascadeType.ALL)
    private List<Order> orderList;
}
