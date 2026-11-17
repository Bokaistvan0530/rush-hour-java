package hu.rushhour;

import hu.rushhour.db.ScoreRepository;
import hu.rushhour.game.Game;
import hu.rushhour.io.XmlSave;
import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private Board createBoard() {
        Board board = new Board(6, 12);
        board.add(new Vehicle('A', 2, 2, 2, true));
        board.add(new Vehicle('B', 3, 6, 2, true));
        return board;
    }

    private String runGame(Board board, String commands,
                           ScoreRepository scores) {

        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            Game game = new Game(
                    board,
                    new Scanner(commands),
                    new Random(42),
                    scores
            );

            game.start();

        } finally {
            System.setOut(original);
        }

        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void quitShouldEndGame() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nquit\n", scores);

        assertTrue(output.contains("Anna>"));
        verify(scores).initialize();
    }

    @Test
    void helpShouldDisplayCommands() {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nhelp\nquit\n", scores);

        assertTrue(output.contains("Commands:"));
    }

    @Test
    void unknownCommandShouldShowMessage() {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nxyz\nquit\n", scores);

        assertTrue(output.contains("Unknown command"));
    }

    @Test
    void invalidMoveShouldShowError() {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nmove A 100\nquit\n", scores);

        assertTrue(output.contains("Error:"));
    }

    @Test
    void missingArgumentsShouldShowError() {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nmove A\nquit\n", scores);

        assertTrue(output.contains("Expected 2 argument(s)"));
    }

    @Test
    void invalidVehicleIdShouldNotMoveAnotherCar() {
        ScoreRepository scores = mock(ScoreRepository.class);
        String output = runGame(
                createBoard(), "Anna\nmove AAA 1\nquit\n", scores);
        assertTrue(output.contains("Vehicle id must be one character"));
        assertFalse(output.contains("Computer moved"));
    }

    @Test
    void invalidExitIdShouldNotWin() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);
        Board board = new Board(6, 12);
        board.add(new Vehicle('A', 2, 10, 2, true));
        board.add(new Vehicle('B', 4, 5, 2, true));
        String output = runGame(board, "Anna\nexit Alice\nquit\n", scores);
        assertTrue(output.contains("Vehicle id must be one character"));
        assertFalse(output.contains("You win!"));
    }

    @Test
    void scoresShouldBeDisplayed() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nscores\nquit\n", scores);

        verify(scores, atLeastOnce()).highScores();
        assertFalse(output.isEmpty());
    }

    @Test
    void exitBeforeEdgeShouldFail() {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nexit A\nquit\n", scores);

        assertTrue(output.contains(
                "A must reach the right edge first"));
    }

    @Test
    void emptyCommandsShouldNotCrash() {
        ScoreRepository scores = mock(ScoreRepository.class);

        assertDoesNotThrow(() ->
                runGame(createBoard(), "", scores));
    }

    @Test
    void humanWinShouldBeRecorded() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);

        Board board = new Board(6, 12);
        board.add(new Vehicle('A', 2, 10, 2, true));
        board.add(new Vehicle('B', 4, 5, 2, true));

        String output = runGame(
                board, "Anna\nexit A\n", scores);

        assertTrue(output.contains("You win!"));
        verify(scores).win("Anna");
    }

    @Test
    void computerWinShouldBeRecorded() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);

        Board board = new Board(6, 12);
        board.add(new Vehicle('A', 2, 5, 2, true));
        board.add(new Vehicle('B', 4, 0, 2, true));

        String output = runGame(
                board, "Anna\nmove A 1\n", scores);

        assertTrue(output.contains("Computer wins!"));
        verify(scores).win("Computer");
    }

    @Test
    void loadXmlShouldResumeComputerTurn() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);
        Path file = Files.createTempFile("rush-hour-resume", ".xml");
        try {
            XmlSave.save(file, new XmlSave.Snapshot(createBoard(), false, "Anna"));
            String output = runGame(createBoard(),
                    "Anna\nloadxml " + file + "\nquit\n", scores);
            assertTrue(output.contains("Game resumed."));
            assertTrue(output.contains("Computer moved"));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void computerShouldMakeLegalMove() throws Exception {
        ScoreRepository scores = mock(ScoreRepository.class);

        String output = runGame(
                createBoard(), "Anna\nmove A 1\nquit\n", scores);

        assertTrue(output.contains("Computer moved"));
    }

}
