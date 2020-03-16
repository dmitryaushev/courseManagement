package com.courses.management.common.commands.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputString {

    private static final Logger LOG = LogManager.getLogger(InputString.class);
    private String inputString;

    public InputString(String inputString) {
        this.inputString = inputString;
    }

    public void validateParameters(String command) {
        int commandLength = getParametersSize(command);
        int inputLength = getLength();
        if(inputLength != commandLength) {
            String message = String.format("Invalid number of parameters separated by |, expected %s, but was %s"
                    , commandLength, inputLength);
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public int getLength() {
        return getParametersSize(inputString);
    }

    public String[] getParameters() {
        return inputString.split("\\|");
    }

    private int getParametersSize(String command) {
        return command.split("\\|").length;
    }
}