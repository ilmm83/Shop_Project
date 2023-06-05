package com.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

  private Long id;

  private Long parentId;

  private String name;

  private String alias;

  private String image;

  private boolean enabled;

  private String allParentIDs;

  public String getPhotosImagePath() {
    if (this.id == null || this.image == null)
      return null;
    return "/categories-images/" + this.id + "/" + this.image;
  }
}
