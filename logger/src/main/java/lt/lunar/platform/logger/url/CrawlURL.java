package lt.lunar.platform.logger.url;

import lt.lunar.platform.logger.common.BaseEntity;
import lt.lunar.platform.logger.key.RemoteKey;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
class CrawlURL extends BaseEntity {

    @Column(nullable = false)
    private String url;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "category_id")
    private RemoteKey remoteKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RemoteKey getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(RemoteKey remoteKey) {
        this.remoteKey = remoteKey;
    }
}
