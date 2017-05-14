package lt.platform.lunar.logger.url;

import lt.platform.lunar.logger.celebrities.Celebrity;
import lt.platform.lunar.logger.common.BaseEntity;
import lt.platform.lunar.logger.key.RemoteKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
public class CrawlURL extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String url;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "category_id")
    private RemoteKey remoteKey;

    @OneToMany
    @JoinColumn(name = "celebrity_id")
    private List<Celebrity> celebrities = new ArrayList<>();

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

    public List<Celebrity> getCelebrities() {
        return celebrities;
    }

    public void setCelebrities(List<Celebrity> celebrities) {
        this.celebrities = celebrities;
    }

    public void addCelebrity(Celebrity celebrity) {
        celebrities.add(celebrity);
    }
}
