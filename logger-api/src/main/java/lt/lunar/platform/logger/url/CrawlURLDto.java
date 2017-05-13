package lt.lunar.platform.logger.url;

import lt.lunar.platform.logger.key.RemoteKeyDto;

public class CrawlURLDto {

    private Long id;
    private String crawlerId;
    private String url;
    private RemoteKeyDto remoteKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrawlerId() {
        return crawlerId;
    }

    public void setCrawlerId(String crawlerId) {
        this.crawlerId = crawlerId;
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
}
