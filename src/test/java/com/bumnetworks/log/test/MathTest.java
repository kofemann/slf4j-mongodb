package com.bumnetworks.log.test;

import org.junit.*;
import static org.junit.Assert.*;
import com.mongodb.*;
import org.slf4j.*;
import com.bumnetworks.log.MongoDBLogger;
import java.util.*;
import java.lang.reflect.*;

public class MathTest {
    private Logger log = LoggerFactory.getLogger(MathTest.class);
    private Random rand = new Random();
    private Mongo mongo;
    private DB db;

    @Before
    public void setUp() throws Throwable {
        mongo = ((MongoDBLogger) log).getMongo();
        db = ((MongoDBLogger) log).getDB();

        for (MongoDBLogger.Level l : MongoDBLogger.Level.values())
            ((MongoDBLogger) log).getColl(l).remove(new BasicDBObject());
    }

    @Test
    public void isThisUs() {
        assertTrue(log instanceof MongoDBLogger);
    }

    @Test
    public void logAndCount()
        throws NoSuchMethodException, IllegalAccessException,
               InvocationTargetException {

        Map<MongoDBLogger.Level, Integer> counts
            = new HashMap<MongoDBLogger.Level, Integer>();
        for (MongoDBLogger.Level l : MongoDBLogger.Level.values())
            counts.put(l, rand.nextInt(100));

        for (MongoDBLogger.Level l : counts.keySet())
            for (int i = 1; i <= counts.get(l); i++)
                levelToMethod(l).invoke(log, new Object[] { "giggity_" + i });

        System.err.println(counts);

        for (MongoDBLogger.Level l : counts.keySet())
            assertEquals(
                         l.toString(),
                         counts.get(l).toString(),
                         (new Integer(((MongoDBLogger) log).getColl(l).find().length())).toString()
                         );
    }

    private Method levelToMethod(MongoDBLogger.Level level)
        throws NoSuchMethodException, IllegalAccessException {

        return Logger.class.getMethod(level.toString().toLowerCase(),
                                      String.class);
    }
}
