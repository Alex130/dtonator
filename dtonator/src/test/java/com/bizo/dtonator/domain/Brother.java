package com.bizo.dtonator.domain;

import java.util.Set;

public class Brother implements IBrother {

  public Long id;

  public Set<Sibling> siblings;

  /* (non-Javadoc)
   * @see com.bizo.dtonator.domain.IBrother#getGender()
   */
  @Override
  public String getGender() {
    return "male";
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.domain.IBrother#getSiblingIds()
   */
  @Override
  public Set<Long> getSiblingIds() {
    return null;
  }

  public Set<Sibling> getSiblings() {
    return siblings;
  }

  public void setSiblings(Set<Sibling> siblings) {
    this.siblings = siblings;
  }
}
