package org.chomookun.arch4j.core.common.data.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.chomookun.arch4j.core.common.crpyto.CryptoUtil;
import org.springframework.stereotype.Component;

//@Component
//@Intercepts({@Signature(
//        type = Executor.class,
//        method = "update",
//        args = {MappedStatement.class, Object.class}
//)})
@Slf4j
@Deprecated
public class CryptoEncryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if(args.length >= 2) {
           Object parameter = args[1];
           CryptoUtil.getInstance().encryptObject(parameter);
        }
        Object returnObject = invocation.proceed();
        log.debug("returnObject:{}", returnObject);
        return returnObject;
    }

}
