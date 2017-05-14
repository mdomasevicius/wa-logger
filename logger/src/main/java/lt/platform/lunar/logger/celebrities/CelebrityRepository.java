package lt.platform.lunar.logger.celebrities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CelebrityRepository extends CrudRepository<Celebrity, Long> {
}
