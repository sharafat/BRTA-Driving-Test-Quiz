package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @author sharafat
 * @Created 2/15/12 7:58 PM
 */
public class Sign {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "sign_set_id", foreign = true, canBeNull = false, index = true)
    private SignSet signSet;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(dataType= DataType.BYTE_ARRAY, canBeNull = false)
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SignSet getSignSet() {
        return signSet;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setSignSetAndDescriptionAndImage(SignSet signSet, String description, byte[] image) {
        this.signSet = signSet;
        this.description = description;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Sign{" +
                "id=" + id +
                ", signSet=" + signSet +
                ", description='" + description + '\'' +
                '}';
    }
}
