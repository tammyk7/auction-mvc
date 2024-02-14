package com.weareadaptive.auction.user;


import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import static com.weareadaptive.auction.StringUtil.isNullOrEmpty;

@Entity(name = "AuctionUser")
public class User implements com.weareadaptive.auction.model.Entity
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String organisation;
    private boolean isAdmin;
    private boolean isBlocked;

    public User(final int id, final String username, final String password, final String firstName, final String lastName, final String organisation)
    {
        this(id, username, password, firstName, lastName, organisation, false);
    }

    public User(final int id, final String username, final String password, final String firstName, final String lastName, final String organisation, final boolean isAdmin)
    {
        if (isNullOrEmpty(username))
        {
            throw new AuthenticationExceptionHandling.BusinessException("username cannot be null or empty");
        }
        if (!UserValidation.isAlphanumeric(username))
        {
            throw new AuthenticationExceptionHandling.BusinessException("username must contain letters and numbers");
        }
        if (isNullOrEmpty(password))
        {
            throw new AuthenticationExceptionHandling.BusinessException("password cannot be null or empty");
        }
        if (isNullOrEmpty(firstName))
        {
            throw new AuthenticationExceptionHandling.BusinessException("first name cannot be null or empty");
        }
        if (isNullOrEmpty(lastName))
        {
            throw new AuthenticationExceptionHandling.BusinessException("last name cannot be null or empty");
        }
        if (isNullOrEmpty(organisation))
        {
            throw new AuthenticationExceptionHandling.BusinessException("organisation cannot be null or empty");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.isAdmin = isAdmin;
        this.isBlocked = false;
    }

    public User()
    {

    }

    @Override
    public String toString()
    {
        return username + '\'';
    }

    public void setUsername(final String username)
    {
        if (!username.isEmpty())
        {
            this.username = username;
        }
    }

    public void setPassword(final String password)
    {
        if (!password.isEmpty())
        {
            this.password = password;
        }
    }

    public void setFirstName(final String firstName)
    {

        if (!firstName.isEmpty())
        {
            this.firstName = firstName;
        }
    }

    public void setLastName(final String lastName)
    {
        if (!lastName.isEmpty())
        {
            this.lastName = lastName;
        }
    }

    public void setOrganisation(final String organisation)
    {
        if (!organisation.isEmpty())
        {
            this.organisation = organisation;
        }
    }

    public void setBlocked(final boolean blocked)
    {
        isBlocked = blocked;
    }

    public String getUsername()
    {
        return username;
    }

    public boolean validatePassword(final String password)
    {
        return this.password.equals(password);
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getId()
    {
        return id;
    }

    public String getOrganisation()
    {
        return organisation;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public boolean isBlocked()
    {
        return isBlocked;
    }

}
