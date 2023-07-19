# RxJS Marble Testing

Before moving on to the testing, let's review the two different types of Observables: cold and hot. This characteristic depends on where the producer of this Observable is set; when the producer is created by the Observable, the observable is "cold", instead when the producer is created out from the observable it is defined "hot".

Cold Observables are functions that create the producer and manage it for all its life. The cold observable is in charge of these things:

- Create the producer
- Activate the producer
- Start listening to the producer
- Unicast
- Close the producer

An observable is “hot” if its underlying producer is either created or activated outside of subscription.

- Shares a reference to a producer
- Starts listening to the producer
- Multicast (usually)

If you want to read more about hot and cold observables, check out this [post](https://benlesh.medium.com/hot-vs-cold-observables-f8094ed53339).

## Testing RxJS Code with Marble Diagrams

To test our code in RxJS we use Marble testing. A method that combines the [Marble Diagram](https://rxmarbles.com/) with the code, and allows us to represent the behaviors of our observables and translate them to something that the Testing Framework can understand.

Marble diagrams provide a visual way for us to represent the behavior of an Observable. We can use them to assert that a particular Observable behaves as expected, as well as to create hot and cold Observables we can use as mocks.

**Syntax**

Marble testing has its own syntax to represent the behaviors:

- `' '` whitespace - horizontal whitespace is ignored, and can be used to help vertically align multiple marble diagrams.
- `'-'` frame - 1 "frame" of virtual time passing.
- `[0-9]+[ms|s|m]` time progression - the time progression syntax lets you progress virtual time by a specific amount. It's a number, followed by a time unit of `ms`(milliseconds), `s`(seconds), or `m`(minutes) without any space between them, e.g. `a 10ms b`.
- `'|'` complete - The successful completion of an observable. This is the observable producer signaling `complete()`.
- `'#'` error - An error terminating the observable. This is the observable producer signaling `error()`.
- `[a-z0-9]` e.g. 'a' any alphanumeric character - Represents a value being emitted by the producer signaling `next()`.
- `()` sync groupings - When multiple events need to be in the same frame synchronously, parentheses are used to group those events.
- `^` subscription point - (hot observables only) shows the point at which the tested observables will be subscribed to the hot observable. This is the "zero frame" for that observable, every frame before the `^` will be negative. Negative time might seem pointless, but there are in fact advanced cases where this is necessary, usually involving ReplaySubjects.

**API**

We can test our asynchronous RxJS code synchronously and deterministically by virtualizing time using the `TestScheduler`. The `TestScheduler` translates the Marble Syntax to something that the Test Framework can understand. It's important to remember that we cannot use the `TestScheduler` to test our code all the time. For example, if the code consumes a `Promise` we cannot use it but we should use a traditional method.

The TestScheduler exposes us some APIs that we can use to write our tests but we start from its initialization.

```tsx
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })
})
```

The callback function you provide to testScheduler.run(callback) is called with helpers object that contains functions you'll use to write your tests.

```tsx
testScheduler.run((helpers) => {
  const {
    cold,
    hot,
    expectObservable,
    expectSubscriptions,
    flush,
    time,
    animate,
  } = helpers
  // use them
})
```

- `cold(marbleDiagram: string, values?: object, error?: any)`- creates a "cold" observable whose subscription starts when the test begins.
- `hot(marbleDiagram: string, values?: object, error?: any)` - creates a "hot" observable (like a subject) that will behave as though it's already "running" when the test begins. An interesting difference is that hot marbles allow a `^` character to signal where the "zero frame" is. That is the point at which the subscription to observables being tested begins.
- `expectObservable(actual: Observable<T>, subscriptionMarbles?: string).toBe(marbleDiagram: string, values?: object, error?: any)` - schedules an assertion for when the `TestScheduler` flushes. Give `subscriptionMarbles` as parameter to change the schedule of subscription and unsubscription. If you don't provide the `subscriptionMarbles` parameter it will subscribe at the beginning and never unsubscribe.
- `expectSubscriptions(actualSubscriptionLogs: SubscriptionLog[]).toBe(subscriptionMarbles: string)` - like `expectObservable` schedules an assertion for when the `testScheduler` flushes. Both `cold()` and `hot()` return an observable with a property `subscriptions` of type `SubscriptionLog[]`. Give `subscriptions` as parameter to `expectSubscriptions` to assert whether it matches the `subscriptionsMarbles` marble diagram given in `toBe()`. Subscription marble diagrams are slightly different than Observable marble diagrams.
- `flush()` - immediately starts virtual time. Not often used since `run()` will automatically flush for you when your callback returns, but in some cases you may wish to flush more than once or otherwise have more control.
- `time()` - converts marbles into a number indicating number of frames. It can be used by operators expecting a specific timeout. It measures time based on the position of the complete (`|`) signal.
- `animate()` - specifies when requested animation frames will be 'painted'. `animate` accepts a marble diagram and each value emission in the diagram indicates when a 'paint' occurs - at which time, any queued `requestAnimationFrame` callbacks will be executed. Call `animate` at the beginning of your test and align the marble diagrams so that it's clear when the callbacks will be executed.

It's important to create a new `TestScheduler` for every test, this allows us to have a new instance for every test and create a clean case. But the weird thing in this code is the body of the code passing to the `TestScheduler`. This code is particular if you are confident with any test framework because in this function we have already indicated the expectations of the test, but we haven't written one yet. This, because the `TestScheduler` exposes some helpers to test the code and these helpers call the function indicated in the constructor of the TestScheduler to check the failure or the success of the test.

```tsx
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })

  it('test', () => {
    testScheduler.run((helpers) => {
      const { cold, expectObservable } = helpers
      const source$ = cold('-a-b-c|')
      const expected = '-a-b-c|'

      expectObservable(source$).toBe(expected)
    })
  })
})
```

In this example, we created a cold Observable that emits 3 values: a, b and c. Using the `expectObservable` helper we can test our observable by comparing it with the expectation passed to the `toBe` method.

```tsx
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })

  it('test with values', () => {
    testScheduler.run((helpers) => {
      const { cold, expectObservable } = helpers
      const source$ = cold('-a-b-c|', { a: 1, b: 2, c: 3 })
      const expected = '-a-b-c|'

      expectObservable(source$).toBe(expected, { a: 1, b: 2, c: 3 })
    })
  })
})
```

We can pass another argument to the cold function. This argument is an object where the fields are the correspondents of the value passed in the marble string, so if you use a, b, and c in the marble string you have to use a, b, and c as fields of your argument. The values of these fields are the values used by the test and emitted by the observable. The same goes for the `toBe` method, it accepts another argument where we can pass the expected result values.

Another important concept when you test your observables is the time, in these cases, it's possible to specify after how much time an observable emits a value or after how much time a value is expected. Here, an example using the `concatMap` operator combined with the `delay` operator that delays the result by 100ms.

```tsx
import { concatMap, delay, of } from 'rxjs'
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })

  it('test', () => {
    testScheduler.run((helpers) => {
      const { cold, expectObservable } = helpers
      const source$ = cold('-a-b-c|')
      const final$ = source$.pipe(concatMap((val) => of(val).pipe(delay(100))))
      const expected = '- 100ms a 99ms b 99ms (c|)'
      expectObservable(final$).toBe(expected)
    })
  })
})
```

**Hot Observables**

When you create a hot Observable, you can indicate when the observables are subscribed using the `^` character. When you indicate the subscription your results start from the subscription and the values emitted before are ignored by the test.

```tsx
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })

  it('test', () => {
    testScheduler.run((helpers) => {
      const { hot, expectObservable } = helpers
      const source$ = hot('-a-b-^-c|')
      const expected = '--c|'
      expectObservable(source$).toBe(expected)
    })
  })
})
```

**Subscriptions**

It could be necessary to test when an observable is subscribed and for how much time.

For example, we have two observables combined together using a `concat` operator, in this case, we need to test if the first observable is subscribed and when it is completed we need to check if the second observable is subscribed.
While you are before these cases, you need to use the `expectSubscriptions` helper. This helper allows you to check the subscriptions of an observable and detects when the observable is subscribed and when is unsubscribed.

Here the code for the example:

```tsx
import { concat } from 'rxjs'
import { TestScheduler } from 'rxjs/testing'

describe('Marble Testing', () => {
  let testScheduler: TestScheduler

  beforeEach(() => {
    testScheduler = new TestScheduler((actual, expected) => {
      expect(actual).toEqual(expected)
    })
  })

  it('test subscriptions', () => {
    testScheduler.run((helpers) => {
      const { cold, expectObservable, expectSubscriptions } = helpers
      const source1$ = cold('-a-b-c|')
      const source2$ = cold('-d-e-f|')
      const final$ = concat(source1$, source2$)

      const expected = '-a-b-c-d-e-f|'
      const expectedSubscriptionsSource1 = '^-----!'
      const expectedSubscriptionsSource2 = '------^-----!'

      expectObservable(final$).toBe(expected)
      expectSubscriptions(source1$.subscriptions).toBe(
        expectedSubscriptionsSource1
      )
      expectSubscriptions(source2$.subscriptions).toBe(
        expectedSubscriptionsSource2
      )
    })
  })
})
```

To read more about Marble Testing you can check out the [RxJS Documentation](https://rxjs.dev/guide/testing/marble-testing).
