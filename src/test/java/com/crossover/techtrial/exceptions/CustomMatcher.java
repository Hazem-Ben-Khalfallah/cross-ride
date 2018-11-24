package com.crossover.techtrial.exceptions;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatcher extends TypeSafeMatcher<CustomException> {

    public static CustomMatcher hasCode(CustomErrorCode item) {
        return new CustomMatcher(item);
    }

    private CustomErrorCode foundErrorCode;
    private final CustomErrorCode expectedErrorCode;

    private CustomMatcher(CustomErrorCode expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final CustomException exception) {
        foundErrorCode = exception.getCustomErrorCode();
        return foundErrorCode.equals(expectedErrorCode);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(foundErrorCode)
                .appendText(" was not found instead of ")
                .appendValue(expectedErrorCode);
    }
}