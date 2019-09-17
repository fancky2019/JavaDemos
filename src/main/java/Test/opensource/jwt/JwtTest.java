package Test.opensource.jwt;

import Model.Student;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.DateTimeException;
import java.util.Date;

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

                //设置到期日期，此时验证时候不会判断是否过期
                //.withClaim("exp",expireDate.toString())
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
        return jwtVerifier.verify(token);
    }

    private void decoderToken(String token) {
        // 验证 token
        DecodedJWT decodedJWT = verifier(token);

        Date date=decodedJWT.getExpiresAt();
        //获取Token中自定义信息
        String role = decodedJWT.getClaim("role").asString();
        String name = decodedJWT.getClaim("name").asString();
    }
}
