package xpertss.json.schema.core.report;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.exceptions.ProcessingException;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public final class AbstractProcessingReportTest {

    /*
     * All levels except fatal
     */
    private static final EnumSet<LogLevel> LEVELS = EnumSet.complementOf(EnumSet.of(LogLevel.NONE));

    public Iterator<Object[]> getLogLevels()
    {
        final List<Object[]> list = Lists.newArrayList();

        for (final LogLevel level: LEVELS)
            list.add(new Object[] { level });

        // We don't want the values in the same order repeatedly, so...
        Collections.shuffle(list);

        return list.iterator();
    }

    @Test
    @Parameters(method = "getLogLevels")
    public void logThresholdIsRespected(final LogLevel logLevel)
        throws ProcessingException
    {
        final AbstractProcessingReport report
            = spy(new LogThreshold(logLevel));
        final ProcessingMessage message = new ProcessingMessage();
        // OK, that's ugly, but it works...
        final int count = LogLevel.NONE.ordinal() - logLevel.ordinal();

        report.debug(message);
        report.info(message);
        report.warn(message);
        report.error(message);
        report.fatal(message);

        verify(report, times(count)).log(any(LogLevel.class), same(message));
    }

    @Test
    public void logLevelIsCorrectlySetInMessages()
        throws ProcessingException
    {
        final ProcessingReport report = new LogThreshold(LogLevel.NONE);
        final ProcessingMessage message = new ProcessingMessage();

        report.debug(message);
        assertMessage(message).hasLevel(LogLevel.DEBUG);
        report.info(message);
        assertMessage(message).hasLevel(LogLevel.INFO);
        report.warn(message);
        assertMessage(message).hasLevel(LogLevel.WARNING);
        report.error(message);
        assertMessage(message).hasLevel(LogLevel.ERROR);
        report.fatal(message);
        assertMessage(message).hasLevel(LogLevel.FATAL);
    }

    @Test
    @Parameters(method = "getLogLevels")
    public void exceptionThresholdIsRespected(final LogLevel logLevel)
    {
        ProcessingReport report = new LogThreshold(LogLevel.DEBUG, logLevel);
        final ProcessingMessage message = new ProcessingMessage();
        final int expected = LogLevel.NONE.ordinal() - logLevel.ordinal();
        int actual = 0;

        try {
            report.debug(message);
        } catch (ProcessingException ignored) {
            actual++;
        }
        try {
            report.info(message);
        } catch (ProcessingException ignored) {
            actual++;
        }
        try {
            report.warn(message);
        } catch (ProcessingException ignored) {
            actual++;
        }
        try {
            report.error(message);
        } catch (ProcessingException ignored) {
            actual++;
        }

        try {
            report.fatal(message);
        } catch (ProcessingException ignored) {
            actual++;
        }

        assertEquals(expected, actual);
    }

    private static class LogThreshold extends AbstractProcessingReport {

        private LogThreshold(LogLevel logLevel, LogLevel exceptionThreshold)
        {
            super(logLevel, exceptionThreshold);
        }

        private LogThreshold(final LogLevel logThreshold)
        {
            super(logThreshold, LogLevel.NONE);
        }

        @Override
        public void log(final LogLevel level, final ProcessingMessage message)
        {
        }
    }
}
