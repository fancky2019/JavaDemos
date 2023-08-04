package Test.opensource.jwt;

import Model.Student;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.DateTimeException;
import java.util.Date;

/*
 jwt：payload存储用户及过期信息。jwt去中心化。不需要存储，但是严格来说还是要存储。比如用户更改密码了，redis黑名单校验.
 token+redis: token用uuid生成，去掉uuid的“-”，redis存储建立uuid与用户信息的关联。Redis+token依赖redis数据集中共享。
 */

/*
 session:登录成功生成一个sessionId和用户信息关联保存在内存中，sessionId返回给客户端，客户端保存在cookie中，
         客户端每次请求携带sessionId的cookie。如果不用cookie可以通过url或body只要将sessionId传给后端就可以。

        sessionId: 直接使用UUID/GUID即可，如果有验证AuthToken合法性需求，可以将UserName+时间戳加密生成，服务端解密之后验证合法性
 */

/*
String.token组成：format("%s.%s.%s", header, payload, signature);


//签名逻辑
String header = Base64.encodeBase64URLSafeString(this.headerJson.getBytes(StandardCharsets.UTF_8));
String payload = Base64.encodeBase64URLSafeString(this.payloadJson.getBytes(StandardCharsets.UTF_8));
byte[] signatureBytes = this.algorithm.sign(header.getBytes(StandardCharsets.UTF_8), payload.getBytes(StandardCharsets.UTF_8));
String signature = Base64.encodeBase64URLSafeString(signatureBytes);
return String.format("%s.%s.%s", header, payload, signature);

headerJson:{"typ":"JWT","alg":"HS256"}
payloadJson:{"role":"administrator","name":"fancky","exp":1646225607,"age":"27"}
 */

/*
cookie:客户端，有跨域问题
session:服务端
token：指点单点登录，返回客户端token

token  stateless JWT要非常明确的一点：JWT失效的唯一途径就是等待时间过期。
token 验证
设计：1、登录成功将token写入redis(string 类型，key:token,value:任意),以后请求携带token，后端到redis中取token，如果token不存在，则登录，
     否则校验token。
     登出、修改密码等操作，删除redis中的token。
     2、黑名单token 入redis,每次修改、登出将原token入redis黑名单，每次请求判断token在不在黑名单中。黑名单的过期时间，设置比token的过期时间
     要长，但是设计token续签还要维护黑名单过期时间，比较复杂。但是不如第一种运维简单。


 jwt payload 中的jti id:  "jti": "662795c6-b2bc-4cc5-b995-59c802bfe384",
 可以放入redis中作为key区分黑名单




token 续签：
① 类似于 Session 认证中的做法： 假设服务端给的 token 有效期设置为30分钟，服务端每次进行校验时，如果发现 token 的有效期马上快过期了，
服务端就重新生成 token 给客户端。客户端每次请求都检查新旧token，如果不一致，则更新本地的token。这种做法的问题是仅仅在快过期的时候请求才会更新
token ,对客户端不是很友好。每次请求都返回新 token :这种方案的的思路很简单，但是，很明显，开销会比较大。

双token:比单token安全。accessToken有效期短。
② 用户登录返回两个 token ：第一个是 accessToken ，它的过期时间比较短，不如1天；另外一个是 refreshToken 它的过期时间更长一点比如为10天。
客户端登录后，将 accessToken和refreshToken 保存在客户端本地，每次访问将 accessToken 传给服务端。服务端校验 accessToken 的有效性，
如果过期的话，就将 refreshToken 传给服务端。如果 refreshToken 有效，服务端就生成新的 accessToken 给客户端。否则，客户端就重新登录即可。



 */








public class JwtTest {
    private static final String jWTSecretKey = "GQDstcKsx0NHjPOuXOYg5MbeJ1XT0uFiwDVvVBr";

    /**
     * 具体使用参见springbootproject项目JWTController
     */
    public void test() {

        Student student = new Student();
        student.setName("fancky");
        student.setAge(27);

        //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW5pc3RyYXRvciIsIm5hbWUiOiJmYW5ja3kiLCJleHAiOjE1Njg2ODY0MzEsImFnZSI6IjI3In0.nHCpZVCR2aBVJARMKHerJ6iAbTFnuNCuztQA0GypZ-w
        String token = getToken(student);

        decoderToken(token);
    }

    /**
     * 编码：
     * 将model信息生成token
     *
     * @param student
     * @return
     */
    public String getToken(Student student) {
        //60秒过期
        Date expireDate = new Date(System.currentTimeMillis() + 60 * 1000);
        String token = JWT.create()
                //如果设置了到期时间，验证的时候会判断是否到期
                .withExpiresAt(expireDate)
                //
                //私有的Claims,即自定义字段

                //
                //私有的Claims,即自定义字段

                //设置key=exp 等同于设置 .withExpiresAt(expireDate)
//                .withClaim("exp",expireDate)
                //设置到期日期，此时验证时候不会判断是否过期
//                .withClaim("expireDate",expireDate)
                .withClaim("age", student.getAge().toString())
                .withClaim("name", student.getName())
                .withClaim("role", "administrator")
                .sign(Algorithm.HMAC256(jWTSecretKey));
        return token;
    }

    /**
     * 解码：
     * 校验成功返回DecodedJWT对象，否则内部抛出异常。
     *
     * @param token
     * @return
     */
    public DecodedJWT verifier(String token) {
        //SECRET:生成Token的key和解码、验证的key必须一样，否则报下面错误。
        //The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jWTSecretKey)).build();


        /*
           this.algorithm.verify(jwt); 验证的时候会将header和payload根据加密key生成的signature 的base64string 的byte[]和穿过来的
           signature 的base64string 的byte[]是否相等 。
           MessageDigest.isEqual(this.createSignatureFor(algorithm, secretBytes, headerBytes, payloadBytes), signatureBytes);


           有点类似MD5加密，比较加密后的字符串是否相等。
         */
        return jwtVerifier.verify(token);
    }

    private void decoderToken(String token) {
        // 验证 token
        DecodedJWT decodedJWT = verifier(token);

        Date date = decodedJWT.getExpiresAt();
        //获取Token中自定义信息
        String role = decodedJWT.getClaim("role").asString();
        String name = decodedJWT.getClaim("name").asString();

        Date expireDate = decodedJWT.getClaim("expireDate").asDate();
    }
}
