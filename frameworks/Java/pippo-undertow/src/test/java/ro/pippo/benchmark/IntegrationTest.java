package ro.pippo.benchmark;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pippo.benchmark.model.HelloWorld;
import ro.pippo.core.HttpConstants;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Integration test to verify that everything works.
 *
 * Prerequisites:
 * 1) Have all databases running
 * 2) Have the server started up
 */
public class IntegrationTest {

  public static void main(String[] args) throws IOException {
    new IntegrationTest().run();
  }

  private OkHttpClient client;

  private void run() throws IOException {
    client = new OkHttpClient();

    assertJson(execute("/json"));

    assertJson(execute("/mysql/db"));
    assertJson(execute("/psql/db"));
    assertJson(execute("/mongo/db"));

    assertJson(execute("/mysql/queries"));
    assertJson(execute("/mysql/queries?queries=-1"));
    assertJson(execute("/mysql/queries?queries=ABC"));
    assertJson(execute("/mysql/queries?queries=600"));
    assertJson(execute("/psql/queries"));
    assertJson(execute("/psql/queries?queries=-1"));
    assertJson(execute("/psql/queries?queries=ABC"));
    assertJson(execute("/psql/queries?queries=600"));
    assertJson(execute("/mongo/queries"));
    assertJson(execute("/mongo/queries?queries=-1"));
    assertJson(execute("/mongo/queries?queries=ABC"));
    assertJson(execute("/mongo/queries?queries=600"));

    assertHtml(execute("/mysql/fortunes"));
    assertHtml(execute("/psql/fortunes"));
    assertHtml(execute("/mongo/fortunes"));

    assertJson(execute("/mysql/updates"));
    assertJson(execute("/mysql/updates?queries=-1"));
    assertJson(execute("/mysql/updates?queries=ABC"));
    assertJson(execute("/mysql/updates?queries=600"));
    assertJson(execute("/psql/updates"));
    assertJson(execute("/psql/updates?queries=-1"));
    assertJson(execute("/psql/updates?queries=ABC"));
    assertJson(execute("/psql/updates?queries=600"));
    assertJson(execute("/mongo/updates"));
    assertJson(execute("/mongo/updates?queries=-1"));
    assertJson(execute("/mongo/updates?queries=ABC"));
    assertJson(execute("/mongo/updates?queries=600"));

    assertHelloWorld(execute("/plaintext"));
  }

  private void assertHelloWorld(Response response) throws IOException {
    assertEquals(200, response.code());
    assertTrue(response.header(HttpConstants.Header.CONTENT_TYPE)
        .contains(Utils.CONTENT_TYPE_TEXT_PLAIN));
    assertEquals(HelloWorld.MESSAGE, response.body().string());
  }

  private void assertJson(Response response) {
    assertEquals(200, response.code());
    assertTrue(response.header(HttpConstants.Header.CONTENT_TYPE)
        .contains(Utils.CONTENT_TYPE_JSON));
  }

  private void assertHtml(Response response) {
    assertEquals(200, response.code());
    assertTrue(response.header(HttpConstants.Header.CONTENT_TYPE)
        .contains("text/html"));
  }

  private Response execute(String uri) throws IOException {
    Request request = new Request.Builder()
        .url("http://localhost:8080" + uri)
        .build();

    return client.newCall(request).execute();
  }
}
