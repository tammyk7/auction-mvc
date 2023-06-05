### Challenge 2: User Profile Component

Create a UserProfile component that displays a user's name and email. The component should subscribe to a `user$` Observable that emits user data. Use RxJS to handle the subscription to the `user$` Observable.

Use the github api for users to grab information and display it.

```js
https://api.github.com/users/{username}
```

Here's a skeleton to get you started:

```jsx
import { useEffect, useState, FC } from 'react';
import { of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

interface User {
  name: string;
  email: string;
}

const user$ = of<User>({
  name: 'John Doe',
  email: 'john.doe@example.com'
});

const UserProfileComponent: FC = () => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const subscription = user$.subscribe(setUser);
    return () => subscription.unsubscribe();
  }, []);

  return user ? (
    <div>
      <p>Name: {user.name}</p>
      <p>Email: {user.email}</p>
    </div>
  ) : null;
}

```

To run this application:
1. `cd challenge-1`
2. `npm i`
3. `npm run dev`


Validate your solution by running it against the test suite:
`npm run test`
