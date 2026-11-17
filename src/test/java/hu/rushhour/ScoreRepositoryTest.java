package hu.rushhour;

import hu.rushhour.db.ScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ScoreRepositoryTest {

    @TempDir
    Path tempDir;

    private ScoreRepository createRepository() {
        String databasePath = tempDir.resolve("scores").toString();
        return new ScoreRepository(databasePath);
    }

    @Test
    void initializeShouldCreateDatabase() throws SQLException {
        ScoreRepository repository = createRepository();

        assertDoesNotThrow(repository::initialize);

        String result = repository.highScores();

        assertEquals("HIGH SCORES\n", result);
    }

    @Test
    void initializeCanBeCalledMultipleTimes() {
        ScoreRepository repository = createRepository();

        assertDoesNotThrow(() -> {
            repository.initialize();
            repository.initialize();
        });
    }

    @Test
    void winShouldSavePlayerScore() throws SQLException {
        ScoreRepository repository = createRepository();
        repository.initialize();

        repository.win("Anna");

        String result = repository.highScores();

        assertTrue(result.contains("Anna: 1"));
    }

    @Test
    void multipleWinsShouldIncreaseScore() throws SQLException {
        ScoreRepository repository = createRepository();
        repository.initialize();

        repository.win("Anna");
        repository.win("Anna");
        repository.win("Anna");

        String result = repository.highScores();

        assertTrue(result.contains("Anna: 3"));
    }

    @Test
    void differentPlayersShouldHaveSeparateScores()
            throws SQLException {

        ScoreRepository repository = createRepository();
        repository.initialize();

        repository.win("Anna");
        repository.win("Peter");
        repository.win("Anna");

        String result = repository.highScores();

        assertTrue(result.contains("Anna: 2"));
        assertTrue(result.contains("Peter: 1"));
    }

    @Test
    void highScoresShouldSortByWins() throws SQLException {
        ScoreRepository repository = createRepository();
        repository.initialize();

        repository.win("Anna");
        repository.win("Peter");
        repository.win("Peter");
        repository.win("Peter");

        String result = repository.highScores();

        assertTrue(result.indexOf("Peter: 3")
                < result.indexOf("Anna: 1"));
    }

    @Test
    void scoresShouldPersistBetweenRepositoryInstances()
            throws SQLException {

        String databasePath = tempDir.resolve("persistent").toString();

        ScoreRepository first = new ScoreRepository(databasePath);
        first.initialize();
        first.win("Anna");

        ScoreRepository second = new ScoreRepository(databasePath);

        String result = second.highScores();

        assertTrue(result.contains("Anna: 1"));
    }

    @Test
    void highScoresShouldSortEqualScoresByName()
            throws SQLException {

        ScoreRepository repository = createRepository();
        repository.initialize();

        repository.win("Zita");
        repository.win("Anna");

        String result = repository.highScores();

        assertTrue(result.indexOf("Anna: 1")
                < result.indexOf("Zita: 1"));
    }
}
