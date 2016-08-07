package ro.pippo.benchmark.undertow.handlers;

import java.util.ArrayList;
import java.util.List;
import ro.pippo.benchmark.undertow.Utils;
import ro.pippo.benchmark.undertow.dao.Dao;
import ro.pippo.benchmark.undertow.dto.WorldDto;
import ro.pippo.core.HttpConstants;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;

import static ro.pippo.benchmark.undertow.Utils.CONTENT_TYPE_JSON;
import static ro.pippo.benchmark.undertow.Utils.HEADER_NAME_SERVER;
import static ro.pippo.benchmark.undertow.Utils.HEADER_VALUE_SERVER;

/**
 * Test type 5: Database updates
 *
 * Example request
 *
 * GET /updates?queries=10 HTTP/1.1
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
 * Content-Length: 315
 * Content-Type: application/json
 * Server: Example
 * Date: Wed, 17 Apr 2013 12:00:00 GMT
 *
 * [{"id":4174,"randomNumber":331},{"id":51,"randomNumber":6544},
 * {"id":4462,"randomNumber":952},{"id":2221,"randomNumber":532},
 * {"id":9276,"randomNumber":3097},{"id":3056,"randomNumber":7293},
 * {"id":6964,"randomNumber":620},{"id":675,"randomNumber":6601},
 * {"id":8414,"randomNumber":6569},{"id":2753,"randomNumber":4065}]
 */
public class Test5Handler implements RouteHandler {

  private Dao dao;

  public Test5Handler(Dao dao) {
    this.dao = dao;
  }

  @Override public void handle(RouteContext routeContext) {
    int queries = Utils.getQueriesParam(routeContext);
    List<WorldDto> dtos = new ArrayList<>(queries);
    try {

      for (int i = 0; i < queries; i++) {
        WorldDto dto = dao.getRandomWorld();
        dto.randomNumber = Utils.random();
        dtos.add(i, dao.getRandomWorld());
      }
      dao.updateRandomWorlds(dtos);

      routeContext
          .getResponse()
          .header(HttpConstants.Header.CONTENT_TYPE, CONTENT_TYPE_JSON)
          .header(HEADER_NAME_SERVER, HEADER_VALUE_SERVER)
          .json(dtos);
    } catch (Exception e) {
      routeContext.getResponse().internalError();
    }
  }
}
