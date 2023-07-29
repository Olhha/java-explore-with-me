package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.model.ViewStats(h.app, h.uri, count(h.id)) " +
            "from EndpointHit h " +
            "where h.timestamp between :startDate and :endDate " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<ViewStats> getStatsFull(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("select new ru.practicum.model.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp between :startDate and :endDate " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ViewStats> getStatsUniqueIP(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("select new ru.practicum.model.ViewStats(h.app, h.uri, count(h.id)) " +
            "from EndpointHit h " +
            "where h.uri in :uris " +
            "and h.timestamp between :startDate and :endDate " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<ViewStats> getStatsWithUris(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.uri in :uris " +
            "and h.timestamp between :startDate and :endDate " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ViewStats> getStatsWithUrisUniqueIP(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("uris") List<String> uris);
}
