package de.invesdwin.context.r.runtime.rserve.pool.internal;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Named;

import org.math.R.Rsession;
import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.pool.IPoolableObjectFactory;
import de.invesdwin.context.r.runtime.contract.IScriptTaskRunnerR;
import de.invesdwin.context.r.runtime.rserve.RserveProperties;
import de.invesdwin.context.r.runtime.rserve.RserveScriptTaskEngineR;
import de.invesdwin.context.r.runtime.rserve.RserverConfMode;
import de.invesdwin.util.error.UnknownArgumentException;

@ThreadSafe
@Named
public final class RsessionPoolableObjectFactory
        implements IPoolableObjectFactory<Rsession>, FactoryBean<RsessionPoolableObjectFactory> {

    public static final RsessionPoolableObjectFactory INSTANCE = new RsessionPoolableObjectFactory();

    private RsessionPoolableObjectFactory() {}

    @Override
    public Rsession makeObject() {
        switch (RserveProperties.RSERVER_CONF_MODE) {
        case LOCAL_SPAWN:
            return Rsession.newInstanceTry(new RsessionLogger(), RserveProperties.RSERVER_CONF);
        case LOCAL:
            //fallthrough
        case REMOTE:
            return Rsession.newRemoteInstance(new RsessionLogger(), RserveProperties.RSERVER_CONF);
        default:
            throw UnknownArgumentException.newInstance(RserverConfMode.class, RserveProperties.RSERVER_CONF_MODE);
        }
    }

    @Override
    public void destroyObject(final Rsession obj) throws Exception {
        obj.end();
    }

    @Override
    public boolean validateObject(final Rsession obj) {
        return true;
    }

    @Override
    public void activateObject(final Rsession obj) throws Exception {}

    @Override
    public void passivateObject(final Rsession obj) throws Exception {
        final RserveScriptTaskEngineR engine = new RserveScriptTaskEngineR(obj);
        engine.eval(IScriptTaskRunnerR.CLEANUP_SCRIPT);
        engine.close();
        obj.close(); //reset logger
    }

    @Override
    public RsessionPoolableObjectFactory getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return RsessionPoolableObjectFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
