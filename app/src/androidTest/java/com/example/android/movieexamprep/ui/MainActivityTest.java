package com.example.android.movieexamprep.ui;

import android.content.Intent;

import com.example.android.movieexamprep.R;
import com.example.android.movieexamprep.model.Movie;
import com.example.android.movieexamprep.util.FakeMovieDataSource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.runner.intercepting.SingleActivityFactory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MovieViewModel movieViewModel;
    private MutableLiveData<PagedList<Movie>> movieLiveData = new MutableLiveData<>();
    private MutableLiveData<String> movieLiveNetworkErrors = new MutableLiveData<>();

    private FakeMovieDataSource fakeMovieDataSource = new FakeMovieDataSource();

    private final String movieSearchQuery = "fake search query";
    private final String expectedLanguageSetting = "en";

    /**
     * Intercept the activity launch and mock the ViewModel.
     *
     * Alternatively: extract this logic into a fragment and then
     * set the viewmodel before launching the fragment. (see githubbrowser sample)
     */
    private SingleActivityFactory<MainActivity> injectedFactory =
            new SingleActivityFactory<MainActivity>(MainActivity.class) {
        @Override
        protected MainActivity create(Intent intent) {
            MainActivity activity = new MainActivity();
            movieViewModel =  mock(MovieViewModel.class);
            when(movieViewModel.getMovies()).thenReturn(movieLiveData);
            when(movieViewModel.getNetworkErrors()).thenReturn(movieLiveNetworkErrors);
            activity.viewModel = movieViewModel;
            return activity;
        }
    };


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<MainActivity>(injectedFactory, false, true);

    @Test
    public void spinner_isDisplayed_whenResultsAreLoading(){
        onView(withId(R.id.search_movie))
                .perform(typeText(movieSearchQuery))
                .perform(pressImeActionButton());

        onView(withId(R.id.spinner)).check(matches(isDisplayed()));
    }

    @Test
    public void spinner_isNotDisplayed_whenEmptyResultsHaveBeenLoaded(){
        mockEmptyMoviePagedListReturned();

        onView(withId(R.id.search_movie))
                .perform(typeText(movieSearchQuery))
                .perform(pressImeActionButton());

        onView(withId(R.id.spinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptyList)).check(matches(isDisplayed()));
    }

    @Test
    public void noResultsFound_isDisplayed_whenNoSearchResults() {
        mockEmptyMoviePagedListReturned();

        onView(withId(R.id.search_movie))
                .perform(typeText(movieSearchQuery))
                .perform(pressImeActionButton());

        onView(withId(R.id.emptyList)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void movieResults_areDisplayed_whenNonZeroResultsReturned() {
        mockFakeMovieResultsReturned();

        onView(withId(R.id.search_movie))
                .perform(typeText(movieSearchQuery))
                .perform(pressImeActionButton());

        onView(withId(R.id.emptyList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.spinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withText("fake title 2")).check((matches(isDisplayed())));
        onView(withText("fake overview 2")).check((matches(isDisplayed())));

    }

    /**
     * Responds to query string with an empty list of movies.
     */
    private void mockEmptyMoviePagedListReturned(){
        @SuppressWarnings("unchecked")
        PagedList<Movie> emptyMovieResult = (PagedList<Movie>) mock(PagedList.class);
        when(emptyMovieResult.size()).thenReturn(0);

        doAnswer((Answer<Void>) invocation -> {
            movieLiveData.postValue(emptyMovieResult);
            return null;
        }).when(movieViewModel).searchMovie(movieSearchQuery, expectedLanguageSetting);
    }

    /**
     * Responds to the query string with a list of fake movies. See FakeMovieDataSource for details.
     */
    private void mockFakeMovieResultsReturned() {
        PagedList<Movie> movies =
                new PagedList.Builder<>(fakeMovieDataSource, 20)
                        .setNotifyExecutor(mActivityRule.getActivity().getMainExecutor())
                        .setFetchExecutor(mActivityRule.getActivity().getMainExecutor())
                        .build();

        doAnswer((Answer<Void>) invocation -> {
            movieLiveData.postValue(movies);
            return null;
        }).when(movieViewModel).searchMovie(movieSearchQuery, expectedLanguageSetting);
    }

}