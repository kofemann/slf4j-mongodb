package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import com.bumnetworks.log.*;

/**
   Ruthlessly ripped out of slf4j-simple.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    public static final StaticLoggerBinder getSingleton() { return SINGLETON; }
    private static final String loggerFactoryClassStr = MongoDBLoggerFactory.class.getName();
    private final ILoggerFactory loggerFactory;
    private StaticLoggerBinder() { loggerFactory = new MongoDBLoggerFactory(); }
    public ILoggerFactory getLoggerFactory() { return loggerFactory; }
    public String getLoggerFactoryClassStr() { return loggerFactoryClassStr; }
}
