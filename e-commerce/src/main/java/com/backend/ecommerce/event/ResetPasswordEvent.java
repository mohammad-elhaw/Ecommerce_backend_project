package com.backend.ecommerce.event;

import com.backend.ecommerce.model.LocalUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class ResetPasswordEvent extends ApplicationEvent {

    private LocalUser user;
    private String clientUrl;


    public ResetPasswordEvent(LocalUser user, String clientUrl) {
        super(user);
        this.user = user;
        this.clientUrl = clientUrl;
    }
}
