package dev.seanboaden.hls.lib.model;

import lombok.Data;

@Data
public class FileNameMetadata {
  public String raw;
  public String title;

  public Integer season;
  public Integer episode;
  public Integer year;

  public String resolution;
  public String quality;
  public String codec;
  public String audio;
  public String extended;
  public String hardcoded;
  public String proper;
  public String repack;
  public String widescreen;
  public String language;
  public String sbs;
  public String unrated;
  public String size;
  public String threed;

  public String group;
  public String container;

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
