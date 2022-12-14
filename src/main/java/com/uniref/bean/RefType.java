package com.uniref.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RefType {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;
  private String name;

  public RefType() {
    
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

  public RefType(String name) {
    this.name = name;
  }
}
