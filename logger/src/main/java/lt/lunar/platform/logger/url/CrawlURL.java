package lt.lunar.platform.logger.url;

import lt.lunar.platform.logger.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
class CrawlURL extends BaseEntity {

    @Column(nullable = false)
    private String crawlerId;
    @Column(nullable = false)
    private String url;

    String getCrawlerId() {
        return crawlerId;
    }

    void setCrawlerId(String crawlerId) {
        this.crawlerId = crawlerId;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
