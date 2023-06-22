package com.atique.balanceservice.annotations;

import com.atique.balanceservice.util.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:14 PM
 */

public class AccountNoValidator implements ConstraintValidator<ValidAccountNo, String> {

    private Integer ACCOUNT_NO_MIN_LENGTH;
    private Integer ACCOUNT_NO_MAX_LENGTH;
    private Pattern ACCOUNT_NO_REGEX_PATTERN;

    @Override
    public void initialize(ValidAccountNo constraintAnnotation) {
        ACCOUNT_NO_MIN_LENGTH = Constants.AccountNo.MIN_LENGTH;
        ACCOUNT_NO_MAX_LENGTH = Constants.AccountNo.MAX_LENGTH;
        ACCOUNT_NO_REGEX_PATTERN = Pattern.compile(Constants.AccountNo.REGEX);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasLength(value) || value.length() < ACCOUNT_NO_MIN_LENGTH || value.length() > ACCOUNT_NO_MAX_LENGTH) return false;

        return ACCOUNT_NO_REGEX_PATTERN.matcher(value).matches();
    }
}
