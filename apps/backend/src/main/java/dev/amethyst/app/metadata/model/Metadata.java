package dev.amethyst.app.metadata.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.amethyst.app.metadata.service.ContainerResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
  private List<MetadataStream> streams = new ArrayList<MetadataStream>();
  private MetadataFormat format = new MetadataFormat();

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MetadataStream {
    @JsonProperty("r_frame_rate")
    private String framerate;
    @JsonProperty("bit_rate")
    private Long bitrate;
    @JsonProperty("sample_rate")
    private String samplerate;
    @JsonProperty("channel_layout")
    private String channelLayout;
    @JsonProperty("channels")
    private Integer channels;
    @JsonProperty("codec_name")
    private String codecName;
    @JsonProperty("codec_type")
    private String codecType;
    @JsonProperty("pix_fmt")
    private String pixelFormat;
    @JsonProperty("level")
    private String level;
    @JsonProperty("bits_per_raw_sample")
    private String bitsPerRawSample;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MetadataFormat {
    @JsonProperty("duration")
    private Double durationSeconds;
    @JsonProperty("format_name")
    private String formatName;
    @JsonProperty("format_long_name")
    private String formatLongName;
    @JsonProperty("bit_rate")
    private Long bitrate;
  }

  public static class MetadataCodecTypes {
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String SUBTITLE = "subtitle";
  }

  public MetadataStream getStream(String codecType) {
    if (this.streams == null || this.streams.isEmpty())
      return null;
    return this.streams.stream()
        .filter(stream -> codecType.equals(stream.getCodecType()))
        .findFirst()
        .orElse(null);
  }

  public MetadataStream getVideo() {
    return this.getStream(MetadataCodecTypes.VIDEO);
  }

  public MetadataStream getAudio() {
    return this.getStream(MetadataCodecTypes.AUDIO);
  }

  public String getVideoCodec() {
    MetadataStream stream = this.getVideo();
    if (stream == null)
      return null;
    return stream.getCodecName();
  }

  public String getAudioCodec() {
    MetadataStream stream = this.getAudio();
    if (stream == null)
      return null;
    return stream.getCodecName();
  }

  public Long getFramerate() {
    MetadataStream stream = this.getVideo();
    if (stream == null)
      return null;
    String framerate = stream.getFramerate();
    String[] parts = framerate.split("/");

    return Math.round(Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]));
  }

  public Container getContainer() {
    if (this.getFormat() == null)
      return Container.UNKNOWN;
    return ContainerResolver.resolve(this.getFormat().getFormatName());
  }

  public Integer getVideoBitDepth() {
    MetadataStream stream = this.getVideo();
    if (stream == null)
      return null;

    // Try bits_per_raw_sample
    try {
      Integer bitsPerRawSample = Integer.parseInt(stream.getBitsPerRawSample());
      if (bitsPerRawSample > 0)
        return bitsPerRawSample;
    } catch (NumberFormatException ignored) {
    }

    // Fallback to pix_fmt
    String pixelFormat = stream.getPixelFormat();
    if (pixelFormat == null)
      return null;

    switch (pixelFormat) {
      case "yuv420p":
      case "yuv422p":
      case "yuv444p":
        return 8;
      case "yuv420p10le":
      case "yuv422p10le":
      case "yuv444p10le":
        return 10;
      case "yuv420p12le":
      case "yuv422p12le":
      case "yuv444p12le":
        return 12;
      default:
        return null;
    }
  }

  public Integer getVideoLevel() {
    MetadataStream stream = this.getVideo();
    if (stream == null)
      return null;
    try {
      return Integer.parseInt(stream.getLevel());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public Long getBitrate() {
    // Look for bit_rate in format first
    if (this.getFormat() != null && this.getFormat().getBitrate() != null) {
      return this.getFormat().getBitrate();
    }

    // fallback to sum of video/audio stream bit_rate
    MetadataStream video = this.getVideo();
    MetadataStream audio = this.getAudio();

    try {
      Long videoBitrate = video == null || video.getBitrate() == null ? 0L : video.getBitrate();
      Long audioBitrate = audio == null || audio.getBitrate() == null ? 0L : audio.getBitrate();
      return videoBitrate + audioBitrate;
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  public Integer getAudioSamplerate() {
    MetadataStream stream = this.getAudio();
    if (stream == null)
      return null;
    try {
      return Integer.parseInt(stream.getSamplerate());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
