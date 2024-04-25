package com.otmetkaX.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otmetkaX.model.Security;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityResponseMessage extends ResponseMessage {
    @JsonProperty("securities")
    private List<Security> securityList;

    @JsonProperty("security")
    private Security security;
    
    public SecurityResponseMessage(
            String status, String message, int code, List<Security> securityList, Security security, String token) {
        super(status, message, code, token);
        this.security = security;
        this.securityList = securityList;
    }
}
