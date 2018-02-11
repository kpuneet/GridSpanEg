package com.puneet.newscorp;

import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class) @LargeTest public class ImagesFragmentExpressoTests {

    @Rule public FragmentTestRule<ImagesFragment> mFragmentTestRule = new FragmentTestRule<>(ImagesFragment.class);

    @Test
    public void checkImagesFragment() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imagesRecyclerView)).check(matches(isDisplayed()));
        //Awaitility, a small yet awesome lib to support async api data loads in unit tests
        await().atMost(10, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            public Boolean call() {
                return mFragmentTestRule.getFragment().imagesAdapter.getItemCount() > 0;
            }
        });
        //The sleeps accommodate if the user or test device has animations enabled in Developer Options
        SystemClock.sleep(2000);
        onView(withId(R.id.grid_spans)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 Span"))).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.grid_spans)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 Spans"))).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.grid_spans)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3 Spans"))).perform(click());
        SystemClock.sleep(3000);
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.scrollToPosition(20));
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(20, click()));
        SystemClock.sleep(1000);
        pressBack();
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.scrollToPosition(15));
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(15, click()));
        SystemClock.sleep(1000);
        pressBack();
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.scrollToPosition(5));
        onView(withId(R.id.imagesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        SystemClock.sleep(1000);
    }

    public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<MainActivity> {

        private final Class<F> mFragmentClass;
        private F mFragment;

        public FragmentTestRule(final Class<F> fragmentClass) {
            super(MainActivity.class, true, false);
            mFragmentClass = fragmentClass;
        }

        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = mFragmentClass.newInstance();
                transaction.replace(R.id.fragment_container, mFragment);
                transaction.commit();
            } catch (InstantiationException | IllegalAccessException e) {
                Assert.fail(String.format("%s: Could not insert %s into com.puneet.MainActivity: %s", getClass().getSimpleName(), mFragmentClass.getSimpleName(), e.getMessage()));
            }
        }

        public F getFragment() {
            return mFragment;
        }
    }
}
