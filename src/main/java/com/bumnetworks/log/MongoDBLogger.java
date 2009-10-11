package com.bumnetworks.log;

import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class MongoDBLogger extends MarkerIgnoringBase {
  private static final long serialVersionUID = -6560244151660620173L;

  public MongoDBLogger(String name) {
    this.name = name;
  }

  public boolean isTraceEnabled() {
    return false;
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
    return false;
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

  private void log(String level, String message, Throwable t) {
  }

  private void formatAndLog(String level, String format, Object arg1, Object arg2) {
    String message = MessageFormatter.format(format, arg1, arg2);
    log(level, message, null);
  }
  
  private void formatAndLog(String level, String format, Object[] argArray) {
    String message = MessageFormatter.arrayFormat(format, argArray);
    log(level, message, null);
  }

  public boolean isInfoEnabled() {
    return true;
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
    return true;
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
    return true;
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
