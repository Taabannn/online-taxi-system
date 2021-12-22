package ir.maktab58.onlinetaxisys.service.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taban Soleymani
 */
public class UserAndPassValidator {
    private static final UserAndPassValidator userAndPassValidator = new UserAndPassValidator();
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=\\S+$).{6,20}$";
    private static final String USERNAME_PATTERN =  "^[a-zA-Z0-9]" +
            "([._-](?![._-])|[a-zA-Z0-9])" +
            "{3,18}[a-zA-Z0-9]$";

    private UserAndPassValidator() {
    }

    public static UserAndPassValidator getSingletonInstance() {
        return userAndPassValidator;
    }

    public boolean isUserAndPassValid(String username, String password) {
        Pattern userPattern = Pattern.compile(USERNAME_PATTERN);
        Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);
        if ((password == null) || (username == null)) {
            return false;
        }
        Matcher userMatcher = userPattern.matcher(username);
        Matcher passMatcher = passPattern.matcher(password);
        return (userMatcher.matches() && passMatcher.matches());
    }
}
