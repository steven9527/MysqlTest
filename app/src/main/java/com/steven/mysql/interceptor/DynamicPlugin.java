package com.steven.mysql.interceptor;

import com.steven.mysql.datasource.DynamicDataSourceGlobal;
import com.steven.mysql.datasource.DynamicDataSourceHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
//import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steven.zhao on 2019/3/14.
 */

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }) })
public class DynamicPlugin implements Interceptor {

    protected static final Logger logger = LoggerFactory.getLogger(DynamicPlugin.class);

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    private static final Map<String,DynamicDataSourceGlobal> cacheMap=new ConcurrentHashMap<String, DynamicDataSourceGlobal>() ;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];
            DynamicDataSourceGlobal dynamicDataSourceGlobal = cacheMap.get(ms.getId());

            if (dynamicDataSourceGlobal  == null) {

                //读方法
                if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {

                    ////!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                    }
                    else
                    {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql();

                        //如果sql语句里有insert,delete,update，用写库
                        if (sql.matches(REGEX)) {
                            dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                        }
                        else {
                            dynamicDataSourceGlobal = DynamicDataSourceGlobal.READ;
                        }
                    }
                }
                else {
                    dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                }

                logger.warn("设置方法[{}] use [{}] strategy,SqlCommandType:[{}].. ", ms.getId(), dynamicDataSourceGlobal
                        .name(), ms.getSqlCommandType());

                cacheMap.put(ms.getId(), dynamicDataSourceGlobal);
            }

            DynamicDataSourceHolder.putDataSource(dynamicDataSourceGlobal);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        else {
            return target;
        }

    }

    @Override
    public void setProperties(Properties properties) {

    }
}
