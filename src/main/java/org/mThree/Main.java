package org.mThree;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception {

        System.out.println( "Hello World!" );

        // Create a service with 3 threads.
        ScheduledExecutorService execService = Executors.newScheduledThreadPool(3);

        // Schedule a task to run every 5 minutes with no initial delay.
        execService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    JsonParser.JsonParse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 5L, TimeUnit.MINUTES);
    }
}
