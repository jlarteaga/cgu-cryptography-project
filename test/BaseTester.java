package test;

import main.util.Logger;

import java.time.Duration;
import java.time.Instant;

public class BaseTester {

    public static final String STARTING_TEST_MESSAGE = "Testing \"%s\"";
    private static final String FINISHING_TEST_MESSAGE = "Finished in %.4f milliseconds";

    /**
     * Included to hide the implicit public constructor
     */
    private BaseTester() {
    }

    public static Instant startTest(String name) {
        String header = String.format(STARTING_TEST_MESSAGE, name);
        String horizontalLine = "-".repeat(header.length() + 4);
        String box = String.format("%s%n| %s |%n%s%n", horizontalLine, header, horizontalLine);

        Logger.info(box);

        return Instant.now();
    }

    public static Instant finishTest(Instant startedAt) {
        Instant finishedAt = Instant.now();
        Duration duration = Duration.between(startedAt, finishedAt);

        String header = String.format(FINISHING_TEST_MESSAGE,
                duration.toMillis() + duration.getNano() / 1_000_000d);

        Logger.info(String.format("- %s -%n%n", header));

        return finishedAt;
    }

    public static void test(String testDescription, String actualValue, String expectedValue) {
        String status;
        if ((expectedValue == null && actualValue == null) || (actualValue != null && actualValue.equals(expectedValue))) {
            status = "PASSED";
        } else {
            status = String.format("FAILED (expected [%s] and got [%s])%n", expectedValue, actualValue);
        }

        Logger.info(String.format("%s: %s%n", testDescription, status));
    }
}
