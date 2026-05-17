package com.example.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.functions.Validation.*;

public class Team{
    @Getter
    private String teamName;
    private final List<User> users;

    public Team(String teamName, List<User> users) {
        requireValidUsersList(users);

        this.teamName = requireValidString(teamName, "Team name");
        this.users = new ArrayList<>(users);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(this.users);
    }

    public void setTeamName(String teamName){
        this.teamName = requireValidString(teamName, "Team name");
    }

    public void addUser(User user) {
        requireNonNull(user, "User");
        if (this.users.contains(user)) throw new IllegalArgumentException("User is already a member of this team.");

        this.users.add(user);
    }

    public void removeUser(User user) {
        requireNonNull(user, "User");
        if (this.users.size() <= 1)
            throw new IllegalStateException("A team must have at least one user. Cannot leave the team empty.");

        if (!this.users.remove(user)) throw new IllegalArgumentException("User is not a member of this team.");
    }

    private void requireValidUsersList(List<User> users){
        requireNonNull(users, "Users list");
        if (users.isEmpty()) throw new IllegalArgumentException("Users list can't be empty.");
    }
}
