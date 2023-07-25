package com.example.module2.state_machines;

import java.util.Random;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class FaultyDeterministicStateMachine
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(FaultyDeterministicStateMachine.class);
    private static final Random RANDOM = new Random();
    private final double errorLikelihood;

    public FaultyDeterministicStateMachine(final double errorLikelihood)
    {
        this.errorLikelihood = errorLikelihood;
    }

    public State processInput(final State currentState)
    {
        switch (currentState)
        {
            case START:
                LOGGER.info("Currently in START state").log();
                return getNextState(State.STATE1, State.STATE2);
            case STATE1:
                LOGGER.info("Currently in STATE1 state").log();
                return getNextState(State.STATE2);
            case STATE2:
                LOGGER.info("Currently in STATE2 state").log();
                return getNextState(State.END);
            default:
                throw new IllegalArgumentException("Invalid state: " + currentState);
        }
    }

    private State getNextState(final State... nextStates)
    {

        if (RANDOM.nextDouble() > errorLikelihood &&
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
