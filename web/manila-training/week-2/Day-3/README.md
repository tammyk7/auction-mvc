# Day 3: Observables, Observers, and Subscriptions with TypeScript

## Creating Observables

Observables in RxJS represent a source of data streams that emit values over time. You can create observables using various methods provided by RxJS, such as the `Observable` constructor.

1. **Observable Constructor:** The `Observable` constructor is the most fundamental way to create an observable from scratch. It takes a subscriber function as an argument, which defines how values are emitted over time. Inside the `observer` function, you can use the `next` method to emit values, the `error` method to emit an error, and the `complete` method to signal the completion of the observable.

   ```typescript
   import { Observable } from 'rxjs'

   const numberObservable$ = new Observable((subscriber) => {
     subscriber.next(1)
     subscriber.next(2)
     subscriber.next(3)

     setTimeout(() => {
       subscriber.next(4)
       subscriber.complete()
     }, 1000)
   })
   ```

   In the example above, we are using the `Observable` constructor to create a `numberObservable$` that emits 3 values and then after a second, emits another value and then completes. Remember that after a complete notification has been emitted, there can be no other value emitted. even if you put a `next` after you will not see anything.

2. **Factory Method:** RxJS provides various factory methods that simplify the creation of observables for common use cases. Some commonly used factory methods include:

   1. **of**

      ```javascript
      import { of } from 'rxjs'
      const dataObservable = of(1, 2, 3, 4, 5)
      ```

   2. **from**

      ```javascript
      import { from } from 'rxjs'

      const array = [1, 2, 3, 4]
      const arrayObservable$ = from(array)
      ```

   > The difference between `of` and `from` is that `of` is used when we want to create an `observable` out any number of arguments which emits in order, primitive values and objects. `from` is used when we want to create an `observable` when we have have a promise, array or array-like object or some form of iterable.

# Subscribing and Unsubscribing to Observables

Subscriptions represent the connection between an `Observer` and an `Observable`. They allow you to receive and handle the emitted values from the observable.

To subscribe to an observable, you can use the subscribe method, which takes an object with callback functions as arguments. The next function is called for each emitted value, the complete function is called when the observable completes, and the error function is called when an error occurs.

so to invoke the `Observable` and see the value, we need to subscribe it.

```JavaScript
import { Observable } from 'rxjs';

const numberObservable$ = new Observable((subscriber) => {
  subscriber.next(1)
  subscriber.next(2)
  subscriber.next(3)

  setTimeout(() => {
    subscriber.next(4)
    subscriber.complete()
  }, 1000)
})

console.log('just before subscribe');
numberObservable$.subscribe({
  next(x) {
    console.log('got value ' + x)
  },
  error(err) {
    console.error('something wrong occurred: ' + err)
  },
  complete() {
    console.log('done')
  },
})
console.log('just after subscribe')
```

## Unsubscribing

Unsubscribing from an observable is an important step to release resources and stop receiving values when you no longer need them. Unsubscribing ensures that your application doesn't retain unnecessary subscriptions and prevents memory leaks.

When you subscribe to an observable, the subscribe method returns a Subscription object. which as mentioned before is the connection between the `Observer` and an `Observable`. You can call the unsubscribe method on the Subscription object to stop receiving values and release associated resources.

- **Calling `unsubscribe`:** To unsubscribe from an observable, simply call the unsubscribe method on the Subscription object returned by the subscribe method.

  ```javascript
  numberObservable$.unsubscribe()
  ```

- **Completing and Error Handling:** When an observable completes or encounters an error, it automatically unsubscribes all its subscribers. Therefore, you don't need to manually unsubscribe in these cases. However, it's still good practice to unsubscribe explicitly when you no longer need the values, even if the observable completes.

- **Teardown Logic:** It's essential to include appropriate teardown logic in the observable's creation function or use operators like `finalize` or `takeUntil` to handle resource cleanup. Teardown logic ensures that any resources associated with the observable, such as timers, event listeners, or subscriptions to other observables, are properly cleaned up when unsubscribing. Failing to include teardown logic may result in resource leaks.

### In Terms of React

1. **Unsubscribing in React Components:** In React, it's important to unsubscribe from observables when the component is unmounted to avoid memory leaks and unnecessary data processing. To handle this, you can use the useEffect hook to subscribe and unsubscribe from the observable.

   ```javascript
   import { useEffect } from 'react'
   import { Observable } from 'rxjs'

   const MyComponent = () => {
     useEffect(() => {
       const subscription = myObservable.subscribe((value) => {
         console.log('Received value:', value)
       })

       return () => {
         subscription.unsubscribe()
       }
     }, []) // Empty dependency array ensures the effect runs only once

     return <div>React component rendering</div>
   }

   export default MyComponent
   ```

2. **Unsubscribing from Multiple Observables:** If your component subscribes to multiple observables, it's important to unsubscribe from each one individually. You can create separate subscription variables for each observable and unsubscribe from them in the cleanup function.

   ```JavaScript
    useEffect(() => {
      const subscription1$ = observable$.subscribe((value) => {
        console.log('Subscription 1:', value);
      })

      const subscription2$ = observable$.subscribe((value) => {
        console.log('Subscription 2:', value);
      })

      return () => {
        subscription1$.unsubscribe();
        subscription2$.unsubscribe();
      }
    }, [])
   ```

# Using TypeScript types with observables and subscriptions

Leveraging TypeScript with Observables in RxJS offers several advantages, including improved type safety, better tooling support, and enhanced code readability. TypeScript's strong static typing enables you to catch errors early in the development process, making it easier to work with Observables - ensuring that your code is more reliable and maintainable.

1. **Specifying Types for Observables:**

    TypeScript allows you to specify the type values emitted by an Observable using generics. By defining the type of an Observable, you ensure that the emitted values are of the correct type, reducing the risk of runtime errors caused by incorrect data types.

    ```ts
    import { Observable } from 'rxjs';

    const numberObservable: Observable<number> = new Observable<number>((subscriber) => {
      subscriber.next(1);
      subscriber.next(2);
      subscriber.next(3);
      subscriber.complete();
    });
    ```

    In the example, we have defined an Observable `numberObservable` that emits numbers of type `number`. TypeScript will enforce that only numbers can be emitted by this Observable.

<br/>

2. **Type Inference from Operators:**

    RxJS operators propagate types through the operator chain, allowing TypeScript to infer the types of values emitted by intermedia and final Observables. This enables concise and expressive code without sacrificing type safety.

    ```ts
    import { from } from 'rxjs';
    import { map } from 'rxjs/operators';

    const source$ = from([1, 2, 3]);

    const doubled$ = source$.pipe(
      map((value) => value * 2)
    );
    ```

    In this example, TypeScript infers that the `doubled$` Observable will emit numbers, as the `map` operator transforms each value into a new number.

<br />

3. **Typed Subscriptions:**

    When subscribing to an Observable, TypeScript allows you to specify the type of values received in the subscription callback, ensuring that you handle the emitted values correctly.

    ```ts
    import { Observable } from 'rxjs';

    const numberObservable: Observable<number> = ...

    const subscription = numberObservable.subscribe((value: number) => {
      console.log(value);
    });
    ```

    In this example, we have a subscription to the numberObservable, and we explicitly define that the subscription will receive numbers in the subscription callback.

<br />

4. **Unsubscribing and Resource Management:**

    TypeScript can help you manage subscriptions and prevent memory leaks by handling unsubscribing from Observables. Storing the subscription in a variable allows you to call `unsubscribe()` when it is no longer needed.

    ```ts
    import { Observable, Subscription } from 'rxjs';

    const numberObservable: Observable<number> = ...

    const subscription: Subscription = numberObservable.subscribe((value: number) => {
      console.log(value);
    });

    // Unsubscribe when no longer needed
    subscription.unsubscribe();
    ```

    By calling unsubscribe(), you release resources associated with the subscription and prevent potential memory leaks, especially when dealing with long-lived Observables.

Leveraging TypeScript with Observables in RxJS provides a more robust and maintainable reactive codebase. TypeScript's type system catches many potential issues during development, leading to a smoother development process and better quality code. When combined with the powerful features of RxJS, TypeScript makes it easier to build complex and scalable applications while ensuring type safety and developer productivity.