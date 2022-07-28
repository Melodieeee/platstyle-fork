package com.example.platstyle.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String firstName;
    private String lastName;
    @NotNull
    private String password;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date registerDate;
    @Column(name="EMAIL", length=50, nullable=false, unique=true)
    private String email;
    @Column(name="GENDER", length=10)
    private String gender;
    private String phone;
    @NotNull
    private String roles;

    @OneToOne(mappedBy = "uid", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Stylist stylist;

    public String getStylistIDdocument() {
        if(this.stylist!=null) {
            return this.stylist.getIdentityDocument();
        } else {
            return "";
        }
    }
    public String getStylistWorkPermit() {
        if(this.stylist!=null) {
            return this.stylist.getWorkPermit();
        } else {
            return "";
        }
    }
    public int getStylistVerify() {
        if(this.stylist!=null) {
            if(this.stylist.isVerify()) return 1;
            else return 2;
        } else {
            return 0;
        }
    }

}