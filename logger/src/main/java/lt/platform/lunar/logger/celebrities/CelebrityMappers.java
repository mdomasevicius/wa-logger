package lt.platform.lunar.logger.celebrities;

public class CelebrityMappers {

    public static CelebrityDto toDto(Celebrity celebrity) {
        CelebrityDto dto = new CelebrityDto();
        dto.setId(celebrity.getId());
        dto.setFirstName(celebrity.getFirstName());
        dto.setLastName(celebrity.getLastName());
        dto.setAddress(celebrity.getAddress());
        return dto;
    }

    public static Celebrity fromDto(CelebrityDto dto) {
        Celebrity celebrity = new Celebrity();
        celebrity.setId(dto.getId());
        celebrity.setFirstName(dto.getFirstName());
        celebrity.setLastName(dto.getLastName());
        celebrity.setAddress(dto.getAddress());
        return celebrity;
    }

}
