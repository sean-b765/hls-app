package dev.seanboaden.hls.collection.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.media.model.Media;

public interface TvSeriesCollectionRepository extends JpaRepository<TvSeriesCollection, String> {
  public Optional<TvSeriesCollection> findByName(String name);

  @Query("SELECT m FROM Media m JOIN m.tvSeason t WHERE t.tvSeries.id = :seriesId")
  public List<Media> findMedia(@Param("seriesId") String seriesId);
}
