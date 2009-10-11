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
            String dbName = props.getProperty("slf4j.mongodb.mongo.database", "");
            if (dbName.equals(""))
                throw new IllegalArgumentException("please define 'slf4j.mongodb.mongo.database' in your props file");
            db = mongo.getDB(dbName);
        }
        catch (Throwable t) { throw new RuntimeException(t); }
    }

    private boolean isLevelEnabled(Level level) {
        return levels.get(level);
    }

    private boolean isLevelEnabled(String level) {
        return levels.get(Level.valueOf(level));
    }

    private void formatAndLog(Level level, String format, Object arg1, Object arg2) {
        String message = MessageFormatter.format(format, arg1, arg2);
        log(level, message);
    }

    private void formatAndLog(Level level, String format, Object[] argArray) {
        String message = MessageFormatter.arrayFormat(format, argArray);
        log(level, message);
    }

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, String message, Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            message = message + " " + sw.toString();
        }
        StackTraceElement caller = getCaller();
        BasicDBObjectBuilder builder = BasicDBObjectBuilder
            .start("message", message)
            .add("time", new Date())
            .add("file", caller.getFileName())
            .add("line", caller.getLineNumber())
            .add("method", caller.getMethodName())
            .add("class", caller.getClassName());
        getColl(level).insert(builder.get());
    }

    private StackTraceElement getCaller() {
        try {
            throw new Throwable();
        }
        catch (Throwable t) {
            for (StackTraceElement frame : t.getStackTrace())
                if (frame.getClassName().equals(name))
                    return frame;
        }
        return null;
    }

    public boolean isTraceEnabled() {
        return isLevelEnabled(Level.TRACE);
    }

    public void trace(String msg) {
        log(Level.TRACE, msg, null);
    }

    public void trace(String format, Object arg) {
        formatAndLog(Level.TRACE, format, arg, null);
    }

    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog(Level.TRACE, format, arg1, arg2);
    }

    public void trace(String format, Object[] argArray) {
        formatAndLog(Level.TRACE, format, argArray);
    }

    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    public void debug(String msg) {
        log(Level.DEBUG, msg, null);
    }

    public void debug(String format, Object arg) {
        formatAndLog(Level.DEBUG, format, arg, null);
    }

    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog(Level.DEBUG, format, arg1, arg2);
    }

    public void debug(String format, Object[] argArray) {
        formatAndLog(Level.DEBUG, format, argArray);
    }

    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    public void info(String format, Object arg) {
        formatAndLog(Level.INFO, format, arg, null);
    }

    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(Level.INFO, format, arg1, arg2);
    }

    public void info(String format, Object[] argArray) {
        formatAndLog(Level.INFO, format, argArray);
    }

    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARN);
    }

    public void warn(String msg) {
        log(Level.WARN, msg, null);
    }

    public void warn(String format, Object arg) {
        formatAndLog(Level.WARN, format, arg, null);
    }

    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(Level.WARN, format, arg1, arg2);
    }

    public void warn(String format, Object[] argArray) {
        formatAndLog(Level.WARN, format, argArray);
    }
    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    public boolean isErrorEnabled() {
        return isLevelEnabled(Level.ERROR);
    }

    public void error(String msg) {
        log(Level.ERROR, msg, null);
    }

    public void error(String format, Object arg) {
        formatAndLog(Level.ERROR, format, arg, null);
    }

    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(Level.ERROR, format, arg1, arg2);
    }

    public void error(String format, Object[] argArray) {
        formatAndLog(Level.ERROR, format, argArray);
    }

    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    public Mongo getMongo() { return mongo; }
    public DB getDB() { return db; }
    public DBCollection getColl(Level level) {
        return getDB().getCollection("level." + level.toString());
    }
}
