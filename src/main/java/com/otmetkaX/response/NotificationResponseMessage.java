package com.otmetkaX.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otmetkaX.model.Notification;
import com.otmetkaX.model.Security;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseMessage extends ResponseMessage{

    @JsonProperty("notifications")
    private List<Notification> notificationList;

    @JsonProperty("notification")
    private Notification notification;

    public NotificationResponseMessage(String status, String message, int code, String jwtToken, List<Notification> notificationList, Notification notification) {
        super(status, message, code, jwtToken);
        this.notification = notification;
        this.notificationList = notificationList;
    }


}
