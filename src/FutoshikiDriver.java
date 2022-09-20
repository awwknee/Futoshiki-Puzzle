import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 * This program can solve Futoshiki puzzles quickly using the Java Fork/Join framework. The program
 * loads puzzles in from files, ask the user whether they want to solve the problem sequentially or
 * with Fork/Join, and then times how long it takes to find a solution.
 * 
 * @author Ani Lamcichhane
 *
 */
public class FutoshikiDriver {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        int puzzleNumber;
        int puzzleSolver;
        Puzzle puzzle;
        long startTime;
        long endTime;

        System.out.println("Welcome to the Futoshiki solver");
        System.out.print("Enter a puzzle number: ");
        puzzleNumber = keyboard.nextInt();

        try {
            puzzle = Puzzle.fromFile(puzzleNumber + ".txt");

        } catch (IllegalArgumentException e) {
            System.out.print("Wrong file you silly goose! :D");
            return;
        }

        System.out.print(puzzle);

        System.out.println("How would you like to solve the puzzle?");
        System.out.println("1. Sequential");
        System.out.println("2. Fork/Join");
        puzzleSolver = keyboard.nextInt();

        if (puzzleSolver == 1) {
            System.out.println("Solving sequentially...");
        } else if (puzzleSolver == 2) {
            System.out.println("Solving with Fork/Join...");
        } else {
            System.out.println("Invalid number you silly goose! :D");
        }

        if (puzzleSolver == 1) {
            startTime = System.currentTimeMillis();
            Puzzle solvedPuzzle = Puzzle.solve(puzzle, 0);
            endTime = System.currentTimeMillis();

            if (solvedPuzzle == null) {
                System.out.println("No solution you silly goose. :D");

            } else {
                System.out.println(solvedPuzzle);
                System.out.print("Solution found in " + ((endTime - startTime) / (double) 1000)
                        + " seconds.");
            }

        } else {
            PuzzleSolver initial = new PuzzleSolver(puzzle, 0);
            startTime = System.currentTimeMillis();
            ForkJoinPool.commonPool().invoke(initial);
            endTime = System.currentTimeMillis();

            if (initial.getSolved() == null) {
                System.out.println("No solution you silly goose. :D");

            } else {
                System.out.println(initial.getSolved());
                System.out.print("Solution found in " + ((endTime - startTime) / (double) 1000)
                        + " seconds.");
            }


        }


    }



}
