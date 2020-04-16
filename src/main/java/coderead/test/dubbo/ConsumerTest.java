package coderead.test.dubbo;


import coderead.test.dubbo.service.UserService;
import com.cbtu.agent.StackSessionBuild;
import com.cbtu.agent.jacoco.StackSession;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.junit.Test;

/**
 * @author tommy
 * @title: ConsumerTest
 * @projectName dubbo-test
 * @description: 消费者服务测试
 * @date 2020/4/109:57 AM
 */
public class ConsumerTest {

    // 远程服务引用
    @Test
    public void referenceTest() {
        ReferenceConfig<UserService> referenceConfig = getReferenceConfig();
        referenceConfig.setConnections(1);
        referenceConfig.get();
        referenceConfig = getReferenceConfig();
        StackSession session = StackSessionBuild.open();
        //==============测试方法开始=================/
        referenceConfig.get();
        //==============测试方法结束=================/
        session.saveCase("远程服务引用");
    }

    // 远程服务调用
    @Test
    public void remoteInvokeTest() {
        ReferenceConfig<UserService> referenceConfig = getReferenceConfig();
        referenceConfig.setTimeout(Integer.MAX_VALUE);
        UserService userService = referenceConfig.get();
        StackSession session = StackSessionBuild.open();
        session.addSubSessionInlet("org/apache/dubbo/remoting/transport/netty4/NettyCodecAdapter$InternalEncoder","encode",null);
        session.addSubSessionInlet("io/netty/handler/codec/ByteToMessageDecoder","channelRead",null);

        //==============测试方法结束=================/
        System.out.println("调用服务：" + userService.getUser(1));
        //==============测试方法开始=================/
        session.printStack(System.out);
        session.saveCase("远程服务调用");
    }

    private ReferenceConfig<UserService> getReferenceConfig() {
        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig();
        referenceConfig.setInterface(UserService.class);
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setApplication(new ApplicationConfig("young-app"));
        referenceConfig.setLoadbalance("roundrobin");
        return referenceConfig;
    }

}
