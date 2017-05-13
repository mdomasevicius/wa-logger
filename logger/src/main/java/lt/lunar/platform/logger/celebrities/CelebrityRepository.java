package lt.lunar.platform.logger.celebrities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelebrityRepository extends CrudRepository<Celebrity, Long> {
}
