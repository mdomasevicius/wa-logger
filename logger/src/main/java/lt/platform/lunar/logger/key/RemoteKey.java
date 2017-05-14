package lt.platform.lunar.logger.key;

import lt.platform.lunar.logger.common.BaseEntity;

import javax.persistence.Entity;

@Entity
public class RemoteKey extends BaseEntity {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
