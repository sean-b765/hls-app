package dev.amethyst.app.media.service;

import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.media.repository.MediaInfoRepository;

@Service
public class MediaInfoService extends AbstractCrudService<MediaInfo, String, MediaInfoRepository> {
  protected MediaInfoService(MediaInfoRepository repository) {
    super(repository);
  }
}
