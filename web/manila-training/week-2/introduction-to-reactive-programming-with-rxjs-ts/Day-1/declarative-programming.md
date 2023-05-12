# Declarative Programming
Declarative programming is a programming paradigm in which the programmer specifies what the program should accomplish, rather than how it should be accomplished. Declarative programming focuses on the desired outcome, rather than the specific steps needed to achieve that outcome. In contrast, imperative programming specifies the sequence of statements or commands that change the program's state to accomplish a task.

Below are some examples of declarative programming at work:

1. **React:** React is a popular JavaScript library for building user interfaces. It uses a declarative programming style where developers define components and their desired output based on the given input (props). React takes care of updating the UI efficiently when the input changes, abstracting away the underlying imperative DOM manipulation.

```JavaScript

function Welcome(props) {
  return <h1>Hello, {props.name}</h1>;
}

```

2. **Array methods (map, filter, reduce):** JavaScript provides several built-in array methods hat allow for declarative programming when working with arrays.

```JavaScript

const numbers = [1, 2, 3, 4, 5];
const doubled = numbers.map((num) => num * 2);
const even = numbers.filter((num) => num % 2 === 0);
const sum = numbers.reduce((acc, num) => acc + num, 0);

```

3. **GraphQL:** GraphQL is a query language for APIs that promotes a declarative approach to data fetching. Clients specify the data they need and the shape of the response, and the GraphQL server takes care of executing the necessary queries and returning the desired data.

```JavaScript

query {
  user(id: "1") {
    id
    name
    email
  }
}

```