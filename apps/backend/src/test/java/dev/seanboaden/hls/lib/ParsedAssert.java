package dev.seanboaden.hls.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParsedAssert {
  private final FileNameMetadata actual;

  public ParsedAssert(FileNameMetadata actual) {
    this.actual = actual;
  }

  public ParsedAssert title(String expected) {
    assertEquals(expected, actual.title, "title mismatch");
    return this;
  }

  public ParsedAssert year(Integer expected) {
    assertEquals(expected, actual.getYear(), "year mismatch");
    return this;
  }

  public ParsedAssert season(Integer expected) {
    assertEquals(expected, actual.getSeason(), "season mismatch");
    return this;
  }

  public ParsedAssert episode(Integer expected) {
    assertEquals(expected, actual.getEpisode(), "episode mismatch");
    return this;
  }

  public ParsedAssert resolution(String expected) {
    assertEquals(expected, actual.getResolution(), "resolution mismatch");
    return this;
  }

  // Add more fields as needed...

  public ParsedAssert all(String title, Integer year, Integer season, Integer episode) {
    return this.title(title)
        .year(year)
        .season(season)
        .episode(episode);
  }
}