package dev.amethyst.app.tv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.tv.model.TvSeries;

public interface TvSeriesRepository extends JpaRepository<TvSeries, String> {
  public Optional<TvSeries> findByName(String name);

  public Optional<TvSeries> findByExternalId(String externalId);

  @Query("SELECT m FROM Media m JOIN m.tvSeason t WHERE t.tvSeries.id = :seriesId")
  public List<Media> findMedia(@Param("seriesId") String seriesId);
}
