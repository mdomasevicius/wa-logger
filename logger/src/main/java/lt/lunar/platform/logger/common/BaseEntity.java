package lt.lunar.platform.logger.common;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.AUTO;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
