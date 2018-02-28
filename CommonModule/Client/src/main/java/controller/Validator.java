package controller;

import java.util.regex.Pattern;

public class Validator {

    private final static String REGEX_REGISTRATION = "((^\\/register\\s)(client|agent)\\s[^\\n]+)";

    public boolean isValidateRequest(String request) {
        Pattern pattern = Pattern.compile(REGEX_REGISTRATION);

        return pattern.matcher(request).matches();
    }
}
