package io.hops.kmon.alert;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.math.BigInteger;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Hamidreza Afzali <afzali@kth.se>
 */
@Entity
@Table(name = "hopsworks.alerts")
@NamedQueries({
  @NamedQuery(name = "Alerts.findAll",
          query
          = "SELECT a FROM Alert a WHERE a.alertTime >= :fromdate AND "
                  + "a.alertTime <= :todate ORDER BY a.alertTime DESC"),
  @NamedQuery(name = "Alerts.findBy-Severity",
          query
          = "SELECT a FROM Alert a WHERE a.alertTime >= :fromdate AND "
                  + "a.alertTime <= :todate AND "
                  + "a.severity = :severity ORDER BY a.alertTime DESC"),
  @NamedQuery(name = "Alerts.findBy-Provider",
          query
          = "SELECT a FROM Alert a WHERE a.alertTime >= :fromdate AND "
                  + "a.alertTime <= :todate AND "
                  + "a.provider = :provider ORDER BY a.alertTime DESC"),
  @NamedQuery(name = "Alerts.findBy-Provider-Severity",
          query
          = "SELECT a FROM Alert a WHERE a.alertTime >= :fromdate AND "
                  + "a.alertTime <= :todate AND a.severity = :severity AND "
                  + "a.provider = :provider ORDER BY a.alertTime DESC"),

  @NamedQuery(name = "Alerts.removeAll",
          query = "DELETE FROM Alert a")
})
public class Alert implements Serializable {

  private static final long serialVersionUID = 1L;
  
  public enum Severity {
    FAILURE,
    WARNING,
    OK
  }

  public enum Provider {
    Collectd,
    Agent
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Long id;
  @Size(max = 32)
  @Column(name = "current_value")
  private String currentValue;
  @Size(max = 32)
  @Column(name = "failure_max")
  private String failureMax;
  @Size(max = 32)
  @Column(name = "failure_min")
  private String failureMin;
  @Size(max = 32)
  @Column(name = "warning_max")
  private String warningMax;
  @Size(max = 32)
  @Column(name = "warning_min")
  private String warningMin;
  @Column(name = "agent_time")
  private BigInteger agentTime;
  @Column(name = "alert_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date alertTime;
  @Size(max = 128)
  @Column(name = "data_source")
  private String dataSource;
  @Size(max = 256)
  @Column(name = "hostid")
  private String hostid;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 1024)
  @Column(name = "message")
  private String message;
  @Size(max = 128)
  @Column(name = "plugin")
  private String plugin;
  @Size(max = 128)
  @Column(name = "plugin_instance")
  private String pluginInstance;
  @Column(name = "provider")
  private String provider;
  @Column(name = "severity")
  private String severity;
  @Size(max = 128)
  @Column(name = "type")
  private String type;
  @Size(max = 128)
  @Column(name = "type_instance")
  private String typeInstance;

  public Alert() {
  }

  public Alert(String hostId, String message, String plugin,
          String pluginInstance, String type, String typeInstance) {
    this.hostid = hostId;
    this.message = message;
    this.plugin = plugin;
    this.pluginInstance = pluginInstance;
    this.type = type;
    this.typeInstance = typeInstance;
  }

  public Alert(Long id) {
    this.id = id;
  }

  public Alert(Long id, String message) {
    this.id = id;
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(String currentValue) {
    this.currentValue = currentValue;
  }

  public String getFailureMax() {
    return failureMax;
  }

  public void setFailureMax(String failureMax) {
    this.failureMax = failureMax;
  }

  public String getFailureMin() {
    return failureMin;
  }

  public void setFailureMin(String failureMin) {
    this.failureMin = failureMin;
  }

  public String getWarningMax() {
    return warningMax;
  }

  public void setWarningMax(String warningMax) {
    this.warningMax = warningMax;
  }

  public String getWarningMin() {
    return warningMin;
  }

  public void setWarningMin(String warningMin) {
    this.warningMin = warningMin;
  }

  public BigInteger getAgentTime() {
    return agentTime;
  }

  public void setAgentTime(BigInteger agentTime) {
    this.agentTime = agentTime;
  }

  public Date getAlertTime() {
    return alertTime;
  }

  public void setAlertTime(Date alertTime) {
    this.alertTime = alertTime;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public String getHostid() {
    return hostid;
  }

  public void setHostid(String hostid) {
    this.hostid = hostid;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPlugin() {
    return plugin;
  }

  public void setPlugin(String plugin) {
    this.plugin = plugin;
  }

  public String getPluginInstance() {
    return pluginInstance;
  }

  public void setPluginInstance(String pluginInstance) {
    this.pluginInstance = pluginInstance;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTypeInstance() {
    return typeInstance;
  }

  public void setTypeInstance(String typeInstance) {
    this.typeInstance = typeInstance;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Alert)) {
      return false;
    }
    Alert other = (Alert) object;
    if ((this.id == null && other.id != null) ||
            (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.kmon.cluster.Alerts[ id=" + id + " ]";
  }

}