package de.invesdwin.context.r.runtime.cli.pool.internal;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import com.github.rcaller.rstuff.RCaller;

import de.invesdwin.context.pool.IPoolableObjectFactory;
import de.invesdwin.context.r.runtime.cli.CliScriptTaskRunner;

@ThreadSafe
@Named
public final class RCallerPoolableObjectFactory
        implements IPoolableObjectFactory<RCaller>, FactoryBean<RCallerPoolableObjectFactory> {

    public static final RCallerPoolableObjectFactory INSTANCE = new RCallerPoolableObjectFactory();

    private RCallerPoolableObjectFactory() {}

    @Override
    public RCaller makeObject() {
        return new ModifiedRCaller();
    }

    @Override
    public void destroyObject(final RCaller obj) throws Exception {
        obj.StopRCallerOnline();
    }

    @Override
    public boolean validateObject(final RCaller obj) {
        return true;
    }

    @Override
    public void activateObject(final RCaller obj) throws Exception {}

    @Override
    public void passivateObject(final RCaller obj) throws Exception {
        obj.getRCode().clear();
        obj.getRCode().getCode().insert(0, "rm(list = ls())\n");
        obj.getRCode().addRCode(CliScriptTaskRunner.INTERNAL_RESULT_VARIABLE + " <- c()");
        obj.runAndReturnResultOnline(CliScriptTaskRunner.INTERNAL_RESULT_VARIABLE);
        obj.deleteTempFiles();
    }

    @Override
    public RCallerPoolableObjectFactory getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return RCallerPoolableObjectFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
