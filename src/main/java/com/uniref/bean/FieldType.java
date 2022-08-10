package com.uniref.bean;

import javax.persistence.*;

@Entity
public class FieldType {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;
  private String name;
  private String pattern;

  public FieldType() {

  }

  public void setId(Long id) {
    this.id = id;
  }

//  @Id
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
