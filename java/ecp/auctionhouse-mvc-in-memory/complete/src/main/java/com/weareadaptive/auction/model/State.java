package com.weareadaptive.auction.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class State<T extends Entity> {
  public static final String ITEM_ALREADY_EXISTS = "Item already exists";
  private final Map<Integer, T> entities;
  private int currentId = 1;

  public State() {
    entities = new HashMap<>();
  }

  public int nextId() {
    return currentId++;
  }

  protected void onAdd(T model) {

  }

  public void add(T model) {
    if (entities.containsKey(model.getId())) {
      throw new BusinessException(ITEM_ALREADY_EXISTS);
    }
    onAdd(model);
    entities.put(model.getId(), model);
  }

  void setNextId(int id) {
    this.currentId = id;
  }

  public T get(int id) {
    return entities.get(id);
  }

  public Stream<T> stream() {
    return entities.values().stream();
  }
}
