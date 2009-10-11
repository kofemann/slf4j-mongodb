package com.bumnetworks.log;

import java.io.*;
import java.util.*;
import com.mongodb.*;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class MongoDBLogger extends MarkerIgnoringBase {
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

    private boolean trace, debug, info, warn, error;
    private Mongo mongo;
    private DB db;

    public MongoDBLogger(String name) {
        this.name = name;
        trace = debug = info = warn = error
            = true;
        try {
            mongo = new Mongo(props.getProperty("slf4j.mongodb.mongo.host", "localhost"),
                              new Integer(props.getProperty("slf4j.mongodb.mongo.port", "27017")).intValue());
            db = mongo.getDB(props.getProperty("slf4j.mongodb.mongo.database", "log." + name));
        }
        catch (Throwable t) { throw new RuntimeException(t); }
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
        return trace;
    }

    public void trace(String msg) {
    }

    public void trace(String format, Object param1) {
    }

    public void trace(String format, Object param1, Object param2) {
    }

    public void trace(String format, Object[] argArray) {
    }

    public void trace(String msg, Throwable t) {
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    public void debug(String msg) {
    }

    public void debug(String format, Object param1) {
    }

    public void debug(String format, Object param1, Object param2) {
    }

    public void debug(String format, Object[] argArray) {
    }

    public void debug(String msg, Throwable t) {
    }

    public boolean isInfoEnabled() {
        return info;
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
        return warn;
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
        return error;
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
