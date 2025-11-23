package dev.seanboaden.hls.video;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class TranscodingManager {

  public void transcodeToWebSocket(String inputPath, WebSocketSession session) {
    new Thread(() -> {
      // transcode to TRANSCODE_DIR
    }).start();
  }
}