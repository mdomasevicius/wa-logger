package lt.platform.lunar.logger.celebrities;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
interface CelebrityService {

    void create(Long urlId, CelebrityDto celebrity);

    List<CelebrityDto> findByUrl(Long urlId);

}
