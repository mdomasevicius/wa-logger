package lt.platform.lunar.logger;

import java.util.List;

public class CollectionResource<T> {

    private List<T> entries;

    public CollectionResource() {}

    public CollectionResource(List<T> entries) {
        this.entries = entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public List<T> getEntries() {
        return entries;
    }
}
