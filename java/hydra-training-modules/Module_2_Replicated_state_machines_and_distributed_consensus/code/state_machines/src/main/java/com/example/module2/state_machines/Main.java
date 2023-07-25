package com.example.module2.state_machines;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class Main
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(Main.class);
    public static void main(final String[] args)
    {

        // Deterministic State Machine
        LOGGER.info("Starting deterministic state machine.").log();
        final DeterministicStateMachine deterministicStateMachine = new DeterministicStateMachine();
        State deterministicState = State.START;
        while (deterministicState != State.END)
        {
            deterministicState = deterministicStateMachine.processInput(deterministicState);
        }
        LOGGER.info("Reached END state in the deterministic state machine.").log();

        // FaultyDeterministic State Machine
        LOGGER.info("Starting faulty deterministic state machine.").log();
        final FaultyDeterministicStateMachine nonDeterministicStateMachine = new FaultyDeterministicStateMachine(0.5d);
        State nonDeterministicState = State.START;
        while (nonDeterministicState != State.END)
        {
            nonDeterministicState = nonDeterministicStateMachine.processInput(nonDeterministicState);
        }
        LOGGER.info("Reached END state in the non-deterministic state machine.").log();
    }
}
