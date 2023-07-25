package com.example.module2.state_machines;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class DeterministicStateMachine
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(DeterministicStateMachine.class);
    public State processInput(final State currentState)
    {
        switch (currentState)
        {
            case START:
                LOGGER.info("Currently in START state").log();
                return State.STATE1;
            case STATE1:
                LOGGER.info("Currently in STATE1 state").log();
                return State.STATE2;
            case STATE2:
                LOGGER.info("Currently in STATE2 state").log();
                return State.END;
            default:
                throw new IllegalArgumentException("Invalid state: " + currentState);
        }
    }
}
