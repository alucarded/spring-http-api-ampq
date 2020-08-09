package com.phoneservice.phoneservice;

import com.phoneservice.phoneservice.model.Signup;
import com.phoneservice.phoneservice.model.SignupParsed;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SignupListener {

    private static final String POLISH_PHONE_PREFIX = "+48";

    @RabbitListener(queues = {"signups"})
    public void receiveMessage(final Message<Signup> message) {
        SignupParsed signupParsed = parseSignup(message.getPayload());
        String output = String.format("User %s %s with phone %s has just signed up!",
                signupParsed.getFirstName(), signupParsed.getLastName(), signupParsed.getPhone());
        // Print to standard output
        System.out.println(output);
    }

    SignupParsed parseSignup(Signup signup) {
        // Split first name and surname
        String[] fullNameSplit = parseFullName(signup.getFullName());
        // Remove spaces from phone number and add Polish phone prefix (if it does not exist)
        String parsedPhone = parsePhoneNumber(signup.getPhone());
        return new SignupParsed(fullNameSplit[0], fullNameSplit[1], parsedPhone);
    }

    String[] parseFullName(String fullName) {
        String[] fullNameSplit = fullName.split(" ", 2);
        String firstName = fullNameSplit[0];
        String lastName = fullNameSplit.length > 1 ? fullNameSplit[1] : "";
        return new String[]{firstName, lastName};
    }

    String parsePhoneNumber(String phone) {
        String parsedPhone = phone.replace(" ", "");
        if (!parsedPhone.isEmpty() && !parsedPhone.startsWith(POLISH_PHONE_PREFIX)) {
            parsedPhone = POLISH_PHONE_PREFIX + parsedPhone;
        }
        return parsedPhone;
    }
}