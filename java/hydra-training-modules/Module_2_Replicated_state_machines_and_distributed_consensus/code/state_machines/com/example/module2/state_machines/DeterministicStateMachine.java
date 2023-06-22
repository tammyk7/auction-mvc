package com.example.module2.state_machines;

public class DeterministicStateMachine
{
    public State processInput(State currentState) {
        switch (currentState) {
            case START:
                System.out.println("Currently in START state");
                return State.STATE1;
            case STATE1:
                System.out.println("Currently in STATE1 state");
                return State.STATE2;
            case STATE2:
                System.out.println("Currently in STATE2 state");
                return State.END;
            default:
                throw new IllegalArgumentException("Invalid state: " + currentState);
        }
    }
}
