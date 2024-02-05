package com.weareadaptive.auction.model;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class UserCollection extends State<User>
{

    private final Map<String, User> usernameIndex;

    public UserCollection()
    {
        usernameIndex = new HashMap<>();
    }

    @Override
    protected void onAdd(final User model)
    {
        if (usernameIndex.containsKey(model.getUsername()))
        {
            throw new BusinessException(format("Username \"%s\" already exist", model.getUsername()));
        }
        usernameIndex.put(model.getUsername(), model);
    }

    public boolean containsUsername(final String username)
    {
        return usernameIndex.containsKey(username);
    }

    public void updateUser(final User oldUser, final String username, final String password, final String firstName,
                           final String lastName, final String organisation)
    {
        usernameIndex.remove(oldUser.getUsername());

        User newUser = new User(nextId(), username, password, firstName, lastName, organisation);
        usernameIndex.put(newUser.getUsername(), newUser);
    }

    public User getUser(final int id)
    {
        return get(id);
    }


    public Optional<User> validateUsernamePassword(final String username, final String password)
    {
        if (!usernameIndex.containsKey(username))
        {
            return Optional.empty();
        }
        var user = usernameIndex.get(username);
        if (!user.validatePassword(password))
        {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
