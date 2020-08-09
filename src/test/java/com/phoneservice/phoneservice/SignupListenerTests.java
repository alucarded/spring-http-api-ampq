package com.phoneservice.phoneservice;

import com.phoneservice.phoneservice.model.Signup;
import com.phoneservice.phoneservice.model.SignupParsed;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignupListenerTests {

    private static SignupListener signupListener;

    @BeforeAll
    static void beforeAll() {
        signupListener = new SignupListener();
    }

    @Test
    void basicPhoneParseTest() {
        assertEquals("+33546317171", signupListener.parsePhoneNumber("5 46 31 71 71"));
    }

    @Test
    void withFrenchPrefixPhoneParseTest() {
        assertEquals("+33546317171", signupListener.parsePhoneNumber(" +3 3 5 46 31 71 71"));
    }

    @Test
    void basicFullNameParseTest() {
        String[] names = signupListener.parseFullName("John Doe");
        assertEquals(2, names.length);
        assertEquals("John", names[0]);
        assertEquals("Doe", names[1]);
    }

    @Test
    void multiPartFullNameParseTest() {
        String[] names = signupListener.parseFullName("John Bob Doe");
        assertEquals(2, names.length);
        assertEquals("John", names[0]);
        assertEquals("Bob Doe", names[1]);
    }

    @Test
    void basicMessageTest() {
        assertEquals(new SignupParsed("John", "Doe", "+33546317171"),
                signupListener.parseSignup(new Signup("John Doe", "5 46 31 71 71")));
    }

    @Test
    void emptyAllMessageTest() {
        assertEquals(new SignupParsed("", "", ""),
                signupListener.parseSignup(new Signup("", "")));
    }
}
