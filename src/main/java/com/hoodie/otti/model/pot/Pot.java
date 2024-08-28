package com.hoodie.otti.model.pot;

import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.profile.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pot_id")
    private Long id;

    @Column(name = "pot_name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "ott_id", nullable = false)
    private Ott ottId;

    @Column(name = "pot_depositAccount")
    private String depositAccount;  // 입금계좌

    @Column(name = "pot_ratePlan")
    private String ratePlan;  // 결제일

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = true)
    private User creatorId;


    @Builder
    public Pot(Long id, String name, Date createdDate, Date modifiedDate, User user, Ott ott, User creator, String depositAccount, String ratePlan) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userId = user;
        this.ottId = ott;
        this.creatorId = creator;
        this.depositAccount = depositAccount;  // 입금계좌
        this.ratePlan = ratePlan;  // 결제일
    }

//    @Builder
//    public Pot(Long id, String name, Date createdDate, Date modifiedDate, Ott ott, String depositAccount, String ratePlan) {
//        this.id = id;
//        this.name = name;
//        this.createdDate = createdDate;
//        this.modifiedDate = modifiedDate;
//        this.ottId = ott;
//        this.depositAccount = depositAccount;  // 입금계좌
//        this.ratePlan = ratePlan;  // 결제일
//    }
}
