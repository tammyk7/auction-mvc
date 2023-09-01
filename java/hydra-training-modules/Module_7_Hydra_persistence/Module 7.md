# Module 7 - Hydra Persistence

- Hydra Repositories
- Transaction support
- Snapshotting
- Referential integrity
- Schema evolution
- Relational Databases
- Seeding/initialisation (populate with initial data) -> Cucumber to Admin REST API

## Indexes

An index is a data structure that improves the speed of data retrieval operations on a database table. Just like most
other databases, Hydra repositories support indexing. The most straight-forward implementation of this is when you
create a unique primary key for your tables. The primary key is indexed, giving you constant time lookups for rows.
However, what if you want to build an index on a field other than the primary key?

Suppose you have a situation where you want an index on a field like `email`, where each email is unique.

```
table Person = {
  primary key id: int64
  name: char[255]
  email: char[255]
}
```

We can use an out-of-the-box index `UniqueColumnIndex`. This is a generic hash index, used to define one-to-one
relationships. This index can be used to map the email to the row index and vice-versa, giving you constant time lookups.

But, what if you want an index that can be used to define one-to-many relationships? Or perhaps it's not in your best
interest to use a hash index, but a b-tree index? At the time of writing, Hydra has one out-of-the-box
index, `UniqueColumnIndex`.

If you want these other indexes, you will have to create your own implementation of them using the `RepositoryPlugin`
interface. Fun fact, the `UniqueColumnIndex` implements this interface as well. To create your own index, there is
a [tutorial](https://docs.hydra.weareadaptive.com/LATEST/Development/Persistence/WriteAnIndex.html) in the Hydra docs.

### Interfaces

In this section, we’ll be discussing the interfaces used to create an index. In order to do that, we'll be breaking
down `OneToOneIndexImpl`, which is a simplified version of `UniqueColumnIndex`.

```java
public interface OneToOneIndex<RowT extends FlyweightCodec, KeyT> extends RepositoryPlugin<RowT>, DirtyRowHandler {
    RowT get(KeyT key);
}
```

Starting with the `RepositoryPlugin` interface, we need to implement a method:

`onAttached` which is a method that will receive a single call when it is attached to a repository. In this
implementation we build an index to have an initial mapping of our repository.

 ```java
public void onAttached(RepositoryPluginHost<RowT> host, long currentMutationSeqNo){
    this.lastSeenMutationSeqNo=currentMutationSeqNo;
    this.host=host;
    this.cursor=host.createCursor();

    buildIndex();
}
 ```

As a part of the `onAttached` method, we have two important parameters that we'll be explained in greater detail:

- `RepositoryPluginHost<RowT> host`: Allows us to interact with the repository we are attached to.
- `long currentMutationSeqNo`: Used to keep track of the repository's state.

Additionally, we need to implement another method `doEndOfUnitOfWorkProcessing`. This is a method that will be called at
least once after processing a message or a timer callback and then return a value representing the amount of work done.

 ```java
public int doEndOfUnitOfWorkProcessing(){
    return host.pollForDirtyRows(lastSeenMutationSeqNo, this);
}
 ```

In this implementation of `doEndOfUnitOfWorkProcessing`, we are polling the `host` (the repository we are attached to),
for "dirty rows". In other words rows in our repository that have been created, mutated, or deleted. The
method `pollForDirtyRows` allows us to supply a handler to update our indexes accordingly. In this scenario, we are
supplying a handler that is implemented as a part of our index `DirtyRowHandler`.

Each of the methods implemented as a part of `DirtyRowHandler` will be called when:

- `onRowDirty(int rowIndex)`: Rows have been created/mutated/deleted while providing us with the index of the respective
  row.
- `onAllRowsDirty`: All rows have been modified.
- `onOutOfOrderPollCompleted(long mutationSeqNo)`: Polling is completed, providing the latest mutation sequence number.

 ```java
public void onRowDirty(int rowIndex){
    //update our indexes when rows are created/mutated/deleted
}

public void onAllRowsDirty(){
    clearIndex();
    buildIndex();
}

public void onOutOfOrderPollCompleted(long mutationSeqNo){
    lastSeenMutationSeqNo = mutationSeqNo;
}
 ```
