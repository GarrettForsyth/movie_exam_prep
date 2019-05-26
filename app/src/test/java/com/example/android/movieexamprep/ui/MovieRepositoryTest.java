package com.example.android.movieexamprep.ui;

import com.example.android.movieexamprep.api.MovieService;
import com.example.android.movieexamprep.data.MovieRepository;
import com.example.android.movieexamprep.db.MovieDao;
import com.example.android.movieexamprep.db.MovieLocalCache;
import com.example.android.movieexamprep.model.Movie;
import com.example.android.movieexamprep.util.FakeMovieDataSourceFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import okhttp3.Call;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MovieRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieLocalCache cache = mock(MovieLocalCache.class);
    private MovieService service = mock(MovieService.class);

    private MovieRepository movieRepository = new MovieRepository(service, cache);

    @Before
    public void init() {

    }

    @Test
    public void loadMovieFromNetwork() {
    }

}
