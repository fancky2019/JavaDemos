package Test.test2021.designpattern;

/**
 *
 * 装饰器：装饰器类和原始实现类都要实现接口， 装饰器类对原始类进行包装，构造函数引入待装饰类。
 *
 *
 * 对原对象功能的拓展
 * 接口功能的拓展：有点类似代理环绕增强、但是代理只能固定增强，装饰者可以通过派生类拓展。
 *
 *
 *  代理模式："控制访问"（隐藏对象，增强非业务逻辑）。
 *  装饰器模式："增强功能"（透明扩展，支持嵌套组合）。
 *  两者选择取决于需求：需控制权限或隐藏细节时用代理；需动态扩展功能时用装饰器
 *
 * 装饰器是为了增强，代理是为了控制
 *使用装饰器模式当：
 * 需要动态添加多个可选功能
 * 功能可以自由组合和叠加
 * 想要保持接口透明，客户端无感知
 *
 * 使用代理模式当：
 * 需要控制对象访问（权限、延迟加载）
 * 功能是固定的，不需要组合
 * 想要隐藏真实对象的复杂性
 *
 *
 *
 *
 * 装饰器模式使用接口实现，不使用继承
 *
 *
 * 装饰器模式关注功能增强，要求装饰器和被装饰对象实现同一接口；适配器模式关注接口兼容，将一个接口转换成客户端期望的另一个接口
 *
 * 实现方式：都是内部构造函数方式包装要装饰适配的实现类对象
 * 装饰器同一个接口的不同实现，适配器不要求同一个接口（包装类内调用要适配的接口就行）
 *
 *
 */
public class DecoratorPattern {

//    /*  配置bean
//    @Configuration
//public class ServiceConfig {
//
//    /**
//     * 配置装饰器模式：将UserServiceDecorator作为主要的UserService实现
//     * 它内部包装了原始的UserServiceImpl
//     */
//    @Bean
//    @Primary  // 标记为主要实现，自动注入时会使用这个
//    public UserService userServiceDecorator(@Qualifier("userServiceImpl") UserService userService) {
//        return new UserServiceDecorator(userService);
//    }
//
//    /**
//     * 如果需要直接访问原始Service，可以保留这个Bean
//     */
//    @Bean
//    public UserService userServiceImpl() {
//        return new UserServiceImpl();
//    }
//}
//     */


    /*
    调用
        // 自动注入的是被装饰过的Service（因为配置了@Primary）
    @Autowired
    private UserService userService;

    // 如果需要访问装饰器特有的方法，可以注入装饰器本身
    @Autowired
    private UserServiceDecorator userServiceDecorator;

    // 如果需要访问原始Service，可以使用@Qualifier
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService rawUserService;

     */


    /* 接口
//    public interface UserService {
//
//        /**
//         * 根据ID查询用户
//         */
//        User getUserById(Long id);
//    }
//    */



    /* 原始实现类
    @Slf4j
@Service
public class UserServiceImpl implements UserService {

    // 模拟数据库
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // 初始化测试数据
        User user1 = new User(idGenerator.getAndIncrement(), "张三", "zhangsan@example.com", 25);
        User user2 = new User(idGenerator.getAndIncrement(), "李四", "lisi@example.com", 30);
        userDatabase.put(user1.getId(), user1);
        userDatabase.put(user2.getId(), user2);
        log.info("初始化测试数据完成，当前用户数：{}", userDatabase.size());
    }

    @Override
    public User getUserById(Long id) {
        log.info("【原始Service】正在查询用户，ID：{}", id);
        // 模拟耗时操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return userDatabase.get(id);
    }


     */


    /* 装饰器类：装饰实现类
    @Slf4j
@Service
public class UserServiceDecorator implements UserService {

    // 被装饰的原始Service
    private final UserService userService;

    // 统计信息
    private long totalExecutionTime = 0;
    private long callCount = 0;

    public UserServiceDecorator(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
        log.info("创建UserService装饰器");
    }

    @Override
    public User getUserById(Long id) {
        long startTime = System.currentTimeMillis();
        log.info("========== 装饰器前置操作 ==========");
        log.info("开始执行getUserById方法，参数：id={}", id);

        // 前置验证
        if (id == null || id <= 0) {
            log.error("参数验证失败：id不能为空且必须大于0");
            throw new IllegalArgumentException("无效的用户ID");
        }

        // 检查缓存（模拟）
        log.info("检查缓存中是否存在用户：{}", id);

        User result = null;
        try {
            // 调用原始方法
            result = userService.getUserById(id);

            // 后置处理
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            // 更新统计信息
            updateStatistics(executionTime);

            log.info("方法执行完成，耗时：{}ms，结果：{}", executionTime, result);

            // 缓存结果（模拟）
            if (result != null) {
                log.info("将用户{}加入缓存", id);
            }

            // 记录操作日志
            log.info("记录操作日志：查询用户 {}", id);

        } catch (Exception e) {
            log.error("方法执行异常：{}", e.getMessage());
            throw e;
        } finally {
            log.info("========== 装饰器后置操作 ==========");
        }

        return result;
    }
     */
}
