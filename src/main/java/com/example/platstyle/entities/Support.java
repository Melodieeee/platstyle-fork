package com.example.platstyle.entities;

import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private String category;
    private String subject;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @Column(columnDefinition = "double default 0")
    private boolean status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
    @OneToMany(mappedBy="support",cascade = CascadeType.ALL)
    private List<Support_message> messages;
    public String getOrderNum() {
        if(this.order!=null) {
            return String.format("%05d", this.order.getOid());
        } else {
            return "";
        }
    }

    public String getUserName() {
        return user.getFirstName();
    }

    public String getFormatCreateDate(){
        return  new SimpleDateFormat("yyyy/MM/dd").format(this.createDate);
    }
}
