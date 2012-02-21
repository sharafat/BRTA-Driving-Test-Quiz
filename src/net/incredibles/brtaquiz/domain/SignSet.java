package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * @author sharafat
 * @Created 2/15/12 7:51 PM
 */
public class SignSet implements Serializable {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    public SignSet() {
    }

    public SignSet(int id) {
        this.id = id;
    }

    public SignSet(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void set(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "SignSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

        SignSet signSet = (SignSet) o;

        return id == signSet.id && name.equals(signSet.name);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
