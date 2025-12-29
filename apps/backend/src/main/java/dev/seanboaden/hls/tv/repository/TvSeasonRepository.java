package dev.seanboaden.hls.tv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.tv.model.TvSeason;

public interface TvSeasonRepository extends JpaRepository<TvSeason, String> {
  public Optional<TvSeason> findByName(String name);

  public Optional<TvSeason> findByExternalId(String externalId);

  public Optional<TvSeason> findByTvSeriesIdAndSeason(String tvSeriesId, Integer season);

  @Query("SELECT m FROM Media m WHERE m.tvSeason.id = :seasonId")
  public List<Media> findMedia(@Param("seasonId") String seasonId);
}
