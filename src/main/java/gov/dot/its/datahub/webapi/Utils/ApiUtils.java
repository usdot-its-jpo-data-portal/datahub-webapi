package gov.dot.its.datahub.webapi.Utils;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import gov.dot.its.datahub.webapi.model.ApiError;

@Component
public class ApiUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);

	private boolean debug;
	private static final String MESSAGE_TEMPLATE = "%s : %s ";
	private static final String ERROR_LABEL = "Error";
	
	public List<ApiError> getErrorsFromException(List<ApiError> errors, Exception e) {
		String msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, e.getMessage());
		errors.add(new ApiError(msg));
		logger.error(msg);
		if (debug) {
			msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, e.toString());
			logger.error(msg);
		}
		if (e.getSuppressed().length > 0) {
			for (Throwable x : e.getSuppressed()) {
				msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, x.toString());
				errors.add(new ApiError(msg));
				logger.error(msg);
			}
		}
		return errors;
	}
    
    public String stringFormat(String template, Object... values) {
		int n = StringUtils.countOccurrencesOf(template, "%s");
		if (n == values.length) {
			return String.format(template, values);
		}
		Object[] result = new Object[n];
		Arrays.fill(result, "");
		if (n > values.length) {
			System.arraycopy(values, 0, result, 0, values.length);
			return String.format(template, result);
		}

		System.arraycopy(values, 0, result, 0, result.length);
		StringBuilder s = new StringBuilder();
		for(int i=n; i<values.length; i++) {
			s.append(values[i] == null ? "": values[i] + " ");
		}
		result[result.length-1] = (result[result.length-1] == null ? "" : result[result.length-1])+ " " +s.toString();
		return String.format(template, result);
	}
}
