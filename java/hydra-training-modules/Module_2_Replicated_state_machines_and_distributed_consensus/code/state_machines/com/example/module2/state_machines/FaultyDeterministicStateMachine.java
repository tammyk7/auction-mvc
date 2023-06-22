package com.example.module2.state_machines;

import java.util.Random;

public class FaultyDeterministicStateMachine
{
    private static final Random random = new Random();
    private final double errorLikelihood;

    public FaultyDeterministicStateMachine(final double errorLikelihood)
    {
        this.errorLikelihood = errorLikelihood;
    }

    public State processInput(State currentState)
    {
        switch (currentState)
        {
            case START:
                System.out.println("Currently in START state");
                return getNextState(State.STATE1, State.STATE2);
            case STATE1:
                System.out.println("Currently in STATE1 state");
                return getNextState(State.STATE2);
            case STATE2:
                System.out.println("Currently in STATE2 state");
                return getNextState(State.END);
            default:
                throw new IllegalArgumentException("Invalid state: " + currentState);
        }
    }

    private State getNextState(State... nextStates)
    {

        if (random.nextDouble() > errorLikelihood &&
            nextStates.length > 1)
        {
            return nextStates[1];
        }
        else
        {
            return nextStates[0];
        }
    }
}
