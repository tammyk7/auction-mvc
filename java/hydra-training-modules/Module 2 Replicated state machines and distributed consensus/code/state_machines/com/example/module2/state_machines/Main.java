package com.example.module2.state_machines;

public class Main {
    public static void main(String[] args) {

        // Deterministic State Machine
        System.out.println("Starting deterministic state machine.");
        DeterministicStateMachine deterministicStateMachine = new DeterministicStateMachine();
        State deterministicState = State.START;
        while (deterministicState != State.END) {
            deterministicState = deterministicStateMachine.processInput(deterministicState);
        }
        System.out.println("Reached END state in the deterministic state machine.");

        System.out.println();

        // FaultyDeterministic State Machine
        System.out.println("Starting faulty deterministic state machine.");
        FaultyDeterministicStateMachine nonDeterministicStateMachine = new FaultyDeterministicStateMachine(0.5d);
        State nonDeterministicState = State.START;
        while (nonDeterministicState != State.END) {
            nonDeterministicState = nonDeterministicStateMachine.processInput(nonDeterministicState);
        }
        System.out.println("Reached END state in the non-deterministic state machine.");
    }
}

