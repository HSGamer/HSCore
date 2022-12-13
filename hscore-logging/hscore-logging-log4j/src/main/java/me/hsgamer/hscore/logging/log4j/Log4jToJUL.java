package me.hsgamer.hscore.logging.log4j;

import java.util.function.Function;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A simple utility to merge / create a {@link Logger} from a {@link org.apache.logging.log4j.Logger}
 */
public final class Log4jToJUL {
  private Log4jToJUL() {
    // EMPTY
  }

  /**
   * Publish the {@link LogRecord} to {@link org.apache.logging.log4j.Logger}
   *
   * @param logRecord      the {@link LogRecord}
   * @param formatFunction the function to format the {@link LogRecord}
   * @param log4jLogger    the {@link org.apache.logging.log4j.Logger}
   */
  public static void publishLogRecord(LogRecord logRecord, Function<LogRecord, String> formatFunction, org.apache.logging.log4j.Logger log4jLogger) {
    String message = formatFunction.apply(logRecord);
    Throwable throwable = logRecord.getThrown();
    switch (logRecord.getLevel().getName()) {
      case "SEVERE":
        log4jLogger.error(message, throwable);
        break;
      case "WARNING":
        log4jLogger.warn(message, throwable);
        break;
      case "CONFIG":
      case "FINE":
        log4jLogger.debug(message, throwable);
        break;
      case "FINER":
      case "FINEST":
        log4jLogger.trace(message, throwable);
        break;
      default:
        log4jLogger.info(message, throwable);
        break;
    }
  }

  /**
   * Publish the {@link LogRecord} to {@link org.apache.logging.log4j.Logger}
   *
   * @param logRecord   the {@link LogRecord}
   * @param formatter   the {@link Formatter} to format the {@link LogRecord}
   * @param log4jLogger the {@link org.apache.logging.log4j.Logger}
   */
  public static void publishLogRecord(LogRecord logRecord, Formatter formatter, org.apache.logging.log4j.Logger log4jLogger) {
    publishLogRecord(logRecord, formatter::format, log4jLogger);
  }

  /**
   * Merge the {@link org.apache.logging.log4j.Logger} to {@link Logger}
   *
   * @param julLogger   the {@link Logger}
   * @param log4jLogger the {@link org.apache.logging.log4j.Logger}
   */
  public static void merge(Logger julLogger, org.apache.logging.log4j.Logger log4jLogger) {
    Handler handler = new Handler() {
      @Override
      public void publish(LogRecord logRecord) {
        publishLogRecord(logRecord, getFormatter(), log4jLogger);
      }

      @Override
      public void flush() {
        // EMPTY
      }

      @Override
      public void close() throws SecurityException {
        // EMPTY
      }
    };
    julLogger.addHandler(handler);
  }

  /**
   * Create a {@link Logger} from a {@link org.apache.logging.log4j.Logger}
   *
   * @param log4jLogger the {@link org.apache.logging.log4j.Logger}
   * @param name        the name of the {@link Logger}
   *
   * @return the {@link Logger}
   */
  public static Logger createLogger(String name, org.apache.logging.log4j.Logger log4jLogger) {
    Logger julLogger = Logger.getLogger(name);
    for (Handler handler : julLogger.getHandlers()) {
      julLogger.removeHandler(handler);
    }
    julLogger.setUseParentHandlers(false);
    merge(julLogger, log4jLogger);
    return julLogger;
  }

  /**
   * Create a {@link Logger} from a {@link org.apache.logging.log4j.Logger}
   *
   * @param log4jLogger the {@link org.apache.logging.log4j.Logger}
   *
   * @return the {@link Logger}
   */
  public static Logger createLogger(org.apache.logging.log4j.Logger log4jLogger) {
    return createLogger(log4jLogger.getName(), log4jLogger);
  }
}
