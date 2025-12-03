package com.smartops.smartserve.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSErrorModelDTO {

    private String validationMessage;

    private Object[] args;

    public SSErrorModelDTO(String validationMessage, Object[] args) {
        this.validationMessage = validationMessage;
        this.args = args;
    }
    
    public SSErrorModelDTO(String validationMessage) {
        this.validationMessage = validationMessage;
        this.args = null;
    }
}
