package biz.digissance.homiedemo.domain;

import com.cloudinary.StoredFile;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PhotoEntity extends BaseEntity {

    @Basic
    private String title;

    @Basic
    private String image;

    @Basic
    private String secureURL;

    @OneToOne
    @ToString.Exclude
    private ElementEntity element;

    @Transient
    @ToString.Exclude
    @Getter(lazy = true)
    private final StoredFile storedFile = getUpload();

    private StoredFile getUpload() {
        StoredFile file = new StoredFile();
        file.setPreloadedFile(image);
        return file;
    }

    public void setUpload(StoredFile file) {
        this.image = file.getPreloadedFile();
    }
}