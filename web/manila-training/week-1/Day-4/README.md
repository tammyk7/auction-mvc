### Challenge: Form with useValidation hook

The goal of this challenge is to complete the implementation of a form and a custom `useValidation` hook. Parts of the form and the hook have been implemented as part of the skeleton of this challenge. Write your code in parts where places are commented as `TODO` and wherever you see fit.

Note: when the user submits the form, the inputs should be validated to ensure that both fields are filled out and that the email is in a valid format (a `RegExp` pattern is provided). If the inputs are **valid**, the inputs should be logged in the console and shown in a `window.alert` dialog box. If the inputs are **invalid**, an error message should be displayed next to their respective inputs.


#### Summarized Requirements: 
- The form should have two input fields: a name field and an email field.
- When the user submits the form, the inputs should be validated to ensure that both fields are filled out and that the email is in a valid format.
- If the inputs are **valid**, the inputs should be logged in console and shown in a `window.alert` dialog box. If the inputs are **invalid**, an error message should be displayed.
- You should use the `useValidation` custom hook to manage the form state and validation logic.
- You should use TypeScript to define types for the form inputs.
