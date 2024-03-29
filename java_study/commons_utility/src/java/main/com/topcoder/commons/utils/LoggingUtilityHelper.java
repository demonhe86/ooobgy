package com.topcoder.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * This is a static helper class that provides log message generation
 * functionality for LoggingWrapperUtility and Log4jUtility.
 * 
 * Thread Safety: This class is immutable and thread safe when array parameters
 * passed to it are used by the caller in thread safe manner.
 * @author frogcherry 周晓龙 frogcherry@gmail.com
 * @version 1.0.0
 * @since 1.0.0
 */
class LoggingUtilityHelper {
    /**
     * Empty private constructor.
     * 
     * Implementation Notes: Do nothing.
     */
    private LoggingUtilityHelper() {
    }

    /**
     * Retrieves the method entrance log message.
     * 
     * Parameters: signature - the signature that uniquely identifies the method
     * (e.g. className#methodName)
     * 
     * Returns: the constructed method entrance message
     * 
     * Implementation Notes: Return "Entering method [" + signature + "].";
     * 
     * @param signature
     *            the signature that uniquely identifies the method (e.g.
     *            className#methodName)
     * @return the constructed method entrance message
     */
    public static String getMethodEntranceMessage(String signature) {
        return "Entering method [" + signature + "].";
    }

    /**
     * Retrieves the log message for the given input parameters. It's assumed
     * that paramNames and paramValues contain the same number of elements.
     * 
     * Parameters: paramNames - the names of input parameters (not null)
     * paramValues - the values of input parameters (not null)
     * 
     * Returns: the constructed log message
     * 
     * Implementation Notes: 1. sb = new StringBuilder("Input parameters ["); 2.
     * For i = 0 .. paramNames.length - 1 do: 2.1. If i != 0 then 2.1.1.
     * sb.append(" ,"); 2.2.
     * sb.append(paramNames[i]).append(":").append(paramValues[i]); 3.
     * sb.append("]"); 4. Return sb.toString().
     * 
     * @param paramValues
     *            the values of input parameters (not null)
     * @param paramNames
     *            the names of input parameters (not null)
     * @return the constructed log message
     * @throws IndexOutOfBoundsException
     *            可能抛出数组越界异常，当paramNames的长度大于paramValues的长度时
     */
    public static String getInputParametersMessage(String[] paramNames,
            Object[] paramValues) {
        StringBuilder sb = new StringBuilder("Input parameters [");
        for (int i = 0; i < paramNames.length; i++) {
            if (i!=0) {
                sb.append(" ,");
            }
            sb.append(paramNames[i]).append(":");
            sb.append(paramValues[i]);
        }
        sb.append("]");
        
        return sb.toString();
    }

    /**
     * Retrieves the method exit log message.
     * 
     * Parameters: signature - the signature that uniquely identifies the method
     * (e.g. className#methodName) entranceTimestamp - the method entrance
     * timestamp (null if not available), is used for calculating method
     * execution time
     * 
     * Returns: the constructed method exit message
     * 
     * Implementation Notes: 1. sb = new StringBuilder("Exiting method ["); 2.
     * sb.append(signature).append("]"); 3. If entranceTimestamp != null then
     * 3.1. sb.append(", time spent in the method: ").append(new
     * Date().getTime() - entranceTimestamp.getTime()).append(" milliseconds");
     * 4. sb.append('.'); 5. Return sb.toString().
     * 
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used
     *            for calculating method execution time
     * @param signature
     *            the signature that uniquely identifies the method (e.g.
     *            className#methodName)
     * @return the constructed method exit message
     */
    public static String getMethodExitMessage(String signature,
            Date entranceTimestamp) {
        StringBuilder sb = new StringBuilder("Exiting method [");
        sb.append(signature).append("]");
        if (entranceTimestamp !=null) {
            sb.append(", time spent in the method: ");
            sb.append((new Date()).getTime() - entranceTimestamp.getTime());
            sb.append(" milliseconds");
        }
        sb.append(".");
        
        return sb.toString();
    }

    /**
     * Retrieves the log message for the given method output value.
     * 
     * Parameters: value - the value returned by the method
     * 
     * Returns: the constructed log message
     * 
     * Implementation Notes: 1. Return "Output parameter: " + value.
     * 
     * @param value
     *            the value returned by the method
     * @return the constructed log message
     */
    public static String getOutputValueMessage(Object value) {
        return "Output parameter: " + value;
    }

    /**
     * Retrieves the exception log message.
     * 
     * Parameters: signature - the signature that uniquely identifies the method
     * (e.g. className#methodName) exception - the exception to be logged
     * (assumed to be not null)
     * 
     * Returns: the retrieved exception message
     * 
     * Implementation Notes: 1. sb = new StringBuilder("Error in method ["); 2.
     * sb
     * .append(signature).append("], details: ").append(exception.getMessage());
     * 3. ByteArrayOutputStream out = new ByteArrayOutputStream(); 4.
     * PrintStream ps = new PrintStream(out); 5. ps.println(); 6.
     * exception.printStackTrace(ps); 7. String stackTrace = out.toString(); 8.
     * sb.append(stackTrace); 9. Return sb.toString().
     * 
     * @param exception
     *            the exception to be logged (assumed to be not null)
     * @param signature
     *            the signature that uniquely identifies the method (e.g.
     *            className#methodName)
     * @return the retrieved exception message
     */
    public static String getExceptionMessage(String signature,
            Throwable exception) {
        StringBuilder sb = new StringBuilder("Error in method [");
        sb.append(signature).append("], details: ").append(exception.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        ps.println();
        exception.printStackTrace(ps);
        sb.append(out.toString());
        
        return sb.toString();
    }
}
