package dev.seanboaden.hls.collection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.seanboaden.hls.media.Media;

public interface TvSeasonCollectionRepository extends JpaRepository<TvSeasonCollection, String> {
  public Optional<TvSeasonCollection> findByName(String name);

  public Optional<TvSeasonCollection> findByExternalId(String externalId);

  public Optional<TvSeasonCollection> findByTvSeriesIdAndSeason(String tvSeriesId, Integer season);

  @Query("SELECT m FROM Media m WHERE m.tvSeason.id = :seasonId")
  public List<Media> findMedia(@Param("seasonId") String seasonId);
}
