package com.common.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "settings")
public class Setting {

    public Setting(String key) {
        this.key = key;
    }


    @Id
    @Column(name = "'key'", nullable = false, length = 128)
    private String key;

    @Column(nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private SettingCategory category;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Setting that = (Setting) o;

        if (key == null) {
            if (that.key != null) return false;
        } else if (!key.equals(that.key))
            return false;

        return true;
    }
}
