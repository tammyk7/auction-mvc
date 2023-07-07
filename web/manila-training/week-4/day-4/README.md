# Week 4 Day 4: Code Challenge - Dynamic Routing, Data Fetching, and Error Handling

**Today you will be building a React application that implements dynamic routing, data fetching, code splitting, lazy loading, error boundaries, and error handling with RxJS observables and react-rxjs.**

## Part 1 - Dynamic Routing, Data Fetching, Code Splitting, & Lazy Loading

Create a Game of Throne characters application using React. React Router, & Data Fetching.

The application should have two pages:

1. A Character list page
2. A Character detail page

The Character list page will make a network call to the [Game of Thrones Character API](https://thronesapi.com/swagger/index.html?urls.primaryName=Game%20of%20Thrones%20API%20v2) `Characters` endpoint to get a list of character data.

The Character object will look like this:

```tsx
 Character {
  id: number
  firstName: string
  lastName: string
  fullName: string
  title: string
  family: string
  image: string
  imageUrl: string
}
```

Use this data to render a list of character names on the page. Clicking on a character's name will route you to the `/characters/[id]` route, where `[id]` is the id of the character clicked.

The Character detail page will make a network call to the Game of Thrones Character API `Characters/{id}` endpoint to get data for the character with the id in the route params.

Use this data to render an image, name, title, and family for the character. There will also be a back button on this page that should route you back to the list page.

We have added some starter code for you to build off of. The pages are located in `./challenge-1/src/components`, add your service calls the the `./challenge-1/src/services/thronesApi.ts` file. We also included styling, the styles should automatically apply but feel free to edit as needed (`./challenge-1/src/App.css`).

Make sure to add lazy loading & TypeScript types where possible.

To get started, run this application using:

1. `cd challenge-1`
2. `npm i`
3. `npm run dev`

<hr>

## Part 2 - RxJS & React-RxJS

Add RxJS & React-RxJS for handling all state managment and network requests.

**HINT:** We used a custom hook to make the Character data service call using the character id URL parameter.

<hr>

## Part 3 - Error Boundaries & Error Handling

Now to wrap up this challenge you are going to add Error Boundaries & Error Handling. Create an ErrorBoundary Component that will render an error state UI that displays the error message.

You should also add error handling for navigation to a character page with an ID that does not exist.

e.g.: `[baseURL]/character/100` will render a message that says "Character ID does not exsit"

<hr>

Once you have completed this, verify your solution by checking ours located in `./challenge-1/solution/src`
