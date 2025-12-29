package dev.amethyst.app.lib.model;

import dev.amethyst.app.media.model.MediaType;
import lombok.Data;

@Data
public class FileNameMetadata {
  private String raw;
  private String title;

  private Integer season;
  private Integer episode;
  private Integer year;

  private String resolution;
  private String quality;
  private String codec;
  private String audio;
  private String extended;
  private String hardcoded;
  private String proper;
  private String repack;
  private String widescreen;
  private String language;
  private String sbs;
  private String unrated;
  private String size;
  private String threed;

  private String group;
  private String container;

  private MediaType mediaType;

  @Override
  public String toString() {
    return "MovieMetadata{" +
        "title='" + title + '\'' +
        ", year=" + year +
        ", resolution='" + resolution + '\'' +
        ", quality='" + quality + '\'' +
        ", codec='" + codec + '\'' +
        ", audio='" + audio + '\'' +
        ", group='" + group + '\'' +
        ", container='" + container + '\'' +
        '}';
  }

}
