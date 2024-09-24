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

    @Column(name = "joinrequest_description", length = 1000)
    private String joinrequestDescription;

    private Boolean approved;

    public boolean isApproved() {
        return Boolean.TRUE.equals(approved);
    }
}
