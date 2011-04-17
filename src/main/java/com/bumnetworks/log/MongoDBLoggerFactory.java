package com.bumnetworks.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.ILoggerFactory;

/**
   Ruthlessly ripped out of slf4j-simple.
*/
public class MongoDBLoggerFactory implements ILoggerFactory {

    final static MongoDBLoggerFactory INSTANCE = new MongoDBLoggerFactory();

    final Map<String, Logger> loggerMap =  new HashMap<String, Logger>();

    public Logger getLogger(String name) {
        Logger slogger = null;
        // protect against concurrent access of the loggerMap
        synchronized (this) {
            slogger = loggerMap.get(name);
            if (slogger == null) {
                slogger = new MongoDBLogger(name);
                loggerMap.put(name, slogger);
            }
        }
        return slogger;
    }
}
