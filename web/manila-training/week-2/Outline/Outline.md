## Observable - how does it work?
  ### Array vs Stream
  ### Observable, Subscription, Observer - Key Elements
  ### Marble Diagrams & Notification Types

## Observable, Observer and Subscription - code snippets and exercises
  ### Execution Timing - Empty Observable
  ### Synchronous Emission - Next Notification
  ### Asynchronous Emission - More Next Notifications
  ### Teardown - Complete Notification
  ### Error Notification
  ### Cancellation - Unsubscribe

### Types of Observables
  ### Hot Observable
  ### Cold Observable
  ### Hot vs Cold

### Creation Functions
  ### of
  ### from
  ### fromEvent
  ### timer
  ### interval
  ### forkJoin - Handle multiple HTTP
  ### forkJoin - Error Scenario
  ### combineLatest - Reacting to multiple input changes

### Pipeable Operators
  ### Operator Stacking
  ### Importing Operators
  ### filter
  ### map
  ### tap
  ### debounceTime
  ### catchError
  ### Flattening Operators
  ### Flattening Operators - Static Example
  ### Flattening Operators - Dynamic HTTP Request
  ### Flattening Operators - Error Handling (First Solution)
  ### Flattening Operators - Error Handling (Second Solution)
  ### Flattening Operators - Concurrency - concatMap
  ### Flattening Operators - switchMap
  ### Flattening Operators - mergeMap
  ### Flattening Operators - Side by Side Comparison

### Subjects
  ### Multicasting
  ### Subject vs Observable vs Observer
  ### Subject in Action
  ### BehaviorSubject - Concept
  ### BehaviorSubject in Action