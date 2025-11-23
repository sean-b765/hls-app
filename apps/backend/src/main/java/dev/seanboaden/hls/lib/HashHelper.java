package dev.seanboaden.hls.lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HashHelper {
  private ObjectMapper objectMapper = new ObjectMapper();

  public String hashObject(Object object) {
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
      String stringToHash = objectMapper.writeValueAsString(object);
      messageDigest.update(stringToHash.getBytes());
      return new String(messageDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
