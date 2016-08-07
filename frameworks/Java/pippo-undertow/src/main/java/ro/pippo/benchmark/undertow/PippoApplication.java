package ro.pippo.benchmark.undertow;

import ro.pippo.benchmark.undertow.dao.Dao;
import ro.pippo.benchmark.undertow.dao.SqlDao;
import ro.pippo.benchmark.undertow.handlers.Test1Handler;
import ro.pippo.benchmark.undertow.handlers.Test2Handler;
import ro.pippo.benchmark.undertow.handlers.Test3Handler;
import ro.pippo.benchmark.undertow.handlers.Test4Handler;
import ro.pippo.benchmark.undertow.handlers.Test5Handler;
import ro.pippo.benchmark.undertow.handlers.Test6Handler;
import ro.pippo.core.Application;
import ro.pippo.trimou.TrimouTemplateEngine;

public class PippoApplication extends Application {

  @Override
  protected void onInit() {
    setTemplateEngine(new TrimouTemplateEngine());

    Dao mysql = new SqlDao(getPippoSettings(), "db.mysql");
    Dao psql = new SqlDao(getPippoSettings(), "db.psql");

    GET("/json", new Test1Handler());

    GET("/mysql/db", new Test2Handler(mysql));
    GET("/psql/db", new Test2Handler(psql));

    GET("/mysql/queries", new Test3Handler(mysql));
    GET("/psql/queries", new Test3Handler(psql));

    GET("/mysql/fortunes", new Test4Handler(mysql));
    GET("/psql/fortunes", new Test4Handler(psql));

    GET("/mysql/updates", new Test5Handler(mysql));
    GET("/psql/updates", new Test5Handler(psql));

    GET("/plaintext", new Test6Handler());
  }
}