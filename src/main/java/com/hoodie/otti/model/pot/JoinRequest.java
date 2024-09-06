package com.hoodie.otti.model.pot;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hoodie.otti.model.profile.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@JsonIgnoreProperties({"pot", "requester"})
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    @JsonBackReference
    private User requester;

    @ManyToOne
    @JoinColumn(name = "pot_id", nullable = false)
    private Pot pot;

    private Boolean approved;

    // 승인 여부를 반환하는 메서드 추가
    public boolean isApproved() {
        return Boolean.TRUE.equals(approved);
    }
}
