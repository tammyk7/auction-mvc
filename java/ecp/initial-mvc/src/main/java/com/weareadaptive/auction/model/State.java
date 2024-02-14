package com.weareadaptive.auction.model;

import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class State<T extends Entity>
{
    public static final String ITEM_ALREADY_EXISTS = "Item already exists";
    private final Map<Integer, T> entities;
    private int currentId = 1;

    public State()
    {
        entities = new HashMap<>();
    }

    public int nextId()
    {
        return currentId++;
    }

    protected void onAdd(final T model)
    {

    }

    public void add(final T model)
    {
        if (entities.containsKey(model.getId()))
        {
            throw new AuthenticationExceptionHandling.BusinessException(ITEM_ALREADY_EXISTS);
        }
        onAdd(model);
        entities.put(model.getId(), model);
    }

    public void remove(final int id)
    {
        entities.remove(id);
    }

    void setNextId(final int id)
    {
        this.currentId = id;
    }

    public T get(final int id)
    {
        return entities.get(id);
    }

    public Stream<T> stream()
    {
        return entities.values().stream();
    }
}
