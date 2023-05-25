# Introduction to TypeScript types for React Hooks

Lets examine how React hooks work with TypeScript.

Prior to hooks, React components used to have two flavors:
- **Classes** that handle a state
- **Functions** that are fully defined by their props

A natural use of these was to build complex container components with classes and simple _presentational_ components with pure functions.

### What are React Hooks?

Container components handle state management and requests to the server, which will be then called in this article _side effects_. The state will be propagated to the container children through the props.

But as the code grows, _functional components tend to be transformed as container components_.

Upgrading a functional component into a smarter one is not that painful, but it is a time-consuming and unpleasant task. Moreover, distinguishing presenters and containers _very strictly_ is not welcome anymore.

**Hooks can do both**, so the resulting code is more uniform and has almost all the benefits. Here is an example of adding a local state to a small component that is handling a quotation signature.

```jsx
// put signature in local state and toggle signature when signed changes
function QuotationSignature({quotation}) {
  const [signed, setSigned] = useState(quotation.signed);
  useEffect(() => {
    fetchPost(`quotation/${quotation.number}/sign`)
  }, [signed]); // effect will be fired when signed changes

  return <>
    <input type="checkbox" checked={signed} onChange={() => {setSigned(!signed)}}/>
    Signature
  </>
}
```

There is a big bonus to this —- coding with _TypeScript_ was great with Angular but bloated with React. However, coding _React hooks_ with _TypeScript_ is a pleasant experience.

#### TypeScript with Old React

Writing React classes with naive TypeScript was quite painful because developers had to type both props and state even though many keys were the same.

Here is a simple domain object. We make a quotation app, with a `Quotation` type, managed in some crud components with state and props. The `Quotation` can be created, and its associated status can change to signed or not.

```tsx
interface Quotation{
  id: number
  title:string;
  lines:QuotationLine[]
  price: number
}

interface QuotationState{
  readonly quotation:Quotation;
  signed: boolean
}

interface QuotationProps{
  quotation:Quotation;
}

class QuotationPage extends Component<QuotationProps, QuotationState> {
	// ...
}
```

But imagine the QuotationPage will now ask the server for an **id**: for example, the 678th quotation made by the company. Well, that means the QuotationProps does not know that important number — it’s not wrapping a Quotation **exactly**. We have to declare much more code in the QuotationProps interface:

```tsx
interface QuotationProps{
  // ... all the attributes of Quotation but id
  title:string;
  lines:QuotationLine[]
  price: number
}
```

We copy all attributes but the id in a new type. Hm. That makes me think of old Java, which involved writing a bunch of DTOs. To overcome that, we will increase our TypeScript knowledge to bypass the pain.

#### Benefits of TypeScript with Hooks

By using hooks, we will be able to get rid of the previous QuotationState interface. To do this, we will split QuotationState into two different parts of the state.

```tsx
interface QuotationProps{
  quotation:Quotation;
}
function QuotationPage({quotation}:QuotationProps){
  const [quotation, setQuotation] = useState(quotation);
  const [signed, setSigned] = useState(false);
}
```

By splitting the state, we don’t have to create new interfaces. Local state types are often inferred by the default state values.

Components with hooks are all functions. So, we can write the same component returning the `FC<P>` type defined in the React library. The function explicitly declares its return type, setting along the props type.

```tsx
const QuotationPage: FC<QuotationProps> = ({quotation}) => {
  const [quotation, setQuotation] = useState(quotation);
  const [signed, setSigned] = useState(false);
}
```

Evidently, using TypeScript with React hooks is easier than using it with React classes. And because strong typing is a valuable security for code safety, you should consider using TypeScript if your new project uses hooks. You should definitely use hooks if you want some TypeScript.

There are numerous reasons why you could avoid TypeScript, using React or not. But if you do choose to use it, you should definitely use hooks, too.

#### Specific Features of TypeScript Suitable for Hooks

In the previous React hooks TypeScript example, I still have the number attribute in the QuotationProps, but there is yet no clue of what that number actually is.

TypeScript gives us a long list of [utility types](https://www.typescriptlang.org/docs/handbook/utility-types.html), and three of them will help us with React by reducing the noise of many interface descriptions.

- `Partial<T>`: any sub keys of `T`
- `Omit<T, 'x'>`: all keys of `T` except the key `x`
- `Pick<T, 'x', 'y', 'z'>`: exactly `x, y, z` from `T`

In our case, we would like `Omit<Quotation, 'id'>` to omit the id of the quotation. We can create a new type on the fly with the `type` keyword.

`Partial<T>` and `Omit<T>` does not exist in most typed languages such as Java but helps a lot for examples with Forms in front-end development. It simplifies the burden of typing.

```tsx
type QuotationProps= Omit<Quotation, id>;
function QuotationPage({quotation}:QuotationProps){
  const [quotation, setQuotation] = useState(quotation);
  const [signed, setSigned] = useState(false);
  // ...
}
```

Now we have a Quotation with no id. So, maybe we could design a `Quotation` and `PersistedQuotation extends Quotation`. Also, we will easily resolve some recurrent `if` or `undefined` problems. Should we still call the variable quotation, though it’s not the full object? That’s beyond the scope of this article, but we will mention it later on anyway.

However, now we are sure that we will not spread an object we thought had a `number`. Using `Partial<T>` does not bring all these guarantees, so use it with discretion.

`Pick<T, ‘x’|’y’>` is another way to declare a type on the fly without the burden of having to declare a new interface. If a component, simply edit the Quotation title:

```tsx
type QuoteEditFormProps= Pick<Quotation, 'id'|'title'>
```

Or just:

```tsx
function QuotationNameEditor({id, title}:Pick<Quotation, 'id'|'title'>){ ...}
```

#### Other Benefits of React Hooks

The React team always viewed and treated React as a functional framework. They used classes so that a component could handle its own state, and now hooks as a technique allowing a function to keep track of the component state.

```tsx
interface Place{
  city:string,
  country:string
}
const initialState:Place = {
  city: 'Rosebud',
  country: 'USA'
};
function reducer(state:Place, action):Partial<Place> {
  switch (action.type) {
    case 'city':
      return { city: action.payload };
    case 'country':
      return { country: action.payload };
  }
}
function PlaceForm() {
  const [state, dispatch] = useReducer(reducer, initialState);
  
  return (
    <form>
      <input type="text" name="city"  onChange={(event) => {
          dispatch({ type: 'city',payload: event.target.value})
        }} 
        value={state.city} />
      <input  type="text"  name="country"   onChange={(event) => {
          dispatch({type: 'country', payload: event.target.value })
        }}
 
        value={state.country} />
   </form>
  );
}
```

Here is a case where using `Partial` is safe and a good choice.

Though a function can be executed numerous times, the associated `useReducer` hook will be created only once.

By _naturally_ extracting the reducer function out of the component, the code can be divided into multiple independent functions instead of multiple functions inside a class, all linked to the state inside the class.

This is clearly better for testability — some functions deal with JSX, others with behavior, others with business logic, and so on.

You (almost) don’t need Higher Order Components anymore. The Render props pattern is easier to write with functions.

So, reading the code is easier. Your code is not a flow of classes/functions/patterns but a flow of functions. However, because your functions are not attached to an object, it may be difficult to name all these functions.

#### TypeScript Is Still JavaScript

JavaScript is fun because you can tear your code in any direction. With TypeScript, you can still use `keyof` to play with the keys of objects. You can use type unions to create something unreadable and unmaintainable - no, I don’t like those. You can use type alias to pretend a string is a UUID.

But you can do it with null safety. Be sure your `tsconfig.json` has the `"strict": true` option. Check it before the start of the project, or you will have to refactor almost every line!

There are debates on the level of typing you put in your code. You can type everything or let the compiler infer the types. It depends on the linter configuration and team choices.

Also, you can still make runtime errors! TypeScript is simpler than Java and avoids covariance/contravariance troubles with Generics.

In this Animal/Cat example, we have an Animal list that is identical to the Cat list. Unfortunately, it’s a contract in the first line, not the second. Then, we add a duck to the Animal list, so the list of Cat is false.

```tsx
interface Animal {}
 
interface Cat extends Animal {
  meow: () => string;
}
 
const duck = { age: 7 };
const felix = {
  age: 12,
  meow: () => "Meow"
};
 
const listOfAnimals: Animal[] = [duck];
const listOfCats: Cat[] = [felix];
 
 
function MyApp() {
  const [cats , setCats] = useState<Cat[]>(listOfCats);
  // Here the thing:  listOfCats is declared as a Animal[]
  const [animals , setAnimals] = useState<Animal[]>(listOfCats)
  const [animal , setAnimal] = useState(duck)
 
  return (
    <div onClick={() => {
      animals.unshift(animal) // we set as first cat a duck !
      setAnimals([...animals]) // dirty forceUpdate
    }}>
      The first cat says {cats[0].meow()}
    </div>
  );
}
```

TypeScript has only a _bivariant_ approach for generics that is simple and helps JavaScript developer adoption. If you name your variables correctly, you will rarely add a `duck` to a `listOfCats`.