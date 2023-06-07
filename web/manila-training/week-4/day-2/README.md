# Week 4 Day 2: React Router and Code Splitting

**\* In this write up we are using React Router v6.**

## Exploring React Router library for navigation in React apps

React is one of the most popular libraries for creating single page applications (SPAs), but sometimes we need more than a SPA to fulfill the requirements. In the case when we need to create a React app that allows for navigation to different pages, we need to add a tool to enable that. That is where we can utilize the [React Router](https://reactrouter.com/en/main) library. This library was created to add the functionality of navigation within React apps by enabling developers to create routes that load specific components based on a user's request without reloading the webpage.

In traditional routing the client will request the server to provide different `index.html` files of different pages. But with a React app with Client Side Rendering, we will only have one `index.html` file and use Client Side Routing. This will provide the user with the ability to use forward and backward navigation as well as updating the URL but not actually reloading the page while navigating.

### **Install React Router**

To add routing to a React app we just have to install the React router package. Within the project directory run the following command in the terminal.

Using npm:

```bash
npm i react-router-dom
```

Using yarn:

```bash
yarn add react-router-dom
```

### **Setup React Router**

To set up React Router we have to navigate to the applications root file, if using Vite this file will be named `main.tsx` and located within the src directory. We then need to import the `BrowserRouter` from the `react-router-dom` package and wrap it around our `<App>` component.

```tsx
// main.tsx
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'

import App from './App.tsx'
import './index.css'

const root = createRoot(document.getElementById('root') as HTMLElement)

root.render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
)
```

### **Configure Routes**

Next we can implement routing by configuring all of our routes (pages/components) that we want to be able to navigate to.

To do this we have to first create components for each page we want. For example if we were creating an application for a store we might want to have a Home page, an About page, and a Products page. Once the components are finished we can configure the routes to each within our `<App>` component.

```tsx
// App.tsx
import { Routes, Route } from 'react-router-dom'

import Home from './Pages/Home'
import About from './Pages/About'
import Products from './Pages/Products'

const App: FC = (): JSX.Element => {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/products" element={<Products />} />
      </Routes>
    </>
  )
}

export default App
```

We import `Routes` and `Route` from `react-router-dom` and use them to define the routes we want. We have to wrap all of the routes in the `<Routes>` component. Then we define each route with a `<Route>` and pass two properties. `path` which identifies the path we want to reach the component, and `element` which contains the component that we want the path to load. So now when we add `/about` to the url we will see the `<About>` component render.

### **Access Configured Routes with Links**

Now we know how to manually access routes by changing the url, but users should not have to navigate this way. Let's add some links in our app. To do this we need to import the `Link` component from `react-router-dom`. This component is just a wrapper around an anchor tag that we would normally use when linking in html, but adds functionality to make sure all the routing and re-rendering is handled properly. Now we can create a `<NavBar>` component that will be added to our `App.tsx` so users use it to navigate our app from any route.

```tsx
// NavBar.tsx
import { Link } from 'react-router-dom'

const NavBar: FC = (): JSX.Element => {
  return (
    <nav>
      <ul>
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/about">About</Link>
        </li>
        <li>
          <Link to="/products">Products</Link>
        </li>
      </ul>
    </nav>
  )
}

export default NavBar
```

Here we create links for all the pages we set up routes for and use the `to` prop of the `<Link>` component to set the path we specified when configuring the routes. This `to` prop works just like an `href` would for an `<a>` element. Now let's add the navbar to our `App.tsx`

```tsx
// App.tsx
import { Routes, Route } from 'react-router-dom'

import Home from './Pages/Home'
import About from './Pages/About'
import Products from './Pages/Products'
import NavBar from './Components/NavBar'

const App: FC = (): JSX.Element => {
  return (
    <>
      <NavBar />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/products" element={<Products />} />
      </Routes>
    </>
  )
}

export default App
```

### **Advanced Route Definitions**

We just touched the surface of what React Router is capable of when it comes to routing. There are more complex and more functional ways to route including:

1. [Dynamic Routing](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=useRoutes%20Hook-,Dynamic%20Routing,-The%20simplest%20and)
2. [Routing Priority](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=Book%203.-,Routing%20Priority,-When%20we%20were)
3. [Nested Routes](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=have%20also%20matched.-,Nested%20Routes,-Finally%2C%20we%20have)
4. [Multiple Routes](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=for%20this%20context.-,Multiple%20Routes,-Another%20incredibly%20powerful)
5. [`useRoutes` Hook](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=the%20NotFound%20component.-,useRoutes%20Hook,-The%20final%20thing)

Take a look at this [Utlimate React Router v6 Guide](https://blog.webdevsimplified.com/2022-07/react-router/#:~:text=the%20URL%20changes.-,Advanced%20Route%20Definitions,-This%20is%20where) to learn more about these advanced features, or click on the links in the list above to navigate directly to each topic.

<hr>

## Utilizing code splitting for optimizing bundle size

### **Bundling**

When we developers create applications it is typical to break up the app into modules, components, and functions that we export and import between files to build larger pieces of the app. This creates a complex web of files needed to build the application.

Most React apps we build will have their files "bundled" by tools such as [esbuild](https://esbuild.github.io/) or [webpack](https://webpack.js.org/). Bundling is a process where imported files are merged into a single file. This single file, or "bundle", is then included on a webpage to load the entire app at once with the goal of reducing the number of requests for files when a user visits the web page.

![Bundle Image](../images/bundling.png)

Bundling is great for reducing the network requests needed when loading a page, but as the app grows, so will the bundle. If the bundle becomes too large it can cause the app to take a long time to load and result in a bad user experience. To avoid large bundle files we can split our bundle using "code splitting".

### **Code Splitting**

Code Splitting is a feature supported by bundlers which can create multiple bundles that can be dynamically loaded at runtime. By splitting your app into smaller "chunks", the browser will only load the code needed by the user for the initial render of the app, which can dramatically improve its performance.

![Code Splitting Image](../images/code-splitting.png)

This does not reduce the overall amount of code in the application, we are now just not loading code that a user may never need. The application will then load the smaller chunks on demand or in parallel, as needed as the user navigates through the application.

One way to introduce code splitting into our app is through dynamic imports. Traditionally we import modules from other files like this:

```js
import { add } from './math'

console.log(add(16, 26))
```

But we can dynamically import to get the same results:

```js
import('./math').then((math) => {
  console.log(math.add(16, 26))
})
```

By using import like a function that returns a Promise we can have it resolve with the module once the module is completely loaded.

<hr>

## Implementing dynamic imports and lazy loading with React Router

When creating a React app utilizing React Router can easily take advantage of code splitting to reduce the bundle size since the user will only need the code to load the initial page. We can then load the code for the additional page as the user accesses them through the routes.

Lets modify our `App.tsx` file from earlier to take advantage of code splitting. We've already seen how to use `lazy()` provided by the react library, this function invokes a dynamic import which returns a React component. Let's use this to add code splitting to our application.

Lets replace:

```tsx
// App.tsx
...

import Home from './Pages/Home'
import About from './Pages/About'
import Products from './Pages/Products'

...
```

with:

```tsx
// App.tsx
...

import { lazy } from 'react'

const Home = lazy(() => import ('./Pages/Home'))
const About = lazy(() => import ('./Pages/About'))
const Products = lazy(() => import ('./Pages/Products'))

...
```

Now that we have added our lazy loaded components we have to remember that these dynamic imports are asynchronous. This means that there will be an unspecified amount of time before the components will be loaded, rendered, and displayed to the user, so we are going to have to tell React how to handle this. We can add `Suspense` to help us with this. With Suspense we can give it multiple lazily loaded components, let's take a look at our new `App.tsx` file.

```tsx
// App.tsx
import { lazy, Suspense } from 'react'
import { Routes, Route } from 'react-router-dom'

import NavBar from './Components/NavBar'

const Home = lazy(() => import('./Pages/Home'))
const About = lazy(() => import('./Pages/About'))
const Products = lazy(() => import('./Pages/Products'))

const App: FC = (): JSX.Element => {
  return (
    <>
      <NavBar />

      <Suspense fallback={<h1>Loading...</h1>}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/products" element={<Products />} />
        </Routes>
      </Suspense>
    </>
  )
}

export default App
```

Now when we run the `npm run build` command in our terminal we can see our app is split into three chunks because we have included three dynamic imports by using `lazy()` three times.

![Code Splitting Build Size Image](../images/split-build.png)

## Resources

- https://hygraph.com/blog/routing-in-react
- https://blog.webdevsimplified.com/2022-07/react-router/
