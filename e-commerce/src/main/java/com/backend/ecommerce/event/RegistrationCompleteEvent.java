package com.backend.ecommerce.event;

import com.backend.ecommerce.model.LocalUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private LocalUser user;
    private String applicationUrl;

    public RegistrationCompleteEvent(LocalUser user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
