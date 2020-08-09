package com.phoneservice.phoneservice;

import com.phoneservice.phoneservice.model.Signup;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupServiceController.class)
class SignupServiceControllerTest {

    private static final String SIGNUP_PATH = "/api/v1/signup";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void testOkResponse() throws Exception {
        mvc.perform(post(SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fullName\": \"John Doe\", \"phone\": \"John Doe\" }"))
                .andExpect(status().isAccepted());
        verify(rabbitTemplate).convertAndSend(eq(SignupServiceApplication.EXCHANGE_NAME), eq(SignupServiceController.ROUTING_KEY), any(Signup.class));
    }

    @Test
    void whenFieldMissing_thenRespondWithBadRequest() throws Exception {
        mvc.perform(post(SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fullName\": \"John Doe\" }"))
                .andExpect(status().isBadRequest())
                // Plain response
                .andExpect(content().string(""));
        mvc.perform(post(SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fullName\": \"John Doe\", \"phone\": null }"))
                .andExpect(status().isBadRequest())
                // Plain response
                .andExpect(content().string(""));
        verify(rabbitTemplate, Mockito.never()).convertAndSend(anyString(), anyString(), any(Signup.class));
    }
}