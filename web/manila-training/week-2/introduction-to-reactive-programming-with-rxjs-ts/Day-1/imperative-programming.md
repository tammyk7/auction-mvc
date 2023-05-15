# Imperative Programming
Imperative programming is a programming paradigm in which the flow of the program is determined by a sequence of statements or commands that change the program's state. In imperative programming, the programmer specifies how the program should accomplish a task, rather than specifying what the program should accomplish. Imperative programming is often contrasted with declarative programming, which focuses on what the program should accomplish, rather than how it should be accomplished.


See below for a few examples of imperative programming so you will be aware in the future of what it looks like.


1. **jQuery:** jQuery is a popular JavaScript library that simplifies DOM manipulation and event handling using an imperative programming style. Developers write step-by-step instructions to manipulate the DOM and respond to events.

```JavaScript

$("#button").click(function() {
  $("p").text("Hello, World!");
});

```

2. **For loops:** The classic **`for`** loop in JavaScript is an example of imperative programming, where developers provide explicit instructions for iterating over an array or collection.

```JavaScript

const numbers = [1, 2, 3, 4, 5];
let doubled = [];

for (let i = 0; i < numbers.length; i++) {
  doubled.push(numbers[i] * 2);
}

```

3. **XMLHttpRequest:** Using the XMLHttpRequest API to make AJAX requests is an example of imperative programming. Developers specify the steps required to create, open, send, and process an HTTP request.

```JavaScript

const xhr = new XMLHttpRequest();
xhr.onreadystatechange = function() {
  if (xhr.readyState === 4 && xhr.status === 200) {
    console.log(xhr.responseText);
  }
};
xhr.open("GET", "https://api.example.com/data", true);
xhr.send();

```