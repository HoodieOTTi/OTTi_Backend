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

    private boolean hasPermission;

    public boolean getApproved() {
        return approved;
    }

    // hasPermission 접근자 추가
    public boolean hasPermission() {
        return hasPermission;
    }
}
