package com.uniref.bean;

import javax.persistence.*;

@Entity
public class RefValue {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private RefObj refObj;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private RefField refField;

  private String value;

  public RefValue() {
  }

  public void setId(Long id) {
    this.id = id;
  }

//  @Id
  public Long getId() {
    return id;
  }

  public RefObj getRefObj() {
    return refObj;
  }

  public void setRefObj(RefObj refObj) {
    this.refObj = refObj;
  }

  public RefField getRefField() {
    return refField;
  }

  public void setRefField(RefField refField) {
    this.refField = refField;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
