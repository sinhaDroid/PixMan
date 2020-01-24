package com.example.pixman;

import androidx.test.filters.LargeTest;
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


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditImageFragmentTest {

    @Rule
//    public ActivityTestRule<EditImageFragment> mActivityRule = new ActivityTestRule<>(EditImageFragment.class, false, false);

    @Test
    public void checkIfActivityIsLaunched() {
//        mActivityRule.launchActivity(null);
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfBrushIsEnabledWhenClickedOnBrushTool() {
//        EditImageFragment editImageActivity = mActivityRule.launchActivity(null);
//        assertFalse(editImageActivity.mPhotoEditor.getBrushDrawableMode());
        onView(withText(R.string.label_brush)).perform(click());
//        assertTrue(editImageActivity.mPhotoEditor.getBrushDrawableMode());
    }

    @Test
    public void checkIfEraserIsEnabledWhenClickedOnEraserTool() {
//        mActivityRule.launchActivity(null);
        onView(withText(R.string.label_eraser)).perform(click());
        onView(withText(R.string.label_eraser_mode)).check(matches(isDisplayed()));
    }

    @Ignore("Flacky test. Need to optimize")
    public void checkIfDiscardDialogIsNotDisplayedWhenCacheIsEmpty() {
//        EditImageFragment editImageActivity = mActivityRule.launchActivity(null);
//        assertTrue(editImageActivity.mPhotoEditor.isCacheEmpty());
        onView(withId(R.id.imgClose)).perform(click());
//        assertTrue(editImageActivity.isDestroyed());
    }

    @Test
    public void checkIfDiscardDialogIsDisplayedWhenCacheIsNotEmpty() {
//        EditImageFragment editImageActivity = mActivityRule.launchActivity(null);
//        assertTrue(editImageActivity.mPhotoEditor.isCacheEmpty());
        onView(withId(R.id.imgClose)).perform(click());
        onView(withText(R.string.msg_save_image)).check(matches(isDisplayed()));
    }
}