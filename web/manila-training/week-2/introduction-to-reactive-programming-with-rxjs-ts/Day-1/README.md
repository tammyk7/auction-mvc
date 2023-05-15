# Day 1: Reactive Programming Fundamentals

## Introduction to Reactive Programming

Reactive programming is a programming paradigm that focuses on asynchronous data streams and the propagation of data changes. It emphasizes the idea that an application's state and data should automatically react and update as the underlying data changes. Reactive programming enables you to build highly responsive and interactive applications. In reactive programming, you define how data flows and transforms over time, allowing you to react to changes and events as they occur.

<hr>

What does reactive programming give me as a JavaScript developer? What's its significance?

### Reactive programming is significant for JavaScript developers for several reasons:

1. **Asynchronous by nature:** JavaScript is often used for web development, where asynchronous operations, such as AJAX requests, user interactions, and animations, are common. Reactive programming provides a robust way to manage and work with these asynchronous data streams effectively.
2. **Simplified data flow management:** Reactive programming enables developers to create complex data flows using simple, composable operations. This can make the code more readable and maintainable, particularly in large-scale applications.
3. **Improved performance:** Reactive programming can improve performance by reducing the need for frequent updates to the DOM, which can be expensive. By only updating the parts of the application that are affected by data changes, reactive programming can lead to more efficient, responsive applications.
4. **Scalability:** Reactive programming makes it easier to manage the complexity of modern applications, which often need to handle large amounts of data and support multiple users concurrently. The reactive paradigm simplifies the process of handling data streams and managing state, making it easier to scale applications.
5. **Better error handling:** Reactive programming provides powerful tools for handling errors and exceptions in asynchronous data streams. This can lead to more robust applications that can recover gracefully from failures.
6. **Integration with popular frameworks:** Many popular JavaScript frameworks, such as Angular, React, and Vue, have adopted reactive programming principles to some extent, making it a valuable skill for developers who work with these frameworks.

<hr>
Great, but how would I use reactive programming (RxJS in particular) in my applications? 

### Real world use cases for reactive programming:

1. **Real-time data updates:** RxJS can be used to manage real-time data updates in applications such as stock trading platforms, live sports scoreboards, or news feeds. By using RxJS observables, developers can handle incoming data streams and update the application state and UI accordingly.
2. **Autocomplete and search features:** In search boxes or autocomplete fields, user input generates multiple asynchronous requests. RxJS can help manage these requests efficiently by debouncing input, canceling previous requests, and ensuring that the UI only updates when the most recent request is completed.
3. **Complex user interactions:** RxJS is well-suited for handling complex user interactions such as drag-and-drop operations, multi-touch gestures, or mouse events. Developers can use RxJS to manage event streams, combine them, and transform them as needed to create a smooth user experience.
4. **Form validation:** RxJS can help create dynamic form validation by managing user input events, validating fields asynchronously, and updating the UI with error messages or success indicators in real-time.
5. **Infinite scrolling and pagination:** In web applications with large datasets, RxJS can be used to manage infinite scrolling or pagination. Developers can use RxJS to handle scroll events, load new data when the user reaches the end of the current data set, and update the UI accordingly.
6. **Debouncing and throttling:** RxJS can help debounce or throttle user input events, API requests, or other actions that may happen too frequently. By using RxJS operators like **`debounceTime`** or **`throttleTime`**, developers can control the rate at which events are processed and improve application performance.
7. **Communication between components:** In complex applications with multiple components, RxJS can be used to manage the communication between components using observables and subjects. This can simplify the flow of data and state updates in the application and improve maintainability.
8. **Managing application state:** RxJS can be used alongside state management libraries or frameworks such as Redux, NgRx, or react-rxjs hooks to create a reactive application state that updates automatically when the underlying data changes.


<hr>


Often two paradigms are thrown about when talking about reactive programming: **event-driven** vs. **reactive**. Below we will begin to pick apart the differences and similarities between the two.

<hr>


## Understanding Event-Driven and Reactive Programming Paradigms

### Event-Driven Programming
Event-driven programming is a paradigm where the flow of the program is driven by events, such as user interactions, such as user interactions (clicks, keypresses), system events (file loaded, timer triggered), or messages from other programs, or data changes. The application waits for these events and reacts to them by executing event handlers or callback functions. The program doesn't follow a predefined sequence of actions; instead, it responds to external events as they occur. Event-driven systems typically consist of event producers and event consumers, where producers generate events and consumers respond to those events.


```JavaScript
// Import the necessary RxJS functions
import { fromEvent } from 'rxjs'

// Get the button element from the DOM
const button = document.getElementById('myButton')

// Create an observable from the button click event
const clickObservable = fromEvent(button, 'click')

// Subscribe to the click event and handle it
clickObservable.subscribe(() => {
  console.log('Button clicked!');
})

//This is an example for a click event
```

> 💡 The event-driven programming paradigm is considered **imperative**. Take a look at the [imperative programming doc](imperative-programming.md) for explanations & examples.


<hr>
Okay, I have an example & a definition but I still don't quite understand how to think about event-driven programming. I get it! Definitions don't really help you develop a mental model. So let's take a look a two:
<hr>

### Mental Models

- Think of event-driven programming like a receptionist at a help desk. The receptionist waits for visitors to approach with requests, processes their requests, and responds accordingly. Visitors can approach the help desk at any time and in any order, but the receptionist processes each request one at a time.

- Consider a party DJ. The DJ plays music but also listens for event-driven cues, such as song requests from guests or announcements. When an event (e.g., a song request) occurs, the DJ responds by changing the music accordingly. In this analogy, the DJ represents the event-driven program, and the guests represent external events or inputs.

<hr>

Alright, now you have some models to think about the event-driven paradigm. Let's now take a look at how this type of programming is often used in the real world.

### Real-World Use Cases:

1. **User interfaces:** Event-driven programming is a natural fit for creating responsive user interfaces, as it allows developers to handle user interactions such as clicks, mouse movements, or keyboard inputs. In this context, events are generated by the user, and the application responds to those events by updating the interface or executing specific actions.
2. **Network communication:** In networked applications, event-driven programming can be used to handle incoming messages, data, or requests from clients or servers. The application listens for events such as incoming connections, data received, or timeouts, and responds by processing the data, updating the state, or sending a response.
3. **File I/O:** Event-driven programming can be used to handle file I/O operations, such as reading or writing files. The application listens for events like file open, read, write, or close, and performs the necessary actions in response to these events. This approach is particularly useful for handling large files or streaming data, as it allows the application to process data asynchronously without blocking the main thread.
4. **Message queues and event buses:** In distributed systems, event-driven programming can be applied to manage communication between services or components using message queues or event buses. Components can emit events and subscribe to events from other components, enabling asynchronous communication and loose coupling.
5. **Web servers:** Event-driven programming is often used in web servers to handle incoming HTTP requests and serve responses. The server listens for incoming requests and processes them by executing specific event handlers, such as serving static files, executing server-side scripts, or routing requests to the appropriate controllers.
6. **Real-time applications:** Event-driven programming can be used to build real-time applications, such as chat applications, online gaming, or live collaboration tools. These applications need to handle and process events such as incoming messages, user presence updates, or game state changes in real-time to provide a seamless user experience.
7. **IoT and embedded systems:** In IoT and embedded systems, event-driven programming can be used to handle sensor data, user inputs, or system events. The application responds to events such as button presses, temperature changes, or device status updates, and performs the necessary actions, like updating the device state, sending data to a server, or controlling other devices.

<hr>
We've now gone over what the definition of reactive programming & event-driven programming. Let's dive deeper into the reactive programming paradigm and run through some mental models.

<hr>

### Reactive Programming Paradigm
Reactive programming builds upon the event-driven paradigm and extends it to handle complex event compositions based on asynchronous data streams. Instead of directly responding to individual events, reactive programming introduces abstractions such as observables and operators that allow you to define event streams and apply transformations and compositions on them. This enables you to handle complex event interactions in a more manageable and declarative manner.

```JavaScript
//Let's consider a simple example of creating an observable and applying an operator to transform the emitted values:

// Import RxJS functions
import { of } from 'rxjs'
import { map } from 'rxjs/operators'

// Create an observable with some values
const sourceObservable = of(1, 2, 3, 4, 5)

// Apply the map operator to double each emitted value
const doubledObservable = sourceObservable.pipe(
  map((value) => value * 2)
)

// Subscribe to the doubled observable and handle the values
doubledObservable.subscribe((value) => {
  console.log('Doubled value:', value);
})

// In this example, we create an observable using the of function, which emits a sequence of values.
//We then apply the map operator to transform each emitted value by doubling it.
// Finally, we subscribe to the transformed observable and log the doubled values.
```

> 💡 The reactive programming paradigm is considered **declarative**. Take a look at the [declarative programming doc](declarative-programming.md) for further definitions & examples. 

<hr>
At the beginning of this doc, we ran through why reactive programming is significant to us as JS developers and even ran through some real-world examples of how we can use it in our applications. Let's just quickly review some of those benefits: 

### Reactive programming offers several benefits, including:

- **Declarative Approach:** Reactive programming allows you to express your code in a more declarative style, focusing on what should happen rather than explicitly defining the control flow. This leads to cleaner and more maintainable code.

- **Asynchronous and Non-Blocking:** Reactive programming enables you to handle asynchronous operations and non-blocking behavior more easily. By leveraging reactive streams and operators, you can handle event streams and data transformations without blocking the execution thread, leading to more responsive applications.

- **Event Composition and Reactivity:** Reactive programming provides powerful abstractions for composing events and reacting to changes in real-time. With operators and combinators, you can transform, filter, merge, and combine event streams to create complex interactions and dynamic behavior.

- **Error Handling and Resilience:** Reactive programming frameworks often provide mechanisms for handling errors and dealing with failure scenarios. By leveraging reactive operators, you can handle error propagation, retries, and fallback strategies in a more structured and controlled manner.

By understanding the fundamentals of reactive programming, you'll be better equipped to utilize the power of RxJS and TypeScript in building robust and responsive applications.
