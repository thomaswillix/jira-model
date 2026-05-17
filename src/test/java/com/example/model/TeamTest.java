package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class TeamTest {
    private final User dummyUser = new User("john_doe");

    // Constructor
    @Test
    public void shouldCreateTeamWithValidData() {
        Team team = new Team("Alpha Team", List.of(dummyUser));

        assertEquals("Alpha Team", team.getTeamName());
        assertEquals(1, team.getUsers().size());
        assertTrue(team.getUsers().contains(dummyUser));
    }

    @Test
    public void shouldNotModifyOriginalListWhenTeamIsCreated() {
        List<User> originalList = new ArrayList<>(List.of(dummyUser));
        Team team = new Team("Alpha Team", originalList);

        originalList.add(new User("intruder"));

        assertEquals(1, team.getUsers().size());
    }

    @Test
    public void shouldThrowExceptionWhenTeamNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Team("  ", List.of(dummyUser)));
    }

    @Test
    public void shouldThrowExceptionWhenTeamNameIsNull() {
        assertThrows(NullPointerException.class, () -> new Team(null, List.of(dummyUser)));
    }

   @Test
    public void shouldThrowExceptionWhenUsersListIsNull() {
        assertThrows(NullPointerException.class, () -> new Team("Alpha Team", null));
    }

    @Test
    public void shouldThrowExceptionWhenUsersListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Team("Alpha Team", List.of()));
    }

    // setter

    @Test
    public void shouldUpdateTeamNameCorrectlyWhenValid() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        team.setTeamName("New team name");

        assertEquals("New team name", team.getTeamName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldNotAllowBlankTeamNameViaSetter(String invalidTeamName) {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        assertThrows(IllegalArgumentException.class, () -> team.setTeamName(invalidTeamName));
    }

    @Test
    public void shouldNotAllowNullTeamNameViaSetter() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        assertThrows(NullPointerException.class, () -> team.setTeamName(null));
    }

    // addUser

    @Test
    public void shouldAllowAddingANewUserToTheTeam() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        User newUser = new User("alice_smith");

        team.addUser(newUser);

        assertEquals(2, team.getUsers().size());
        assertTrue(team.getUsers().contains(newUser));
    }

    @Test
    public void shouldThrowExceptionWhenAddingNullUser() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        assertThrows(NullPointerException.class, () -> team.addUser(null));
    }

    @Test
    public void shouldNotAllowAddingDuplicateUsers() {
        Team team = new Team("Alpha Team", List.of(dummyUser));

        assertThrows(IllegalArgumentException.class, () -> team.addUser(dummyUser));
    }

    // removeUser

    @Test
    public void shouldAllowRemovingAUser() {
        User user1 = new User("user_one");
        User user2 = new User("user_two");
        Team team = new Team("Alpha Team", new ArrayList<>(List.of(user1, user2)));

        team.removeUser(user1);

        assertEquals(1, team.getUsers().size());
        assertFalse(team.getUsers().contains(user1));
    }

    @Test
    public void shouldNotAllowRemovingTheLastUser() {
        Team team = new Team("Alpha Team", List.of(dummyUser));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                team.removeUser(dummyUser)
        );
        assertEquals("A team must have at least one user. Cannot leave the team empty.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenRemovingNullUser() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        assertThrows(NullPointerException.class, () -> team.removeUser(null));
    }

    @Test
    public void shouldThrowExceptionWhenRemovingUserNotInTeam() {
        Team team = new Team("Alpha Team", List.of(dummyUser));
        User stranger = new User("stranger");

        team.addUser(new User("second"));

        assertThrows(IllegalArgumentException.class, () -> team.removeUser(stranger));
    }

    @Test
    public void shouldThrowExceptionIfTryingToModifyGetUsersListExternally() {
        Team team = new Team("Alpha Team", List.of(dummyUser));

        assertThrows(UnsupportedOperationException.class, () ->
                team.getUsers().add(new User("hacker"))
        );
    }
}
