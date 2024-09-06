package com.hoodie.otti.model.pot;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.profile.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
    private String depositAccount;

    @Column(name = "pot_ratePlan")
    private String ratePlan;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = true)
    private User creatorId;

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JoinRequest> joinRequests;

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PotMembership> potMemberships;


    @Builder
    public Pot(Long id, String name, Date createdDate, Date modifiedDate, User user, Ott ott, User creator, String depositAccount, String ratePlan) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userId = user;
        this.ottId = ott;
        this.creatorId = creator;
        this.depositAccount = depositAccount;
        this.ratePlan = ratePlan;
    }
}
