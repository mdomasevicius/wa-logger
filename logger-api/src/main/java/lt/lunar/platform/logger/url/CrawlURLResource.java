package lt.lunar.platform.logger.url;

import org.hibernate.validator.constraints.NotBlank;

public class CrawlURLResource {

    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    CrawlURLDto toDto() {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setUrl(url);
        return dto;
    }

    static CrawlURLResource toResource(CrawlURLDto dto) {
        CrawlURLResource resource = new CrawlURLResource();
        resource.setUrl(dto.getUrl());
        return resource;
    }

}
