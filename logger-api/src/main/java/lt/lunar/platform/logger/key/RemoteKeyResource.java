package lt.lunar.platform.logger.key;

public class RemoteKeyResource {

    private String remoteKey;

    public RemoteKeyResource() {}

    public RemoteKeyResource(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    public String getRemoteKey() {
        return remoteKey;
    }

    public void setRemoteKey(String remoteKey) {
        this.remoteKey = remoteKey;
    }

    static RemoteKeyResource toResource(RemoteKeyDto dto) {
        return new RemoteKeyResource(dto.getKey());
    }
}
