/*
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package io.hops.hopsworks.common.dao.jobhistory;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "yarn_app_heuristic_result_details",
        catalog = "hopsworks",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "YarnAppHeuristicResultDetails.findAll",
          query = "SELECT y FROM YarnAppHeuristicResultDetails y"),
  @NamedQuery(name
          = "YarnAppHeuristicResultDetails.findByYarnAppHeuristicResultId",
          query = "SELECT y FROM YarnAppHeuristicResultDetails y "
          + "WHERE y.yarnAppHeuristicResultDetailsPK.yarnAppHeuristicResultId "
          + "= :yarnAppHeuristicResultId"),
  @NamedQuery(name = "YarnAppHeuristicResultDetails.findByIdAndName",
          query = "SELECT y FROM YarnAppHeuristicResultDetails y "
          + "WHERE y.yarnAppHeuristicResultDetailsPK.yarnAppHeuristicResultId "
          + "= :yarnAppHeuristicResultId AND y.yarnAppHeuristicResultDetailsPK.name = :name"),
  @NamedQuery(name = "YarnAppHeuristicResultDetails.findByName",
          query = "SELECT y FROM YarnAppHeuristicResultDetails y "
          + "WHERE y.yarnAppHeuristicResultDetailsPK.name = :name")})
public class YarnAppHeuristicResultDetails implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected YarnAppHeuristicResultDetailsPK yarnAppHeuristicResultDetailsPK;
  @Lob
  @Size(max = 65535)
  @Column(name = "value")
  private String value;
  @Lob
  @Size(max = 65535)
  @Column(name = "details")
  private String details;
  @JoinColumn(name = "yarn_app_heuristic_result_id",
          referencedColumnName = "id",
          insertable = false,
          updatable = false)
  @ManyToOne(optional = false)
  private YarnAppHeuristicResult yarnAppHeuristicResult;

  public YarnAppHeuristicResultDetails() {
  }

  public YarnAppHeuristicResultDetails(
          YarnAppHeuristicResultDetailsPK yarnAppHeuristicResultDetailsPK) {
    this.yarnAppHeuristicResultDetailsPK = yarnAppHeuristicResultDetailsPK;
  }

  public YarnAppHeuristicResultDetails(int yarnAppHeuristicResultId, String name) {
    this.yarnAppHeuristicResultDetailsPK = new YarnAppHeuristicResultDetailsPK(
            yarnAppHeuristicResultId, name);
  }

  public YarnAppHeuristicResultDetailsPK getYarnAppHeuristicResultDetailsPK() {
    return yarnAppHeuristicResultDetailsPK;
  }

  public void setYarnAppHeuristicResultDetailsPK(
          YarnAppHeuristicResultDetailsPK yarnAppHeuristicResultDetailsPK) {
    this.yarnAppHeuristicResultDetailsPK = yarnAppHeuristicResultDetailsPK;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public YarnAppHeuristicResult getYarnAppHeuristicResult() {
    return yarnAppHeuristicResult;
  }

  public void setYarnAppHeuristicResult(
          YarnAppHeuristicResult yarnAppHeuristicResult) {
    this.yarnAppHeuristicResult = yarnAppHeuristicResult;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (yarnAppHeuristicResultDetailsPK != null
            ? yarnAppHeuristicResultDetailsPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof YarnAppHeuristicResultDetails)) {
      return false;
    }
    YarnAppHeuristicResultDetails other = (YarnAppHeuristicResultDetails) object;
    if ((this.yarnAppHeuristicResultDetailsPK == null
            && other.yarnAppHeuristicResultDetailsPK != null)
            || (this.yarnAppHeuristicResultDetailsPK != null
            && !this.yarnAppHeuristicResultDetailsPK.equals(
                    other.yarnAppHeuristicResultDetailsPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "se.kth.bbc.jobs.jobhistory.YarnAppHeuristicResultDetails[ yarnAppHeuristicResultDetailsPK="
            + yarnAppHeuristicResultDetailsPK + " ]";
  }

}
