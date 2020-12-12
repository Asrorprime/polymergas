package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marketing extends AbsEntity {

    private String titleUz;

    private String titleRu;

    private String titleEn;

    @Column(columnDefinition = "text")
    private String textUz;
    @Column(columnDefinition = "text")
    private String textRu;
    @Column(columnDefinition = "text")
    private String textEn;

    @ManyToOne
    private Attachment attachment;

}
