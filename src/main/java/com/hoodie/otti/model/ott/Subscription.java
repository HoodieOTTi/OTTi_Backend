package com.hoodie.otti.model.ott;

import com.hoodie.otti.model.profile.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Column(length = 40, nullable = false)
    private String name;

    private Integer payment;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private Integer paymentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "USER_PROFILE_ID", nullable = false)
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "OTT_ID", nullable = false)
    private Ott ottId;

    @Builder
    public Subscription(String name, Integer payment, String memo, Integer paymentDate, Date createdDate,
                        Date modifiedDate, UserProfile userProfile, Ott ottId) {
        this.name = name;
        this.payment = payment;
        this.memo = memo;
        this.paymentDate = paymentDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userProfile = userProfile;
        this.ottId = ottId;
    }

    public void update(String name, Integer payment, String memo, Integer paymentDate, Ott ottId) {
        if (!isNullAndBlank(name)) {
            this.name = name;
        }
        if (!isNullAndBlank(payment)) {
            this.payment = payment;
        }
        if (!isNullAndBlank(memo)) {
            this.memo = memo;
        }
        if (!isNullAndBlank(paymentDate)) {
            this.paymentDate = paymentDate;
        }
        if (!isNullAndBlank(ottId)) {
            this.ottId = ottId;
        }
    }

    private <T> boolean isNullAndBlank(T argument) {
        if (argument instanceof String) {
            return argument == null || ((String) argument).trim().isEmpty();
        }

        return argument == null;
    }
}
