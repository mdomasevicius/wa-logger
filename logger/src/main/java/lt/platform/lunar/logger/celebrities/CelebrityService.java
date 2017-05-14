package lt.platform.lunar.logger.celebrities;

import lt.platform.lunar.logger.common.NotFoundException;
import lt.platform.lunar.logger.url.CrawlURL;
import lt.platform.lunar.logger.url.CrawlURLRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
interface CelebrityService {

    CelebrityDto findOne(Long id);

    void create(Long urlId, CelebrityDto celebrity);

    List<CelebrityDto> findByUrl(Long urlId);

}
