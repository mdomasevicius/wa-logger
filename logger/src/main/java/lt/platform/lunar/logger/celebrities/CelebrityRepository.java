package lt.platform.lunar.logger.celebrities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CelebrityRepository extends CrudRepository<Celebrity, Long> {

    List<Celebrity> findAllBySourceUrl(String sourceUrl);
}
