package com.weareadaptive.auction.model;


import static com.weareadaptive.auction.StringUtil.isNullOrEmpty;

public class User implements Entity
{
    private final int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String organisation;
    private final boolean isAdmin;
    private boolean isBlocked;

    public User(final int id, final String username, final String password, final String firstName,
                final String lastName, final String organisation)
    {
        this(id, username, password, firstName, lastName, organisation, false);
    }

    public User(final int id, final String username, final String password, final String firstName,
                final String lastName, final String organisation,
                final boolean isAdmin)
    {
        if (isNullOrEmpty(username))
        {
            throw new BusinessException("username cannot be null or empty");
        }
        if (!UserValidation.isAlphanumeric(username))
        {
            throw new BusinessException("username must contain letters and numbers");
        }
        if (isNullOrEmpty(password))
        {
            throw new BusinessException("password cannot be null or empty");
        }
        if (isNullOrEmpty(firstName))
        {
            throw new BusinessException("first name cannot be null or empty");
        }
        if (isNullOrEmpty(lastName))
        {
            throw new BusinessException("last name cannot be null or empty");
        }
        if (isNullOrEmpty(organisation))
        {
            throw new BusinessException("organisation cannot be null or empty");
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
