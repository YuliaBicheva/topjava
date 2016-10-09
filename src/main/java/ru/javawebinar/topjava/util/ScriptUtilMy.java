//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

public abstract class ScriptUtilMy {
    public static final String DEFAULT_STATEMENT_SEPARATOR = ";";
    public static final String FALLBACK_STATEMENT_SEPARATOR = "\n";
    public static final String EOF_STATEMENT_SEPARATOR = "^^^ END OF SCRIPT ^^^";
    public static final String DEFAULT_COMMENT_PREFIX = "--";
    public static final String DEFAULT_BLOCK_COMMENT_START_DELIMITER = "/*";
    public static final String DEFAULT_BLOCK_COMMENT_END_DELIMITER = "*/";
    private static final Logger logger = LoggerFactory.getLogger(ScriptUtilMy.class);
//    private static final Log logger = LogFactory.getLog(ScriptUtils.class);

    public ScriptUtilMy() {
    }

    public static void splitSqlScript(String script, char separator, List<String> statements) throws ScriptException {
        splitSqlScript(script, String.valueOf(separator), statements);
    }

    public static void splitSqlScript(String script, String separator, List<String> statements) throws ScriptException {
        splitSqlScript((EncodedResource)null, script, separator, "--", "/*", "*/", statements);
    }

    public static void splitSqlScript(EncodedResource resource, String script, String separator, String commentPrefix, String blockCommentStartDelimiter, String blockCommentEndDelimiter, List<String> statements) throws ScriptException {
        Assert.hasText(script, "script must not be null or empty");
        Assert.notNull(separator, "separator must not be null");
        Assert.hasText(commentPrefix, "commentPrefix must not be null or empty");
        Assert.hasText(blockCommentStartDelimiter, "blockCommentStartDelimiter must not be null or empty");
        Assert.hasText(blockCommentEndDelimiter, "blockCommentEndDelimiter must not be null or empty");
        StringBuilder sb = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inEscape = false;
        char[] content = script.toCharArray();

        for(int i = 0; i < script.length(); ++i) {
            char c = content[i];
            if(inEscape) {
                inEscape = false;
                sb.append(c);
            } else if(c == 92) {
                inEscape = true;
                sb.append(c);
            } else {
                if(!inDoubleQuote && c == 39) {
                    inSingleQuote = !inSingleQuote;
                } else if(!inSingleQuote && c == 34) {
                    inDoubleQuote = !inDoubleQuote;
                }

                if(!inSingleQuote && !inDoubleQuote) {
                    if(script.startsWith(separator, i)) {
                        if(sb.length() > 0) {
                            statements.add(sb.toString());
                            sb = new StringBuilder();
                        }

                        i += separator.length() - 1;
                        continue;
                    }

                    int indexOfCommentEnd;
                    if(script.startsWith(commentPrefix, i)) {
                        indexOfCommentEnd = script.indexOf("\n", i);
                        if(indexOfCommentEnd <= i) {
                            break;
                        }

                        i = indexOfCommentEnd;
                        continue;
                    }

                    if(script.startsWith(blockCommentStartDelimiter, i)) {
                        indexOfCommentEnd = script.indexOf(blockCommentEndDelimiter, i);
                        if(indexOfCommentEnd <= i) {
                            throw new ScriptParseException(String.format("Missing block comment end delimiter [%s].", new Object[]{blockCommentEndDelimiter}), resource);
                        }

                        i = indexOfCommentEnd + blockCommentEndDelimiter.length() - 1;
                        continue;
                    }

                    if(c == 32 || c == 10 || c == 9) {
                        if(sb.length() <= 0 || sb.charAt(sb.length() - 1) == 32) {
                            continue;
                        }

                        c = 32;
                    }
                }

                sb.append(c);
            }
        }

        if(StringUtils.hasText(sb)) {
            statements.add(sb.toString());
        }

    }

    static String readScript(EncodedResource resource) throws IOException {
        return readScript(resource, "--", ";");
    }

    private static String readScript(EncodedResource resource, String commentPrefix, String separator) throws IOException {
        LineNumberReader lnr = new LineNumberReader(resource.getReader());

        String var4;
        try {
            var4 = readScript(lnr, commentPrefix, separator);
        } finally {
            lnr.close();
        }

        return var4;
    }

    public static String readScript(LineNumberReader lineNumberReader, String commentPrefix, String separator) throws IOException {
        String currentStatement = lineNumberReader.readLine();

        StringBuilder scriptBuilder;
        for(scriptBuilder = new StringBuilder(); currentStatement != null; currentStatement = lineNumberReader.readLine()) {
            if(commentPrefix != null && !currentStatement.startsWith(commentPrefix)) {
                if(scriptBuilder.length() > 0) {
                    scriptBuilder.append('\n');
                }

                scriptBuilder.append(currentStatement);
            }
        }

        appendSeparatorToScriptIfNecessary(scriptBuilder, separator);
        return scriptBuilder.toString();
    }

    private static void appendSeparatorToScriptIfNecessary(StringBuilder scriptBuilder, String separator) {
        if(separator != null) {
            String trimmed = separator.trim();
            if(trimmed.length() != separator.length()) {
                if(scriptBuilder.lastIndexOf(trimmed) == scriptBuilder.length() - trimmed.length()) {
                    scriptBuilder.append(separator.substring(trimmed.length()));
                }

            }
        }
    }

    public static boolean containsSqlScriptDelimiters(String script, String delim) {
        boolean inLiteral = false;
        char[] content = script.toCharArray();

        for(int i = 0; i < script.length(); ++i) {
            if(content[i] == 39) {
                inLiteral = !inLiteral;
            }

            if(!inLiteral && script.startsWith(delim, i)) {
                return true;
            }
        }

        return false;
    }

    public static void executeSqlScript(Connection connection, Resource resource) throws ScriptException {
        executeSqlScript(connection, new EncodedResource(resource));
    }

    public static void executeSqlScript(Connection connection, EncodedResource resource) throws ScriptException {
        executeSqlScript(connection, resource, false, false, "--", ";", "/*", "*/");
    }

    public static void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError, boolean ignoreFailedDrops, String commentPrefix, String separator, String blockCommentStartDelimiter, String blockCommentEndDelimiter) throws ScriptException {
        try {
            if(logger.isInfoEnabled()) {
                logger.info("Executing SQL script from " + resource);
            }

            long ex = System.currentTimeMillis();

            String script;
            try {
                script = readScript(resource, commentPrefix, separator);
            } catch (IOException var27) {
                throw new CannotReadScriptException(resource, var27);
            }

            if(separator == null) {
                separator = ";";
            }

            if(!"^^^ END OF SCRIPT ^^^".equals(separator) && !containsSqlScriptDelimiters(script, separator)) {
                separator = "\n";
            }

            LinkedList statements = new LinkedList();
            splitSqlScript(resource, script, separator, commentPrefix, blockCommentStartDelimiter, blockCommentEndDelimiter, statements);
            int stmtNumber = 0;
            Statement stmt = connection.createStatement();

            try {
                Iterator elapsedTime = statements.iterator();

                while(elapsedTime.hasNext()) {
                    String statement = (String)elapsedTime.next();
                    ++stmtNumber;

                    try {
                        stmt.execute(statement.replace(";",""));
                        int ex1 = stmt.getUpdateCount();
                        if(logger.isDebugEnabled()) {
                            logger.debug(ex1 + " returned as update count for SQL: " + statement);

                            for(SQLWarning var32 = stmt.getWarnings(); var32 != null; var32 = var32.getNextWarning()) {
                                logger.debug("SQLWarning ignored: SQL state \'" + var32.getSQLState() + "\', error code \'" + var32.getErrorCode() + "\', message [" + var32.getMessage() + "]");
                            }
                        }
                    } catch (SQLException var28) {
                        boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
                        if(!continueOnError && (!dropStatement || !ignoreFailedDrops)) {
                            throw new ScriptStatementFailedException(statement, stmtNumber, resource, var28);
                        }

                        if(logger.isDebugEnabled()) {
                            logger.debug(ScriptStatementFailedException.buildErrorMessage(statement, stmtNumber, resource), var28);
                        }
                    }
                }
            } finally {
                try {
                    stmt.close();
                } catch (Throwable var26) {
                    logger.debug("Could not close JDBC Statement", var26);
                }

            }

            long var31 = System.currentTimeMillis() - ex;
            if(logger.isInfoEnabled()) {
                logger.info("Executed SQL script from " + resource + " in " + var31 + " ms.");
            }

        } catch (Exception var30) {
            if(var30 instanceof ScriptException) {
                throw (ScriptException)var30;
            } else {
                throw new UncategorizedScriptException("Failed to execute database script from resource [" + resource + "]", var30);
            }
        }
    }
}
