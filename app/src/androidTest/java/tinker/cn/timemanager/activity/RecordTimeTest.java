package tinker.cn.timemanager.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tinker.cn.timemanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecordTimeTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recordTimeTest() {
        ViewInteraction imageView = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        imageView.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.fr_ll_create_activity), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText2.perform(replaceText("activity"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(R.id.fr_lv_activity_list),
                        0),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        imageView2.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.fr_ll_create_activity), isDisplayed()));
        linearLayout3.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText3.perform(replaceText("activity2"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.item_iv_activity_start),
                        withParent(allOf(withId(R.id.item_ll_activity_time_display),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        imageView3.perform(click());

        pressBack();

        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        withId(R.id.fr_lv_activity_list),
                        0),
                        isDisplayed()));
        linearLayout4.perform(click());

        ViewInteraction imageView4 = onView(
                allOf(withId(R.id.item_iv_activity_start),
                        withParent(allOf(withId(R.id.item_ll_activity_time_display),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        imageView4.perform(click());

        ViewInteraction imageView5 = onView(
                allOf(withId(R.id.item_iv_activity_start),
                        withParent(allOf(withId(R.id.item_ll_activity_time_display),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        imageView5.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
