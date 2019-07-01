package cn.bisai.repository;

import cn.bisai.domain.Vedio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Vedio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VedioRepository extends JpaRepository<Vedio, Long> {

}
