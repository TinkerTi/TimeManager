package tinker.cn.timemanager.ui.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tinker.cn.timemanager.R;

import static android.support.test.espresso.Espresso.onView;
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
public class AddSequenceTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addSequenceTest() {
        ViewInteraction addImageButton = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        addImageButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.fr_ll_create_activity), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText2.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction addImageButton2 = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        addImageButton2.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.fr_ll_create_activity), isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText3.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.fr_et_activity_name), isDisplayed()));
        editText4.perform(replaceText("2"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction addImageButton3 = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        addImageButton3.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.fr_ll_create_group), isDisplayed()));
        linearLayout3.perform(click());

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText5.perform(click());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText6.perform(replaceText("g1"), closeSoftKeyboard());

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction addImageButton4 = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        addImageButton4.perform(click());

        ViewInteraction linearLayout4 = onView(
                allOf(withId(R.id.fr_ll_create_group), isDisplayed()));
        linearLayout4.perform(click());

        ViewInteraction editText7 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText7.perform(click());

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText8.perform(replaceText("g2"), closeSoftKeyboard());

        ViewInteraction button4 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button4.perform(click());

        ViewInteraction addImageButton5 = onView(
                allOf(withId(R.id.fr_iv_add_activity), isDisplayed()));
        addImageButton5.perform(click());

        ViewInteraction linearLayout5 = onView(
                allOf(withId(R.id.fr_ll_create_group), isDisplayed()));
        linearLayout5.perform(click());

        ViewInteraction editText9 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText9.perform(click());

        ViewInteraction editText10 = onView(
                allOf(withId(R.id.fr_et_group_name), isDisplayed()));
        editText10.perform(replaceText("g3"), closeSoftKeyboard());

        ViewInteraction button5 = onView(
                allOf(withId(android.R.id.button1), withText("确定"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button5.perform(click());

    }

}
