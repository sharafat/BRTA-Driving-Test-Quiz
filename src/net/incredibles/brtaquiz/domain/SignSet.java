package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author sharafat
 * @Created 2/15/12 7:51 PM
 */
public class SignSet {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SignSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
