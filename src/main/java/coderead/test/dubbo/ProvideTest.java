package coderead.test.dubbo;

import coderead.test.dubbo.service.UserService;
import coderead.test.dubbo.service.UserServiceImpl;
import com.cbtu.agent.StackSessionBuild;
import com.cbtu.agent.jacoco.StackSession;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.junit.Test;

import java.io.IOException;

import static com.cbtu.agent.jacoco.StackSession.*;

/**
 * @author tommy
 * @title: ProvideTest
 * @projectName dubbo-test
 * @description: TODO
 * @date 2020/4/109:33 AM
 */
public class ProvideTest {

    // 服务暴露
    @Test
    public void exportTest() throws IOException {
        ServiceConfig<UserService> serviceConfig = buildServiceConfig(-1);
        serviceConfig.export(); //由于很初始操作导致堆栈 太多，先构建一个完成初始化，以降低堆栈

        serviceConfig = buildServiceConfig(20881);
        StackSession session = StackSessionBuild.open();
        //==============测试方法开始=================/
        serviceConfig.export();
        System.out.println("服务已开启 :" + serviceConfig);
        //==============测试方法结束=================/
        session.groupByMethod();
        session.saveCase("服务暴露");
        System.in.read();
    }

    //请求消息解码，默认只会解码协议头，并且生成一个解码器， 将给work线程来处理
    @Test
    public void decodeRequestTest() throws IOException {
        //添加监听自动打采集会话
        ServiceConfig<UserService> serviceConfig = buildServiceConfig(-1);
        serviceConfig.export();
        System.out.println("开启服务:"+serviceConfig);
        StackSession.addListener(new StackSessionListener() {
            public boolean isOpen(long classId, String className,String methodFullName) {
                if (!className.equals("io/netty/handler/codec/bytetomessagedecoder")) {
                    return false;
                } else if (!methodFullName.startsWith("channelRead ")) {
                    return false;
                }
                return true;
            }
            @Override
            public void closeSession(StackSession session) {
                // 打印堆栈
                session.saveCase("服务响应-协议解码");
            }
        });
        System.in.read();
    }

    // 处理服务请求
    @Test
    public void handlerRequest() throws IOException {
        //添加监听自动打采集会话
        ServiceConfig<UserService> serviceConfig = buildServiceConfig(-1);
        serviceConfig.export();
        System.out.println("开启服务:"+serviceConfig);
        StackSession.addListener(new StackSessionListener() {
            public boolean isOpen(long classId, String className,String methodFullName) {
                if (!className.equals("org/apache/dubbo/remoting/transport/DecodeHandler")) {
                    return false;
                } else if (!methodFullName.startsWith("received ")) {
                    return false;
                }
                return true;
            }
            @Override
            public void closeSession(StackSession session) {
                // 打印堆栈
                session.saveCase("服务响应-请求处理");
            }
        });
        System.in.read();
    }
    //编码响应结果
    @Test
    public void encodeResponseData() throws IOException {
        //添加监听自动打采集会话
        ServiceConfig<UserService> serviceConfig = buildServiceConfig(-1);
        serviceConfig.export();
        System.out.println("开启服务:"+serviceConfig);
        StackSession.addListener(new StackSessionListener() {
            public boolean isOpen(long classId, String className,String methodFullName) {
                if (!className.equals("org/apache/dubbo/remoting/transport/netty4/NettyServerHandler")) {
                    return false;
                } else if (!methodFullName.startsWith("write ")) {
                    return false;
                }
                return true;
            }
            @Override
            public void closeSession(StackSession session) {
                // 打印堆栈
                session.saveCase("服务响应-协议编码");
            }
        });
        System.in.read();
    }

    public ServiceConfig<UserService> buildServiceConfig(int port) {
        // 服务配置
        ServiceConfig<UserService> serviceConfig = new ServiceConfig();
        // 设置服务接口
        serviceConfig.setInterface(UserService.class);
        // 设置开放的协议
        serviceConfig.setProtocol(new ProtocolConfig("dubbo", port));
        // 设置一个空的注册中心
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        // 设置服务当前所在应用
        serviceConfig.setApplication(new ApplicationConfig("simple-app"));
        // 设置服务实现对象
        UserServiceImpl ref = new UserServiceImpl();
        serviceConfig.setRef(ref);
        serviceConfig.setFilter("testFilter");
        return serviceConfig;
    }

}
