import { render, screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from '../App';

describe('MyForm', () => {
  it('should render errors when any of the inputs are blank', () => {
    render(<App />);
    const submitBtn = screen.getByTestId('submit-button');
    fireEvent.click(submitBtn);
    
    // we declare here since error elements DO NOT render until there's an error
    const nameErr = screen.getByTestId('name-error');
    const emailErr = screen.getByTestId('email-error');
    expect(nameErr).toBeInTheDocument();
    expect(emailErr).toBeInTheDocument();
  });
  
  it('should validate the email input', async () => {
    render(<App />);
    const user = userEvent.setup();
    const emailInput = screen.getByTestId('email-input');
    const submitBtn = screen.getByTestId('submit-button');
    
    await user.type(emailInput, 'test@email');
    await user.click(submitBtn);
    expect(emailInput).toHaveValue('test@email');
    expect(screen.getByText('This field is invalid')).toBeInTheDocument();
  });
});
