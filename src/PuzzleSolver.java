import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

/**
 * Creates a PuzzleSolver class that uses the backtracking method to solve a Futoshiki puzzle using
 * Fork/Join.
 * 
 * @author Ani Lamichhane
 *
 */
public class PuzzleSolver extends RecursiveAction {
    /**
     * private puzzle object of the Futoshiki puzzle.
     */
    private Puzzle puzzle;
    /**
     * private int that holds the starting spot.
     */
    private int start;

    /**
     * private puzzle object that holds the answer to the Futoshiki puzzle and null if there isn't
     * an answer.
     */
    private Puzzle answer;


    /**
     * Constructor that set values for the puzzle and current spot, also sets answer to null.
     * 
     * @param puzzle the Futoshiki puzzle
     * @param currentSpot the starting spot
     */
    public PuzzleSolver(Puzzle puzzle, int currentSpot) {
        this.puzzle = puzzle;
        start = currentSpot;

        answer = null;

    }

    /**
     * Method that computes the answer to the Futoshiki puzzle using Fork/Join.
     */
    public void compute() {

        if ((puzzle.getSize() * puzzle.getSize() - start) <= 20) {
            answer = Puzzle.solve(puzzle, start);

        } else {

            int currentRow = start / puzzle.getSize();
            int currentColumn = start % puzzle.getSize();
            if (puzzle.getValue(currentRow, currentColumn) == Puzzle.EMPTY) {
                ArrayList<PuzzleSolver> puzzleList = new ArrayList<PuzzleSolver>();

                for (int i = 1; i <= puzzle.getSize(); i++) {
                    Puzzle copy = new Puzzle(puzzle);
                    copy.insertValue(currentRow, currentColumn, i);

                    if (copy.isValid() == true) {
                        puzzleList.add(new PuzzleSolver(copy, start + 1));

                    } else {
                        answer = null;
                    }

                }

                for (int j = 0; j < puzzleList.size(); j++) {
                    puzzleList.get(j).fork();
                }

                for (int j = 0; j < puzzleList.size(); j++) {
                    puzzleList.get(j).join();
                }

                for (int j = 0; j < puzzleList.size(); j++) {

                    if (puzzleList.get(j).getSolved() != null) {
                        answer = puzzleList.get(j).getSolved();
                    }
                }

            } else {
                PuzzleSolver anotherPuzzleSolver = new PuzzleSolver(puzzle, start + 1);
                anotherPuzzleSolver.compute();

                answer = anotherPuzzleSolver.getSolved();
            }

        }

    }

    /**
     * A getter method for the answer.
     * 
     * @return the solved puzzle or null if it can't be solved.
     */
    public Puzzle getSolved() {
        return answer;

    }

}
