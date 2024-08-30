package com.hoodie.otti.model.pot;

import com.hoodie.otti.model.profile.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PotMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    private Pot pot;

    private boolean approved;

    private boolean hasPermission; // 사용자가 'pot'에 대한 권한을 가지고 있는지 여부
}
