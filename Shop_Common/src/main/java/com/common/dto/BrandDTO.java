package com.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BrandDTO {

    private Long id;

    private List<Long> parentIds;

    private String name;

    private String logo;


    public String getPhotosImagePath() {
        if (this.id == null || this.logo == null) {
            return null;
        }

        return "/brands-images/" + this.id + "/" + this.logo;
    }
}
