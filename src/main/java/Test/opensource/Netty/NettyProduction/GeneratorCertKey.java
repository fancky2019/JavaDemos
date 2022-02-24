package Test.opensource.Netty.NettyProduction;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import sun.reflect.generics.tree.VoidDescriptor;

import javax.net.ssl.SSLException;
import java.io.File;
import java.security.cert.CertificateException;

public class GeneratorCertKey {
    //    File certificate = new File("C:\\Users\\86185\\AppData\\Local\\Temp\\keyutil_localhost_7006298436648660336.crt");
//    File privateKey = new File("C:\\Users\\86185\\AppData\\Local\\Temp\\keyutil_localhost_6063666083091152427.key");
    public static File certificate;
    public static File privateKey;

    public static void Generator() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();

            //X509Certificate 一个证书文件、一个私钥文件
            certificate = ssc.certificate();
            privateKey = ssc.privateKey();
            //SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
