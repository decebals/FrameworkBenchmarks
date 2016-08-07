package ro.pippo.benchmark.undertow;

import ro.pippo.core.Pippo;

public class PippoLauncher {

  public static void main(String[] args) {

    // TODO Investigate server optimizations ?

    PippoApplication app = new PippoApplication();
    Pippo pippo = new Pippo(app);
    pippo.start();
  }
}