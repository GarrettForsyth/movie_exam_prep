package com.example.android.movieexamprep.ui;

import com.example.android.movieexamprep.data.MovieRepository;
import com.example.android.movieexamprep.model.Movie;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("ALL")
@RunWith(JUnit4.class)
public class MovieViewModelTest {

    private MovieRepository repository = mock(MovieRepository.class);
    private MovieViewModel movieViewModel = new MovieViewModel(repository);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testNull() {
        assertThat(movieViewModel.getRepository(), notNullValue());
        verify(repository, never()).search(anyString(), anyString());
    }

    @Test
    public void dontFetchWithoutObservers() {
        movieViewModel.searchMovie("lord of the rings", "en");
        verify(repository,never()).search(anyString(), anyString());
    }

    @Test
    public void fetchWhenObserved(){
        movieViewModel.searchMovie("lord of the rings", "en");
        movieViewModel.getMovies().observeForever((Observer<PagedList<Movie>>)mock(Observer.class));
        verify(repository).search("lord of the rings", "en");
    }

    @Test
    public void changeWhileObserved() {
        movieViewModel.getMovies().observeForever((Observer<PagedList<Movie>>)mock(Observer.class));

        movieViewModel.searchMovie("lord of the rings", "en");
        movieViewModel.searchMovie("harry potter", "en");

        verify(repository).search("lord of the rings", "en");
        verify(repository).search("harry potter", "en");
    }

    @Test
    public void reset() {
        Observer<PagedList<Movie>> observer = (Observer<PagedList<Movie>>)mock(Observer.class);
        movieViewModel.getMovies().observeForever(observer);
        verifyNoMoreInteractions(observer);

        movieViewModel.searchMovie("lord of the rings", "en");

    }
}
