/*
@fileName:  StoodyLogger

@aka:       Stoody Logger

@purpose:   Either prints console messages or saves the messages to appropriate repositories so that authorized user can see it.

@author:    OrkhanGG

@created:   20.12.2022
*/

package com.loam.stoody.global.logger;

public class StoodyLogger {

    // Logs message that can only be seen from terminal/console
    // Don't use in production!
    public static void DebugLog(String consoleColor, String message){
        System.out.println(
                consoleColor
                + message
                + ConsoleColors.RESET);
    }

    public static void LogAndSave(String logMessage){
        // This method will save the logs so admin may see them
        // errorRepository.save(logMessage);
    }
}
