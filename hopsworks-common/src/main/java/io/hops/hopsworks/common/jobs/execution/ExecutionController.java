package io.hops.hopsworks.common.jobs.execution;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import io.hops.hopsworks.common.dao.user.activity.ActivityFacade;
import io.hops.hopsworks.common.dao.hdfs.inode.Inode;
import io.hops.hopsworks.common.dao.hdfs.inode.InodeFacade;
import io.hops.hopsworks.common.dao.jobhistory.Execution;
import io.hops.hopsworks.common.dao.jobhistory.YarnApplicationstateFacade;
import io.hops.hopsworks.common.dao.jobs.JobsHistoryFacade;
import io.hops.hopsworks.common.dao.jobs.description.JobDescription;
import io.hops.hopsworks.common.dao.user.Users;
import io.hops.hopsworks.common.hdfs.DistributedFileSystemOps;
import io.hops.hopsworks.common.hdfs.DistributedFsService;
import io.hops.hopsworks.common.hdfs.HdfsUsersController;
import io.hops.hopsworks.common.jobs.adam.AdamController;
import io.hops.hopsworks.common.jobs.flink.FlinkController;
import io.hops.hopsworks.common.jobs.spark.SparkController;
import io.hops.hopsworks.common.jobs.spark.SparkJobConfiguration;
import io.hops.hopsworks.common.util.Settings;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Takes care of booting the execution of a job.
 */
@Stateless
public class ExecutionController {

  //Controllers
  @EJB
  private SparkController sparkController;
  @EJB
  private AdamController adamController;
  @EJB
  private FlinkController flinkController;
  @EJB
  private InodeFacade inodes;
  @EJB
  private ActivityFacade activityFacade;
  @EJB
  private JobsHistoryFacade jobHistoryFac;
  @EJB
  private YarnApplicationstateFacade yarnApplicationstateFacade;
  @EJB
  private HdfsUsersController hdfsUsersBean;
  @EJB
  private DistributedFsService dfs;
  @EJB
  private Settings settings;

  final Logger logger = Logger.getLogger(ExecutionController.class.getName());

  public Execution start(JobDescription job, Users user) throws IOException {
    Execution exec = null;

    switch (job.getJobType()) {
      case ADAM:
        exec = adamController.startJob(job, user);
//        if (exec == null) {
//          throw new IllegalArgumentException("Problem getting execution object for: " + job.
//              getJobType());
//        }
//        int execId = exec.getId();
//        AdamJobConfiguration adamConfig = (AdamJobConfiguration) job.getJobConfig();
//        String path = adamConfig.getAppPath();
//        String[] parts = path.split("/");
//        String pathOfInode = path.replace("hdfs://" + parts[2], "");
//        
//        Inode inode = inodes.getInodeAtPath(pathOfInode);
//        String inodeName = inode.getInodePK().getName();
//        
//        jobHistoryFac.persist(user, job, execId, exec.getAppId());
//        activityFacade.persistActivity(activityFacade.EXECUTED_JOB + inodeName, job.getProject(), user);
        break;
      case FLINK:
        return flinkController.startJob(job, user);
      case SPARK:
        exec = sparkController.startJob(job, user);
        if (exec == null) {
          throw new IllegalArgumentException(
                  "Problem getting execution object for: " + job.
                  getJobType());
        }
        int execId = exec.getId();
        SparkJobConfiguration config = (SparkJobConfiguration) job.
                getJobConfig();

        String path = config.getAppPath();
        String patternString = "hdfs://(.*)\\s";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(path);
        String[] parts = path.split("/");
        String pathOfInode = path.replace("hdfs://" + parts[2], "");

        Inode inode = inodes.getInodeAtPath(pathOfInode);
        String inodeName = inode.getInodePK().getName();

        jobHistoryFac.persist(user, job, execId, exec.getAppId());
        activityFacade.persistActivity(activityFacade.EXECUTED_JOB + inodeName,
                job.getProject(), user);
        break;
      case PYSPARK:
      case TFSPARK:
        exec = sparkController.startJob(job, user);
        if (exec == null) {
          throw new IllegalArgumentException("Problem getting execution object for: " + job.getJobType());
        }
        break;
      default:
        throw new IllegalArgumentException(
                "Unsupported job type: " + job.
                getJobType());
    }

    return exec;
  }

  public void kill(JobDescription job, Users user) throws IOException {

    String appid = yarnApplicationstateFacade.findByAppname(job.getName())
        .get(0)
        .getApplicationid();

    //Look for unique marker file which means it is a streaming job. Otherwise proceed with normal kill.
    DistributedFileSystemOps udfso = null;
    String username = hdfsUsersBean.getHdfsUserName(job.getProject(), user);
    try {
      udfso = dfs.getDfsOps(username);
      String marker = Settings.getJobMarkerFile(job, appid);
      if (udfso.exists(marker)) {
        udfso.rm(new org.apache.hadoop.fs.Path(marker), false);

      }
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Could not remove marker file for job:" + job.getName() + "with appId:" + appid, ex);
    } finally {
      if (udfso != null) {
        udfso.close();
      }
    }

    //WORKS FOR NOW BUT SHOULD EVENTUALLY GO THROUGH THE YARN CLIENT API
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec(settings.getHadoopDir() + "/bin/yarn application -kill " + appid);

  }
  
  public void stop(JobDescription job, Users user, String appid) throws
          IOException {
    switch (job.getJobType()) {
      case ADAM:
        adamController.stopJob(job, user, appid);
        break;
      case SPARK:
        sparkController.stopJob(job, user, appid);
        break;
      case FLINK:
        flinkController.stopJob(job, user, appid);
        break;
      default:
        throw new IllegalArgumentException("Unsupported job type: " + job.
                getJobType());

    }
  }
}