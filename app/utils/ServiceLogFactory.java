package utils;

import models.ServiceExecutionLog;

/**
 * Created by Weiyi Wang on 4/21/2016.
 * andrew id: weiyiw
 * mail: weiyiw@andrew.cmu.edu
 */
public class ServiceLogFactory {
    ServiceExecutionLog prototypeSLog;

    public ServiceLogFactory() {
        prototypeSLog = new ServiceExecutionLog();
    }

    public ServiceExecutionLog makeServiceLog() {
        return (ServiceExecutionLog)prototypeSLog.clone();
    }
}
