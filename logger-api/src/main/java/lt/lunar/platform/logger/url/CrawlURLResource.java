package lt.lunar.platform.logger.url;

import org.hibernate.validator.constraints.NotBlank;

public class CrawlURLResource {

    private Long id;

    @NotBlank
    private String url;

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

    CrawlURLDto toDto() {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setUrl(url);
        dto.setId(id);
        return dto;
    }

    static CrawlURLResource toResource(CrawlURLDto dto) {
        CrawlURLResource resource = new CrawlURLResource();
        resource.setUrl(dto.getUrl());
        resource.setId(dto.getId());
        return resource;
    }

}
