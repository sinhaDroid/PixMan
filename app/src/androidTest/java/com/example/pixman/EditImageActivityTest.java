package com.example.pixman;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditImageActivityTest {

    @Rule
    public ActivityTestRule<EditImageActivity> mActivityRule = new ActivityTestRule<>(EditImageActivity.class, false, false);

    @Test
    public void checkIfActivityIsLaunched() {
        mActivityRule.launchActivity(null);
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfBrushIsEnabledWhenClickedOnBrushTool() {
        EditImageActivity editImageActivity = mActivityRule.launchActivity(null);
        assertFalse(editImageActivity.mPhotoEditor.getBrushDrawableMode());
        onView(withText(R.string.label_brush)).perform(click());
        assertTrue(editImageActivity.mPhotoEditor.getBrushDrawableMode());
    }

    @Test
    public void checkIfEraserIsEnabledWhenClickedOnEraserTool() {
        mActivityRule.launchActivity(null);
        onView(withText(R.string.label_eraser)).perform(click());
        onView(withText(R.string.label_eraser_mode)).check(matches(isDisplayed()));
    }

    @Ignore("Flacky test. Need to optimize")
    public void checkIfDiscardDialogIsNotDisplayedWhenCacheIsEmpty() {
        EditImageActivity editImageActivity = mActivityRule.launchActivity(null);
        assertTrue(editImageActivity.mPhotoEditor.isCacheEmpty());
        onView(withId(R.id.imgClose)).perform(click());
        assertTrue(editImageActivity.isDestroyed());
    }

    @Test
    public void checkIfDiscardDialogIsDisplayedWhenCacheIsNotEmpty() {
        EditImageActivity editImageActivity = mActivityRule.launchActivity(null);
        assertTrue(editImageActivity.mPhotoEditor.isCacheEmpty());
        onView(withId(R.id.imgClose)).perform(click());
        onView(withText(R.string.msg_save_image)).check(matches(isDisplayed()));
    }
}