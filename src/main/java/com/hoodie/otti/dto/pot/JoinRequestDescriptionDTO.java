package com.hoodie.otti.dto.pot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDescriptionDTO {
    private String joinrequestDescription;

    @JsonCreator
    public JoinRequestDescriptionDTO(@JsonProperty("joinrequestDescription") String joinrequestDescription) {
        this.joinrequestDescription = joinrequestDescription;
    }
}
