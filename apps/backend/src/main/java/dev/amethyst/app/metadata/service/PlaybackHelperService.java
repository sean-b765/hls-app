package dev.amethyst.app.metadata.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import dev.amethyst.app.media.model.MediaMetadata;
import dev.amethyst.app.metadata.model.Codecs;
import dev.amethyst.app.metadata.model.Container;
import dev.amethyst.app.metadata.model.PlaybackCompatibility;

/**
 * <p>
 * <strong>Remux</strong>: encoding from one container to another, e.g. mkv->mp4
 * </p>
 * <p>
 * <strong>Transcode</strong>: Need to be re-encoded to a different codec, e.g.
 * h265->h264
 * </p>
 * 
 * https://jellyfin.org/docs/general/clients/codec-support/
 */
@Component
public class PlaybackHelperService {
  private static final Set<Container> SUPPORTED_CONTAINERS = Set.of(Container.MP4, Container.WEBM);
  private static final Set<String> SUPPORTED_VIDEO_CODECS = Set.of(Codecs.Video.H264);
  private static final Set<String> SUPPORTED_AUDIO_CODECS = Set.of(
      Codecs.Audio.AAC,
      Codecs.Audio.MP3,
      Codecs.Audio.VORBIS,
      Codecs.Audio.FLAC);

  /**
   * @param metadata
   * @return the <code>PlaybackCompatibility</code> option based on codecs,
   *         container, quality
   */
  public PlaybackCompatibility getPlaybackCompatibility(MediaMetadata metadata) {
    if (metadata == null)
      return null;

    boolean isVideoSupported = SUPPORTED_VIDEO_CODECS.contains(metadata.getVideoCodec());
    boolean isAudioSupported = SUPPORTED_AUDIO_CODECS.contains(metadata.getAudioCodec());
    boolean isContainerSupported = SUPPORTED_CONTAINERS.contains(metadata.getContainer());

    if (!isVideoSupported && !isAudioSupported)
      return PlaybackCompatibility.TRANSCODE_BOTH;
    if (!isVideoSupported)
      return PlaybackCompatibility.TRANSCODE_VIDEO;
    if (!isAudioSupported)
      return PlaybackCompatibility.TRANSCODE_AUDIO;

    if (!isContainerSupported)
      return PlaybackCompatibility.REMUX;

    return PlaybackCompatibility.REMUX;
  }
}
