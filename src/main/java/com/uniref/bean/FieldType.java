package com.uniref.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pattern {

  private Long id;
  private String name;
  private String pattern;

  public Pattern() {

  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
}
