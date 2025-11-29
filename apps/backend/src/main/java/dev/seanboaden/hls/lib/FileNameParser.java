package dev.seanboaden.hls.lib;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class FileNameParser {
  private static final Pattern SEASON_EP = Pattern.compile("(?i)(S(?<s>[0-9]{1,2})E(?<e>[0-9]{2}))");

  private static final Pattern YEAR = Pattern.compile("(?<!\\d)(?<year>19\\d{2}|20[\\d]\\d)(?!\\d)");

  private static final Pattern RES = Pattern.compile("(?i)(?<res>[0-9]{3,4}p)");

  private static final Pattern CODEC = Pattern.compile("(?i)(?<codec>xvid|[hx]\\.?(?:264|265))");

  private static final Pattern QUALITY = Pattern
      .compile(
          "(?i)(PPV\\.)?[HP]DTV|(?:HD)?CAM|B[DR]Rip|(?:HD-?)?TS|(?:PPV )?WEB-?DL(?: DVDRip)?|HDRip|DVDRip|CamRip|W[EB]BRip|BluRay|DvDScr|telesync|hdtv");

  private static final Pattern AUDIO = Pattern
      .compile("(?i)(MP3|DD5\\.?1|Dual[\\- ]Audio|LiNE|DTS|AAC[.-]LC|AAC(?:\\.?2\\.0)?|AC3(?:\\.5\\.1)?)");

  private static final Pattern GROUP = Pattern.compile("-(?<group>[A-Za-z0-9.\\[\\]_-]+)$");

  private static final Pattern CONTAINER = Pattern.compile("(?i)\\.(?<ext>mp4|mkv|avi)$");

  public String getFileName(String path) {
    if (path == null)
      return null;
    return Paths.get(path).getFileName().toString();
  }

  public FileNameMetadata parse(String filename) {
    FileNameMetadata md = new FileNameMetadata();
    md.raw = filename;
    String base = filename;

    // cursor which will help in extracting the title later
    int cursor = base.length();

    System.out.println(base);
    Matcher ext = CONTAINER.matcher(base);
    if (ext.find()) {
      md.container = ext.group("ext");
      base = base.substring(0, ext.start());
      if (ext.start() < cursor)
        cursor = ext.start();
    }

    // extract season/episode
    Matcher se = SEASON_EP.matcher(base);
    if (se.find()) {
      md.season = Integer.parseInt(se.group("s"));
      md.episode = Integer.parseInt(se.group("e"));
      base = base.substring(0, se.start()) + base.substring(se.end());
      if (se.start() < cursor)
        cursor = se.start();
    }

    // extract year
    Matcher my = YEAR.matcher(base);
    if (my.find()) {
      md.year = Integer.parseInt(my.group("year"));
      base = base.replace(my.group(), "");
      if (my.start() < cursor)
        cursor = my.start();
    }

    // extract resolution
    Matcher mr = RES.matcher(base);
    if (mr.find()) {
      md.resolution = mr.group();
      base = base.replace(mr.group(), "");
      if (mr.start() < cursor)
        cursor = mr.start();
    }

    // Matcher mq = QUALITY.matcher(base);
    // if (mq.find()) {
    // md.quality = mq.group();
    // base = base.replace(mq.group(), "");
    // if (mq.start() < cursor)
    // cursor = mq.start();
    // }

    // extract codec
    Matcher mc = CODEC.matcher(base);
    if (mc.find()) {
      md.codec = mc.group();
      base = base.replace(mc.group(), "");
      if (mc.start() < cursor)
        cursor = mc.start();
    }

    // extract audio
    Matcher ma = AUDIO.matcher(base);
    if (ma.find()) {
      md.audio = ma.group();
      base = base.replace(ma.group(), "");
      if (ma.start() < cursor)
        cursor = ma.start();
    }

    // whatever remains is most likely the title
    md.title = cleanTitle(base, cursor);

    return md;
  }

  private String cleanTitle(String raw, int omitAfter) {
    if (raw == null)
      return null;

    raw = raw.substring(0, omitAfter);
    raw = raw.replaceAll("[.()]+", " ");
    raw = raw.replaceAll("\\s+", " ").trim();
    raw = raw.replaceAll("[-_ ]+$", "");
    raw = raw.replaceAll("^[- ]+", "");

    return raw.trim();
  }

}
