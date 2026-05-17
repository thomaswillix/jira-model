package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void shouldCreateValidUser() {
        User user = new User("thomaswillix");
        assertEquals("thomaswillix", user.getUserName());
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class, () -> new User(null)
        );
        assertEquals("Username can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldThrowExceptionWhenUserNameIsBlank(String blankName) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new User(blankName)
        );
        assertEquals("Username can't be blank.", exception.getMessage());
    }

    // setter

    @Test
    void shouldUpdateUserNameCorrectlyWhenValid(){
        User user = new User("thomaswillix");
        user.setUserName("john_doe");

        assertEquals("john_doe", user.getUserName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldNotAllowBlankUserNameViaSetter(String blankUserName){
        User user = new User("john_doe");
        assertThrows(IllegalArgumentException.class, () -> user.setUserName(blankUserName));
    }

    @Test
    public void shouldNotAllowNullUserNameViaSetter() {
        User user = new User("john_doe");
        assertThrows(NullPointerException.class, () -> user.setUserName(null));
    }
}
