package dhbw.on.webdev.model.baseClass;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


/**
 * Base class for attributes, which are always in entities.
 * @author daniel
 */
@MappedSuperclass
public abstract class BaseClass {
    @Id
    @GeneratedValue()
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
