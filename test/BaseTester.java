package test;

import main.util.Logger;

import java.time.Duration;
import java.time.Instant;

public class BaseTester {

    /**
     * Included to hide the implicit public constructor
     */
    private BaseTester() {
    }

    public static final String STARTING_TEST_MESSAGE = "Testing \"%s\"";
    private static final String FINISHING_TEST_MESSAGE = "Finished tests for \"%s\" in %.4f milliseconds";

    public static Instant startTest(String name) {
        String header = String.format(STARTING_TEST_MESSAGE, name);

        Logger.info("-".repeat(header.length() + 4));
        Logger.info(String.format("| %s |%n", header));
        Logger.info("-".repeat(header.length() + 4));
        Logger.info();

        return Instant.now();
    }

    public static Instant finishTest(String name, Instant startedAt) {
        Instant finishedAt = Instant.now();
        Duration duration = Duration.between(startedAt, finishedAt);
        String header = String.format(FINISHING_TEST_MESSAGE,
                name,
                duration.toMillis() + duration.getNano() / 1_000_000d);

        Logger.info();
        Logger.info(header);
        Logger.info("-".repeat(header.length()));

        return finishedAt;
    }

    public static void test(String testDescription, String actualValue, String expectedValue) {
        String status;
        if ((expectedValue == null && actualValue == null) || (actualValue != null && actualValue.equals(expectedValue))) {
            status = "PASSED";
        } else {
            status = String.format("FAILED (expected [%s] and got [%s])%n", expectedValue, actualValue);
        }

        Logger.info(String.format("%s: %s", testDescription, status));
    }
}
