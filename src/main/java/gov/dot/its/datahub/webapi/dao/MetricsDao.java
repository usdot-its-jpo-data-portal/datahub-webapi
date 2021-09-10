package gov.dot.its.datahub.webapi.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import gov.dot.its.datahub.webapi.model.DHMetrics;

public interface MetricsDao {
    
    List<DHMetrics> getMetrics(int limit) throws IOException;

    List<DHMetrics> getMetrics(int esDefaultLimit, String dhSourceName) throws IOException;

    List<DHMetrics> getMetrics(int esDefaultLimit, String beginDate, String endDate) throws IOException, ParseException;

}
