package dev.seanboaden.hls.config.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import dev.seanboaden.hls.config.interceptor.WebSocketHandshakeInterceptor;
import dev.seanboaden.hls.messaging.handler.IncomingWsMessageHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
  @Autowired
  private IncomingWsMessageHandler handler;
  @Autowired
  private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

  @Override
  public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
    registry.addHandler(handler, "/ws")
        .addInterceptors(webSocketHandshakeInterceptor)
        .setAllowedOrigins("*");
  }
}
