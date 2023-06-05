# Using mergeWithKey and partitionByKey in React-RxJS

React-RxJS provides a set of utility functions that can be used to manage complex state and event handling scenarios. Two of these functions are `mergeWithKey` and `partitionByKey`. These functions are used to combine multiple observables into a single observable, while preserving a key that can be used to identify the source of each emitted value.

---

## `mergeWithKey`

The `mergeWithKey` function is used to merge multiple observables into a single observable. Each emitted value is paired with a key that identifies the source observable.

Here's an example of how you might use `mergeWithKey`:

```jsx
import { mergeWithKey } from "@react-rxjs/utils";
import { interval } from "rxjs";
import { map } from "rxjs/operators";

const source1$ = interval(1000).pipe(map(value => ({ type: "source1", value })));
const source2$ = interval(2000).pipe(map(value => ({ type: "source2", value })));

const merged$ = mergeWithKey({
  source1: source1$,
  source2: source2$,
});

merged$.subscribe(console.log);

```

In this example, `source1$` and `source2$` are two observables that emit values at different intervals. The `mergeWithKey` function is used to merge these two observables into a single observable, `merged$`. Each emitted value is an object that includes a `type` property (the key) and a `value` property.

---

## `partitionByKey`

The `partitionByKey` function is used to split a single observable into multiple observables based on a key. Each emitted value is directed to the observable that corresponds to its key.

Here's an example of how you might use `partitionByKey`:

```jsx
import { partitionByKey } from "@react-rxjs/utils";
import { interval } from "rxjs";
import { map } from "rxjs/operators";

const source$ = interval(1000).pipe(map(value => ({ type: value % 2 === 0 ? "even" : "odd", value })));

const [even$, odd$] = partitionByKey(source$, value => value.type);

even$.subscribe(value => console.log("Even:", value));
odd$.subscribe(value => console.log("Odd:", value));

```

In this example, `source$` is an observable that emits a sequence of numbers. The `partitionByKey` function is used to split this observable into two observables, `even$` and `odd$`, based on whether each emitted value is even or odd.

---

## When to Use `mergeWithKey` and `partitionByKey`

`mergeWithKey` and `partitionByKey` are useful when you're dealing with multiple observables that represent different aspects of your application's state or different sources of events. They allow you to combine or split these observables in a way that preserves the identity of each source.

Here are a few scenarios where you might want to use these functions:

- **Combining multiple sources of events**: If you have multiple observables that represent different sources of events (e.g., user interactions, network requests, timers), you can use `mergeWithKey` to combine these observables into a single observable. This can make it easier to manage and react to these events in a unified way.
- **Splitting events based on a criterion**: If you have an observable that emits a sequence of events, and you want to react to these events differently based on some criterion (e.g., the type of event), you can use `partitionByKey` to split this observable into multiple observables. This can make it easier

to handle different types of events in a more targeted way.

Let's look at a more complex, enterprise-grade example where we have a chat application that receives messages from different users.

```jsx
import { mergeWithKey } from "@react-rxjs/utils";
import { interval } from "rxjs";
import { map } from "rxjs/operators";

// Simulate messages from different users
const user1$ = interval(5000).pipe(map(value => ({ type: "user1", message: `Message ${value} from User 1` })));
const user2$ = interval(7000).pipe(map(value => ({ type: "user2", message: `Message ${value} from User 2` })));
const user3$ = interval(9000).pipe(map(value => ({ type: "user3", message: `Message ${value} from User 3` })));

const messages$ = mergeWithKey({
  user1: user1$,
  user2: user2$,
  user3: user3$,
});

messages$.subscribe(console.log);

```

In this example, `user1$`, `user2$`, and `user3$` are observables that simulate messages from different users. The `mergeWithKey` function is used to merge these observables into a single observable, `messages$`. Each emitted value is an object that includes a `type` property (the key, representing the user) and a `message` property.

Now, let's say we want to partition these messages based on the user. We can use `partitionByKey` for this:

```jsx
import { partitionByKey } from "@react-rxjs/utils";

const [user1Messages$, user2Messages$, user3Messages$] = partitionByKey(messages$, message => message.type);

user1Messages$.subscribe(message => console.log("User 1:", message));
user2Messages$.subscribe(message => console.log("User 2:", message));
user3Messages$.subscribe(message => console.log("User 3:", message));

```

In this example, `partitionByKey` is used to split the `messages$` observable into three observables, `user1Messages$`, `user2Messages$`, and `user3Messages$`, based on the user. Each of these observables emits the messages from a specific user.