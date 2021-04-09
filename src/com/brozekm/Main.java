package com.brozekm;

import java.io.*;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Main {

    /**
     * Parsed from argument
     * Input file
     */
    private static File inputFile;

    /**
     * Parsed from argument
     * Output file
     */
    private static File outputFile;

    /**
     * Parsed from argument
     * Number of workers
     */
    private static int cWorker;

    /**
     * Parsed from argument
     * Maximum time for worker to mine one resource
     */
    private static int tWorker;

    /**
     * Parsed from argument
     * Lorry capacity [resources]
     */
    private static int capLorry;

    /**
     * Parsed from argument
     * Maximum time for lorry to reach destination
     */
    private static int tLorry;

    /**
     * Parsed from argument
     * Ferry capacity [lorries]
     */
    private static int capFerry;


    public static void main(String[] args) {
        if (validateArguments(args)) {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new PrintWriter(outputFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            Ferry ferry = new Ferry(capFerry, bw);
            printArguments();
            CurrentLorry cLorry = new CurrentLorry(new Lorry(1, capLorry, tLorry, ferry, bw));

            Headman headman;
            try {
                headman = new Headman(inputFile, cWorker, cLorry, ferry, bw);
            } catch (IOException e) {
                System.out.println("Error occurred while reading from input file");
                return;
            }

            if (!canSimulationRun(headman.getBlocks())) {
                System.out.println("Simulation data are not correct, simulation would never end.");
                System.out.println("Last ferry would not be able to sail away");
                return;
            }
            System.out.println("------- Simulation -------");

            Thread thread;
            for (int workerId = 1; workerId <= cWorker; workerId++) {
                Worker worker = new Worker(workerId, tWorker, cLorry, headman, bw);
                thread = new Thread(worker);
                thread.start();
            }
        }
    }

    /**
     * Checks if mining simulation would be able to end
     * (Number of lorries that are needed must be after dividing with lorrie capacity without reminder)
     *
     * @param blocks
     * @return
     */
    private static boolean canSimulationRun(Queue<Block> blocks) {
        int resourcesCount = 0;
        for (Block b : blocks) {
            resourcesCount += b.getResources();
        }
        int lorryCount = resourcesCount / capLorry;
        if (resourcesCount % capLorry != 0) {
            lorryCount++;
        }
        if (lorryCount % capFerry == 0) {
//            System.out.println("Lorry count: "+ lorryCount);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates programs arguments
     *
     * @param args arguments
     * @return true if all arguments are correct
     */
    private static boolean validateArguments(String[] args) {
        if (args.length == 14) {
            for (int i = 0; i < args.length - 1; i++) {
                switch (args[i]) {
                    case "-i":
                        inputFile = new File(args[++i]);
                        if (!inputFile.exists()) {
                            System.out.println("Input file (" + args[i] + ") was not found");
                            return false;
                        }
                        break;
                    case "-o":
                        outputFile = new File(args[++i]);
                        try {
                            if (!outputFile.createNewFile()) {
                                System.out.println("File with name " + args[i] + " (output file) already exists and will be overwritten");
                            }
                            break;
                        } catch (IOException e) {
                            System.out.println("Error occurred while creating output file (" + args[i] + ")");
                            return false;
                        }
                    case "-cWorker":
                        try {
                            cWorker = Integer.parseInt(args[++i]);
                            if (cWorker < 1) {
                                System.out.println("There has to be at least one worker");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(args[i - 1] + " expects int as parameter. (" + args[i] + " was used instead)");
                            return false;
                        }
                        break;
                    case "-tWorker":
                        try {
                            tWorker = Integer.parseInt(args[++i]);
                            if (tWorker < 1) {
                                System.out.println("Worker's maximum time for mining one resource has to be 1 or higer");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(args[i - 1] + " expects int as parameter. (" + args[i] + " was used instead)");
                            return false;
                        }
                        break;
                    case "-capLorry":
                        try {
                            capLorry = Integer.parseInt(args[++i]);
                            if (capLorry < 1) {
                                System.out.println("Lorry capacity has to be set to 1 or higher");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(args[i - 1] + " expects int as parameter. (" + args[i] + " was used instead)");
                            return false;
                        }
                        break;
                    case "-tLorry":
                        try {
                            tLorry = Integer.parseInt(args[++i]);
                            if (tLorry < 1) {
                                System.out.println("Lorry maximum time for reaching destination has to be 1 or higher");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(args[i - 1] + " expects int as parameter. (" + args[i] + " was used instead)");
                            return false;
                        }
                        break;
                    case "-capFerry":
                        try {
                            capFerry = Integer.parseInt(args[++i]);
                            if (capFerry < 1) {
                                System.out.println("Ferry capacity has to be set to 1 or higher");
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(args[i - 1] + " expects int as parameter. (" + args[i] + " was used instead)");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("Unknown parameter: " + args[i]);
                        return false;
                }

            }
            return true;
        } else {
            System.out.println("Wrong number of parameters was set");
            System.out.println("java -jar <runnable_jar> -i <input_file> -o <output_file> -cWorker <int> -tWorker <int> -capLorry <int> -tLorry <int> -capFerry <int>");
            return false;
        }
    }

    private static void printArguments() {
        System.out.println("------- Arguments -------");
        System.out.println("Input file: " + inputFile.toString());
        System.out.println("Output file: " + outputFile.getName());
        System.out.println("Worker count: " + cWorker);
        System.out.println("Worker maximum mining time: " + tWorker);
        System.out.println("Lorry capacity: " + capLorry);
        System.out.println("Lorry maximum time for reaching destination: " + tLorry);
        System.out.println("Ferry capacity: " + capFerry);
        System.out.println("--------------------");
    }

}
