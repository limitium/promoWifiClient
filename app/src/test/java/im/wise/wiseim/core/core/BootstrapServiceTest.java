

package me.loc2.loc2me.core.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import me.loc2.loc2me.core.ApiService;
import me.loc2.loc2me.core.models.CheckIn;
import me.loc2.loc2me.core.models.News;
import me.loc2.loc2me.core.models.User;
import me.loc2.loc2me.core.UserAgentProvider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of {@link me.loc2.loc2me.core.ApiService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BootstrapServiceTest {

    /**
     * Create reader for string
     *
     * @param value
     * @return input stream reader
     * @throws IOException
     */
    private static BufferedReader createReader(String value) throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                value.getBytes(HttpRequest.CHARSET_UTF8))));
    }

    @Mock
    private HttpRequest request;

    private ApiService service;

    /**
     * Set up default mocks
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {
        service = new ApiService("foo", new UserAgentProvider()) {
            protected HttpRequest execute(HttpRequest request) throws IOException {
                return BootstrapServiceTest.this.request;
            }
        };
        doReturn(true).when(request).ok();
    }

    /**
     * Verify getting users with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getUsersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<User> users = service.getUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    /**
     * Verify getting wise with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getContentEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        assertNotNull(content);
        assertTrue(content.isEmpty());
    }

    /**
     * Verify getting checkins with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getReferrersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<CheckIn> referrers = service.getCheckIns();
        assertNotNull(referrers);
        assertTrue(referrers.isEmpty());
    }
}
