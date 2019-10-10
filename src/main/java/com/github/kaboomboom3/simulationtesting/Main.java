package com.github.kaboomboom3.simulationtesting;

import com.github.kaboomboom3.simulationtesting.arm.ArmSimulation;
import com.github.kaboomboom3.simulationtesting.elevator.ElevatorSimulation;
import org.apache.commons.cli.*;

import java.util.TreeMap;
import java.util.function.Supplier;

public class Main {
    private static final TreeMap<String, Supplier<Runnable>> AVAILABLE_SIMULATIONS = new TreeMap<>();

    static {
        AVAILABLE_SIMULATIONS.put("arm", ArmSimulation::new);
        AVAILABLE_SIMULATIONS.put("elevator", ElevatorSimulation::new);
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option simulation = new Option("simulation", true, "The simulation to run");
        simulation.setRequired(true);
        options.addOption(simulation);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("simulationtesting", options);

            System.exit(1);
        }

        String simulationName = cmd.getOptionValue("simulation");

        if (simulationName == null || !AVAILABLE_SIMULATIONS.containsKey(simulationName)) {
            System.err.printf("Unknown simulation %s%n", simulationName);
            System.err.printf("Available simulations:%n");
            System.err.println(String.join("\n", AVAILABLE_SIMULATIONS.keySet()));

            System.exit(1);
        }

        AVAILABLE_SIMULATIONS.get(simulationName).get().run();
    }
}
