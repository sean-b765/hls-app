package dev.seanboaden.hls.lib;

import org.junit.jupiter.api.Test;

import dev.seanboaden.hls.lib.model.FileNameMetadata;
import dev.seanboaden.hls.lib.service.FileNameParser;

public class FileNameParserTests {
  private FileNameParser fileNameParser = new FileNameParser();

  @Test
  public void testFileNameParser() {
    expectFrom("I, Robot.mp4")
        .title("I, Robot");
    expectFrom("Breaking Bad s02ep11 720p brrip.sujaidr.mkv")
        .title("Breaking Bad");
    expectFrom("Pi (1998).mkv")
        .title("Pi");
    expectFrom("Catch-22.1970.DVDRip.mp4")
        .title("Catch-22")
        .year(1970);
    expectFrom("F Is for Family (2015–2021)")
        .title("F Is For Family");
    expectFrom("30 Rock (2006-2013)")
        .title("30 Rock");
    expectFrom("Mr. Robot")
        .title("Mr Robot");
    expectFrom("M_A_S_H")
        .title("M_A_S_H");
    expectFrom("Marvel’s Agent Carter")
        .title("Marvel’s Agent Carter");
    expectFrom("Law & Order")
        .title("Law & Order");
    expectFrom("The.Day.Of.The.Jackal.S01E07.1080p.WEB-DL.AAC2.0.H264-P147YPU5.mkv")
        .title("The Day Of The Jackal")
        .season(1)
        .episode(7);
    expectFrom("Game of Thrones S05E09 - The Dance of Dragons.mkv")
        .title("Game Of Thrones")
        .season(5)
        .episode(9);
    expectFrom("Demon Slayer - Kimetsu no Yaiba - s05e01 - To Defeat Muzan Kibutsuji WEBDL-1080p Proper.mkv")
        .title("Demon Slayer - Kimetsu No Yaiba")
        .season(5)
        .episode(1);
    expectFrom("Demon Slayer - Kimetsu no Yaiba - S05E01 - To Defeat Muzan Kibutsuji WEBDL-1080p Proper.mkv")
        .year(null)
        .title("Demon Slayer - Kimetsu No Yaiba");
    expectFrom("12.Angry.Men.1957.720p.BRrip.x264.YIFY.mp4")
        .title("12 Angry Men")
        .year(1957);
    expectFrom("Sunset.Blvd.1950.720p.BluRay.999MB.HQ.x265.10bit-GalaxyRG.mkv")
        .title("Sunset Blvd")
        .year(1950);
    expectFrom("Tenet.2020.1080p.HDRip.x264.AAC2.0-SHITBOX.mkv")
        .title("Tenet")
        .year(2020);
    expectFrom("Better Call Saul - S04E02 - Breathe.mkv")
        .title("Better Call Saul")
        .season(4)
        .episode(2);
    expectFrom("The.Blair.Witch.Project.1999.1080p.BluRay.H264.AAC-RARBG.mp4")
        .title("The Blair Witch Project")
        .year(1999);
    expectFrom("Source Code (2011).mp4")
        .title("Source Code")
        .year(2011);
    expectFrom("The Fellowship of the Ring (2001).mp4")
        .title("The Fellowship Of The Ring")
        .year(2001);
    expectFrom("SPY x FAMILY - S03E01 - BERLINT PANIC + THE INFORMANT AND NIGHTFALL WEBDL-1080p")
        .title("SPY X FAMILY")
        .season(3)
        .episode(1);
    expectFrom("Game of Thrones S02E05  The Ghost Of Harrenhal (1080p x265 10bit Joy).mkv")
        .title("Game Of Thrones")
        .season(2)
        .episode(5);
    expectFrom("A.Good.Girls.Guide.to.Murder.S01E06.720p.STAN.WEBRip.x264-GalaxyTV.mkv")
        .title("A Good Girls Guide To Murder")
        .season(1)
        .episode(6);
    expectFrom("House of the Dragon - S02E08 - The Queen Who Ever Was WEBDL-1080p.mkv")
        .title("House Of The Dragon")
        .season(2)
        .episode(8);
    expectFrom("Taxi Driver.mkv")
        .title("Taxi Driver");
    expectFrom("Star.Wars.Episode.VIII.The.Last.Jedi.2017.1080p.BrRip.6CH.x265.HEVC-PSA.mp4")
        .title("Star Wars Episode VIII The Last Jedi");
    expectFrom("Se7en(1995)1080p.BrRip.x264.YIFY.mp4")
        .title("Se7en")
        .year(1995);
    expectFrom("Cool Movie")
        .title("Cool Movie");
    expectFrom("M-A-S-H.S01E05.1972.mkv")
        .title("M-A-S-H")
        .year(1972);
  }

  private ParsedAssert expectFrom(String path) {
    FileNameMetadata fileNameMetadata = extractForTesting(path);
    return new ParsedAssert(fileNameMetadata);
  }

  private FileNameMetadata extractForTesting(String path) {
    String fileName = this.fileNameParser.getFileName(path);
    return this.fileNameParser.parse(fileName);
  }
}
