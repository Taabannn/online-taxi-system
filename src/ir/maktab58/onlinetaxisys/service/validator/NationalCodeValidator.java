package ir.maktab58.onlinetaxisys.service.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taban Soleymani
 */
public class NationalCodeValidator {
    private static final NationalCodeValidator nationalCodeValidator = new NationalCodeValidator();
    private static final String NATIONAL_CODE_PATTERN = "\\d{10}";

    private NationalCodeValidator() {
    }

    public static NationalCodeValidator getSingletonInstance() {
        return nationalCodeValidator;
    }

    public boolean isNationalCodeValid(String nationalCode) {
        Pattern pattern = Pattern.compile(NATIONAL_CODE_PATTERN);
        if (nationalCode == null) {
            return false;
        }
        Matcher nationalCodeMatcher = pattern.matcher(nationalCode);
        return nationalCodeMatcher.matches();
    }
}
