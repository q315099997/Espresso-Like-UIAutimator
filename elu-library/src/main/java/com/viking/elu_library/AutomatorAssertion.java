package com.viking.elu_library;

import android.graphics.Rect;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertTrue;

/**
 * A collection of assertions for use with {@link DeviceAutomator}.
 */
public abstract class AutomatorAssertion {

    /**
     * Asserts that the ui element specified in {@link DeviceAutomator#onDevice(UiObjectMatcher)}
     * has text that matches the given matcher.
     *
     * @param matcher The <a href="http://hamcrest.org/JavaHamcrest/">Hamcrest</a> to match against.
     * @return
     */
    public static AutomatorAssertion text(final Matcher matcher) {
        return new AutomatorAssertion() {
            @Override
            void wrappedCheck(UiObject object) throws UiObjectNotFoundException {
                assertObjectVisible(object);
                if (!matcher.matches(object.getText())) {
                    StringDescription description = new StringDescription();
                    description.appendText("Expected ");
                    matcher.describeTo(description);
                    description.appendText(" ");
                    matcher.describeMismatch(object.getText(), description);
                    assertTrue(description.toString(), false);
                }
            }
        };
    }

    /**
     * Asserts that the ui element specified in {@link DeviceAutomator#onDevice(UiObjectMatcher)}
     * has a content description that matches the given matcher.
     *
     * @param matcher The <a href="http://hamcrest.org/JavaHamcrest/">Hamcrest</a> to match against.
     * @return
     */
    public static AutomatorAssertion contentDescription(final Matcher matcher) {
        return new AutomatorAssertion() {
            @Override
            void wrappedCheck(UiObject object) throws UiObjectNotFoundException {
                assertObjectVisible(object);
                if (!matcher.matches(object.getContentDescription())) {
                    StringDescription description = new StringDescription();
                    description.appendText("Expected ");
                    matcher.describeTo(description);
                    description.appendText(" ");
                    matcher.describeMismatch(object.getText(), description);
                    assertTrue(description.toString(), false);
                }
            }
        };
    }

    /**
     * Asserts that the foreground app is the package specified.
     *
     * @param packageName The package to check for.
     * @return
     */
    public static AutomatorAssertion foregroundAppIs(final String packageName) {
        return new AutomatorAssertion() {
            @Override
            void wrappedCheck(UiObject object) throws UiObjectNotFoundException {
                assertTrue(UiDevice.getInstance(getInstrumentation()).hasObject(By.pkg(packageName)));
            }
        };
    }

    void check(UiObject object) {
        try {
            wrappedCheck(object);
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    abstract void wrappedCheck(UiObject object) throws UiObjectNotFoundException;

    private static void assertObjectVisible(UiObject object) throws UiObjectNotFoundException {
        Rect bounds = object.getVisibleBounds();
        assertTrue("Matched view was not visible", bounds.width() > 0);
        assertTrue("Matched view was not visible", bounds.height() > 0);
    }
}
