package com.hoodie.otti.dto.pot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PotMembershipDTO {
    private Long id;
    private Long potId;
    private String potName;
    private Long user;
    private String username;
    private Boolean approved;
    private Boolean hasPermission;
}
