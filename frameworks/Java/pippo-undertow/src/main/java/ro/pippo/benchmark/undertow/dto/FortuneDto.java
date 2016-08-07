package ro.pippo.benchmark.undertow.dto;

public class FortuneDto {

  public int id;
  public String message;

  public FortuneDto(int id, String message) {
    this.id = id;
    this.message = message;
  }
}
