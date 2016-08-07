package ro.pippo.benchmark.undertow.handlers;

import ro.pippo.benchmark.undertow.dto.HelloWorldDto;
import ro.pippo.core.HttpConstants;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;

import static ro.pippo.benchmark.undertow.Utils.CONTENT_TYPE_JSON;
import static ro.pippo.benchmark.undertow.Utils.HEADER_NAME_SERVER;
import static ro.pippo.benchmark.undertow.Utils.HEADER_VALUE_SERVER;

/**
 * Test type 1: JSON serialization
 *
 * Example request
 *
 * GET /json HTTP/1.1
 * Host: server
 * User-Agent: Mozilla/5.0 (X11; Linux x86_64) Gecko/20130501 Firefox/30.0 AppleWebKit/600.00 Chrome/30.0.0000.0 Trident/10.0 Safari/600.00
 * Cookie: uid=12345678901234567890; __utma=1.1234567890.1234567890.1234567890.1234567890.12; wd=2560x1600
 * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,"*&#47;"/"*&#47;";q=0.8
 * Accept-Language: en-US,en;q=0.5
 * Connection: keep-alive
 *
 * Example response
 *
 * HTTP/1.1 200 OK
 * Content-Type: application/json
 * Content-Length: 28
 * Server: Example
 * Date: Wed, 17 Apr 2013 12:00:00 GMT
 *
 * {"message":"Hello, World!"}
 */
public class Test1Handler implements RouteHandler {

  HelloWorldDto helloWorld = new HelloWorldDto();

  @Override public void handle(RouteContext routeContext) {
    routeContext
        .getResponse()
        .header(HttpConstants.Header.CONTENT_TYPE, CONTENT_TYPE_JSON)
        .header(HEADER_NAME_SERVER, HEADER_VALUE_SERVER)
        .json(helloWorld);
  }
}
