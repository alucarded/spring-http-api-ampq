package com.phoneservice.phoneservice;

import com.phoneservice.phoneservice.model.Signup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.phoneservice.phoneservice.SignupServiceApplication.SIGNUP_ROUTING_KEY_BASE;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SignupServiceController {

    static final String ROUTING_KEY = SIGNUP_ROUTING_KEY_BASE + ".poland";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SignupServiceController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/signup")
    public ResponseEntity postPhone(@Valid @RequestBody Signup signup) {
        rabbitTemplate.convertAndSend(SignupServiceApplication.EXCHANGE_NAME,
                ROUTING_KEY,
                signup);
        return ResponseEntity.accepted().build();
    }
}