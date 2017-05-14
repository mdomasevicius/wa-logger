package lt.platform.lunar.logger.key;

interface RemoteKeyService {

    RemoteKeyDto findOne(Long urlId);

    void createKeyForUrl(Long urlId, String remoteKey);
}
