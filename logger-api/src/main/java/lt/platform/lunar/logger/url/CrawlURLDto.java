package lt.platform.lunar.logger.url;

import lt.platform.lunar.logger.celebrities.CelebrityDto;
import lt.platform.lunar.logger.key.RemoteKeyDto;

import java.util.ArrayList;
import java.util.List;

public class CrawlURLDto {

    private Long id;
    private String url;
    private RemoteKeyDto remoteKey;
    private List<CelebrityDto> celebrities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RemoteKeyDto getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(RemoteKeyDto remoteKey) {
        this.remoteKey = remoteKey;
    }

    public List<CelebrityDto> getCelebrities() {
        return celebrities;
    }

    public void setCelebrities(List<CelebrityDto> celebrities) {
        this.celebrities = celebrities;
    }

    public void addCelebrity(CelebrityDto celebrity) {
        celebrities.add(celebrity);
    }
}
