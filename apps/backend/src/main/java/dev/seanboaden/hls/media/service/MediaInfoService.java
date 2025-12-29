package dev.seanboaden.hls.media.service;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.media.repository.MediaInfoRepository;

@Service
public class MediaInfoService extends AbstractCrudService<MediaInfo, String, MediaInfoRepository> {
  protected MediaInfoService(MediaInfoRepository repository) {
    super(repository);
  }
}
