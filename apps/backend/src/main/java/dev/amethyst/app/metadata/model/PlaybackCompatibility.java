package dev.amethyst.app.metadata.model;

public enum PlaybackCompatibility {
  /**
   * <p>
   * The default. Video and audio streams can be copied because they are
   * compatible. Because we always
   * </p>
   */
  REMUX,
  /**
   * <p>
   * Audio codec needs to be transcoded
   * </p>
   */
  TRANSCODE_AUDIO,
  /**
   * <p>
   * Video codec needs to be transcoded
   * </p>
   */
  TRANSCODE_VIDEO,
  /**
   * <p>
   * Both video and audio codecs need to be transcoded
   * </p>
   */
  TRANSCODE_BOTH,
}
