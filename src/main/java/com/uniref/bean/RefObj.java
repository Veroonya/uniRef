package com.uniref.bean;

import javax.persistence.*;
import java.util.List;

@Entity
public class RefObj {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private RefType refType;

  @OneToMany
  private List<RefValue> refValues;

  public RefObj() {
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

  public List<RefValue> getRefValues() {
    return refValues;
  }

  public void setRefValues(List<RefValue> refValues) {
    this.refValues = refValues;
  }
}
