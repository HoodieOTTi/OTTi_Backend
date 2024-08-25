package com.hoodie.otti.model.pot;

import com.hoodie.otti.model.profile.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @ManyToOne
    private Pot pot;

    private Boolean approved;// null이면 대기 중, true이면 승인, false이면 거절
}
