package com.bumnetworks.log;

import java.io.*;
import java.util.*;
import com.mongodb.*;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class MongoDBLogger extends MarkerIgnoringBase {
    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR;
    }

    private static final long serialVersionUID = -6560244151660620173L;

    private static final String DEFAULT_PROPS = "slf4j-mongodb.properties";

    private static final Properties props;
    static {
        synchronized (MongoDBLogger.class) {
            try {
                props = new Properties();
                props.load(MongoDBLogger.class
                           .getClassLoader()
                           .getResourceAsStream(System
                                                .getProperty("slf4j.mongodb.props", DEFAULT_PROPS)));
            }
            catch (IOException ioe) { throw new RuntimeException(ioe); }
        }
    }

    private Map<Level,Boolean> levels;
    private Mongo mongo;
    private DB db;

    public MongoDBLogger(String name) {
        levels = new HashMap<Level,Boolean>();
        for (Level level : Level.values())
            levels.put(level, true);
        this.name = name;
        try {
            mongo = new Mongo(props.getProperty("slf4j.mongodb.mongo.host", "localhost"),
                              new Integer(props.getProperty("slf4j.mongodb.mongo.port", "27017")).intValue());
            db = mongo.getDB(props.getProperty("slf4j.mongodb.mongo.database", "log." + name));
        }
        catch (Throwable t) { throw new RuntimeException(t); }
    }

    private boolean isLevelEnabled(Level level) {
        return levels.get(level);
    }

    private boolean isLevelEnabled(String level) {
        return levels.get(Level.valueOf(level));
    }

    private void formatAndLog(String level, String format, Object arg1, Object arg2) {
        String message = MessageFormatter.format(format, arg1, arg2);
        log(level, message);
    }

    private void formatAndLog(String level, String format, Object[] argArray) {
        String message = MessageFormatter.arrayFormat(format, argArray);
        log(level, message);
    }

    private void log(String level, String message) {
        log(level, message, null);
    }

    private void log(String level, String message, Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            message = message + " " + sw.toString();
        }
        DBObject o = BasicDBObjectBuilder.start("level", level)
            .add("message", message).add("time", new Date())
            .get();
        db.getCollection("log").insert(o);
    }

    public boolean isTraceEnabled() {
        return isLevelEnabled(Level.TRACE);
    }

    public void trace(String msg) {
        log("TRACE", msg, null);
    }

    public void trace(String format, Object arg) {
        formatAndLog("TRACE", format, arg, null);
    }

    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog("TRACE", format, arg1, arg2);
    }

    public void trace(String format, Object[] argArray) {
        formatAndLog("TRACE", format, argArray);
    }

    public void trace(String msg, Throwable t) {
        log("TRACE", msg, t);
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    public void debug(String msg) {
        log("DEBUG", msg, null);
    }

    public void debug(String format, Object arg) {
        formatAndLog("DEBUG", format, arg, null);
    }

    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog("DEBUG", format, arg1, arg2);
    }

    public void debug(String format, Object[] argArray) {
        formatAndLog("DEBUG", format, argArray);
    }

    public void debug(String msg, Throwable t) {
        log("DEBUG", msg, t);
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    public void info(String msg) {
        log("INFO", msg, null);
    }

    public void info(String format, Object arg) {
        formatAndLog("INFO", format, arg, null);
    }

    public void info(String format, Object arg1, Object arg2) {
        formatAndLog("INFO", format, arg1, arg2);
    }

    public void info(String format, Object[] argArray) {
        formatAndLog("INFO", format, argArray);
    }

    public void info(String msg, Throwable t) {
        log("INFO", msg, t);
    }

    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARN);
    }

    public void warn(String msg) {
        log("WARN", msg, null);
    }

    public void warn(String format, Object arg) {
        formatAndLog("WARN", format, arg, null);
    }

    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog("WARN", format, arg1, arg2);
    }

    public void warn(String format, Object[] argArray) {
        formatAndLog("WARN", format, argArray);
    }
    public void warn(String msg, Throwable t) {
        log("WARN", msg, t);
    }

    public boolean isErrorEnabled() {
        return isLevelEnabled(Level.ERROR);
    }

    public void error(String msg) {
        log("ERROR", msg, null);
    }

    public void error(String format, Object arg) {
        formatAndLog("ERROR", format, arg, null);
    }

    public void error(String format, Object arg1, Object arg2) {
        formatAndLog("ERROR", format, arg1, arg2);
    }

    public void error(String format, Object[] argArray) {
        formatAndLog("ERROR", format, argArray);
    }

    public void error(String msg, Throwable t) {
        log("ERROR", msg, t);
    }
}
