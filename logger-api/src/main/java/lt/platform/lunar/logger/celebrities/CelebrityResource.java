package lt.platform.lunar.logger.celebrities;

import org.hibernate.validator.constraints.NotBlank;

public class CelebrityResource {

    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    CelebrityDto toDto() {
        CelebrityDto dto = new CelebrityDto();
        dto.setFirstName(this.firstName);
        dto.setLastName(this.lastName);
        dto.setAddress(this.address);
        return dto;
    }

    static CelebrityResource toResource(CelebrityDto dto) {
        CelebrityResource resource = new CelebrityResource();
        resource.setFirstName(dto.getFirstName());
        resource.setLastName(dto.getLastName());
        resource.setAddress(dto.getAddress());
        return resource;
    }
}
