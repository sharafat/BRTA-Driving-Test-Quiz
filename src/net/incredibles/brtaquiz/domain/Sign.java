package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Arrays;

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

    public Sign() {
    }

    public Sign(int id) {
        this.id = id;
    }

    public Sign(int id, SignSet signSet, String description, byte[] image) {
        this.id = id;
        this.signSet = signSet;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
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

    public void set(int id, SignSet signSet, String description, byte[] image) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sign sign = (Sign) o;

        return id == sign.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + signSet.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
