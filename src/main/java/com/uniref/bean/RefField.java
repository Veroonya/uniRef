package com.uniref.bean;


import javax.persistence.*;

@Entity
public class RefField {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private RefType refType;

  private String name;
  private Integer ordr;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false)
  private FieldType fieldType;

  public RefField() {

  }


  public void setId(Long id) {
    this.id = id;
  }

//  @Id
  public Long getId() {
    return id;
  }

  public RefType getRefType() {
    return refType;
  }

  public void setRefType(RefType refType) {
    this.refType = refType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getOrdr() {
    return ordr;
  }

  public void setOrdr(Integer order) {
    this.ordr = order;
  }


  public FieldType getFieldType() {
    return fieldType;
  }

  public void setFieldType(FieldType fieldType) {
    this.fieldType = fieldType;
  }

}
