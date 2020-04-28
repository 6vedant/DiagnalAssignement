package xyz.vedant.diagnal;

import android.util.Log;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecyclerVIewTest {

    private static final String TAG = "RECYCLER_TEST";
    private static final int API2_LIMIT = 21;
    private static final int API3_LIMIT = 41;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);


    //check if recyclerview is empty or not
    @Test
    public void testSample() {
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Log.d(TAG, "Recyclerview loaded with initial data");
    }

    //check if api2 is loaded yet
    @Test
    public void checkApi2() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(API2_LIMIT, click()));

        // Match the text in an item below the fold and check that it's displayed.
        Log.d(TAG, "Api 2 populated in recyclerview");
    }

    //check if api3 is loaded yet
    @Test
    public void checkApi3() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(API3_LIMIT, click()));

        // Match the text in an item below the fold and check that it's displayed.
        Log.d(TAG, "Api 2 populated in recyclerview");
    }


}
