package cn.bisai.repository;

import cn.bisai.domain.Film;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


/**
 * Spring Data  repository for the Film entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Transactional
    @Modifying
    @Query("update Film set curtime=?2 where id=?1")
    void updateCurtime(Long id,Long curtime);

    @Transactional
    @Modifying
    @Query("update Film set playing=?2 where id=?1")
    void updatePlay(Long id,Boolean flag);
}
