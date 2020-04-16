package coderead.test.dubbo;

import com.cbtu.agent.StackSessionBuild;
import com.cbtu.agent.jacoco.StackSession;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author tommy
 * @title: FilterTest
 * @projectName dubbo-test
 * @description: 过滤器用于监听远程服务，从而采集服务处理过程的用例数据
 * @date 2020/4/1010:10 AM
 */
@Activate(group = CommonConstants.PROVIDER, order = Integer.MAX_VALUE)
public class MonitorFilter implements Filter {
    //拦截服务处理过程
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        StackSession session = StackSessionBuild.open(Invoker.class,Invocation.class);
        try {
            //==============测试方法开始=================/
            return invoker.invoke(invocation);
            //==============测试方法结束=================/
        } finally {
//            session.saveCase("服务请求处理");
        }
    }
}
