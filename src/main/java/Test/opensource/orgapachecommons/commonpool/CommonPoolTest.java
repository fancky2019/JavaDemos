package Test.opensource.orgapachecommons.commonpool;

import Model.Student;
import com.google.common.base.Stopwatch;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 依赖：commons-pool2

 1、适合对象池的场景
对象的创建或销毁成本很高，比如：
数据库连接（JDBC Connection）。
网络连接（HttpClient）。
线程池中的线程。
复杂计算对象或需要大量资源的对象。
对象的生命周期短且需要频繁使用。

2、适合直接创建的场景
对象是轻量级的，创建成本很低（例如，简单的 DTO 类）。
对象生命周期较长或实例需求较少时，池化的开销可能得不偿失。
 */
public class CommonPoolTest {
    GenericObjectPool<ShipOrderInfo> objectPool =null;
    public CommonPoolTest()
    {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(1010);
        config.setMinIdle(1010);
        //默认8个
        config.setMaxTotal(1050);

        objectPool = new GenericObjectPool<>(new ShipOrderInfoBasePooledObjectFactoryImp(), config);

    }
    public void test() throws Exception {
//        fun();
        for(int i=0;i<10;i++)
        {
            costTimePool();
//            costTimeUnPool();
        }

        for(int i=0;i<10;i++)
        {
//            costTimePool();
            costTimeUnPool();
        }

        for(int i=0;i<10;i++)
        {
            costTimePool();
//            costTimeUnPool();
        }
        for(int i=0;i<10;i++)
        {
//            costTimePool();
            costTimeUnPool();
        }
    }

    private void fun() {


        try {
//            GenericObjectPool<Student> objectPool1 = new GenericObjectPool<>();
            DefaultPooledObject defaultPooledObject = new DefaultPooledObject<Student>(new Student());
//            defaultPooledObject.
            GenericObjectPool<Student> objectPool = new GenericObjectPool<>(new PooledObjectFactoryImpl<>(Student.class));
            Student student1 = objectPool.borrowObject();
            student1.setName("fancky1");
            Student student2 = objectPool.borrowObject();
            student2.setName("fancky2");
            objectPool.returnObject(student1);

            //
            Student student3 = objectPool.borrowObject();
            //true
            if (student1 == student3) {
                System.out.println("student1==student3");
            }
            Student student4 = objectPool.borrowObject();


            //当流程走完就归还借用的object
            objectPool.returnObject(student2);

//              ObjectPool.clear();
            objectPool.close();//应用程序退出时候使用,释放所有归还的对象
            int m = 0;

        } catch (Exception ex) {

        }


    }


    public void costTimePool() throws Exception {

        String[] names = {"上海市", "徐汇区", "漕河泾", "闵行区", "中国", "鞋子", "帽子", "太阳", "月亮",
                "初中", "高中", "小学", "大学", "佘山", "浦东区", "陆家嘴", "张江", "北京市", "黄山",
                "复旦", "同济", "海洋", "石油", "乌龟", "王八", "苹果树", "梨树", "电影", "香蕉",
                "小猫", "狼狗", "鸡肉", "牛肉", "金枪鱼",
        };
        int length = names.length;


        ShipOrderInfo shipOrderInfo = null;
        //调用目标方法之前执行，对已有方法进行功能拓展
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (long j = 0; j < 1000; j++) {
            List<ShipOrderInfo> shipOrderInfoList = new ArrayList<>();
            for (long m = 0; m < 1000; m++) {
                if (m == 999) {
                    int md = 0;
                }
                long i = j * 1000 + m;
                long id = i + 1;
                BigDecimal quantity = BigDecimal.valueOf(i);
                shipOrderInfo = objectPool.borrowObject();
//                shipOrderInfo = new ShipOrderInfo();
                shipOrderInfo.setApplyShipOrderId(id);
                shipOrderInfo.setApplyShipOrderCode("ApplyShipOrderCode" + i);
                shipOrderInfo.setApplyShipOrderItemId(id);
                shipOrderInfo.setApplyShipOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setApplyShipOrderItemAllocatedPkgQuantity(quantity);

                shipOrderInfo.setId(id);
                shipOrderInfo.setShipOrderCode("ShipOrderCode" + i);
                shipOrderInfo.setShipOrderItemAllocatedPkgQuantity(quantity);
                shipOrderInfo.setShipOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setShipPickOrderId(id);
                shipOrderInfo.setShipPickOrderItemId(id);
                shipOrderInfo.setShipPickOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setShipOrderItemAllocatedPkgQuantity(quantity);
                shipOrderInfo.setInventoryId(id);
                shipOrderInfo.setInventoryItemId(id);
                shipOrderInfo.setMaterialId(id);
                int index = (int) i % length;
                shipOrderInfo.setMaterialName(names[index]);
                shipOrderInfo.setMaterialCode("material" + i);
                shipOrderInfo.setSerialNo("SerialNo" + i);
                shipOrderInfo.setWorkOrderId(id);
                shipOrderInfo.setLocationId(id);
                shipOrderInfo.setTaskCompletedTime(LocalDateTime.now());

                shipOrderInfo.setWmsTaskId(id);
                shipOrderInfo.setTaskNo("WmsTask" + i);
                shipOrderInfo.setInventoryItemDetailId(id);
                shipOrderInfo.setPallet("Palletcode" + i);
                shipOrderInfo.setMovedPkgQuantity(quantity);
                shipOrderInfo.setMaterialId(id);

                shipOrderInfo.setMaterialProperty1("MaterialProperty1_" + i);
                shipOrderInfo.setMaterialProperty2("MaterialProperty2_" + i);
                shipOrderInfo.setMaterialProperty3("MaterialProperty3_" + i);
                shipOrderInfo.setMaterialProperty4("MaterialProperty4_" + i);
                shipOrderInfo.setMaterialProperty5("MaterialProperty5_" + i);
                shipOrderInfo.setMaterialProperty6("MaterialProperty6_" + i);
                shipOrderInfo.setMaterialProperty7("MaterialProperty7_" + i);
                shipOrderInfo.setMaterialProperty8("MaterialProperty8_" + i);
                shipOrderInfo.setMaterialProperty9("MaterialProperty9_" + i);
                shipOrderInfo.setMaterialProperty10("MaterialProperty10_" + i);
                shipOrderInfo.setMaterialProperty11("MaterialProperty11_" + i);
                shipOrderInfo.setMaterialProperty12("MaterialProperty12_" + i);
                shipOrderInfo.setMaterialProperty13("MaterialProperty13_" + i);
                shipOrderInfo.setMaterialProperty14("MaterialProperty14_" + i);
                shipOrderInfo.setMaterialProperty15("MaterialProperty15_" + i);
                shipOrderInfo.setMaterialProperty16("MaterialProperty16_" + i);
                shipOrderInfo.setMaterialProperty17("MaterialProperty17_" + i);
                shipOrderInfo.setMaterialProperty18("MaterialProperty18_" + i);
                shipOrderInfo.setMaterialProperty19("MaterialProperty19_" + i);
                shipOrderInfo.setMaterialProperty20("MaterialProperty20_" + i);
                shipOrderInfo.setMaterialProperty21("MaterialProperty21_" + i);
                shipOrderInfo.setMaterialProperty22("MaterialProperty22_" + i);
                shipOrderInfo.setMaterialProperty23("MaterialProperty23_" + i);
                shipOrderInfo.setMaterialProperty24("MaterialProperty24_" + i);
                shipOrderInfo.setMaterialProperty25("MaterialProperty25_" + i);
                shipOrderInfo.setMaterialProperty26("MaterialProperty26_" + i);
                shipOrderInfo.setMaterialProperty27("MaterialProperty27_" + i);
                shipOrderInfo.setMaterialProperty28("MaterialProperty28_" + i);
                shipOrderInfo.setMaterialProperty29("MaterialProperty29_" + i);
                shipOrderInfo.setMaterialProperty30("MaterialProperty30_" + i);
                shipOrderInfo.setMaterialProperty31("MaterialProperty31_" + i);
                shipOrderInfo.setMaterialProperty32("MaterialProperty32_" + i);
                shipOrderInfo.setMaterialProperty33("MaterialProperty33_" + i);
                shipOrderInfo.setMaterialProperty34("MaterialProperty34_" + i);
                shipOrderInfo.setMaterialProperty35("MaterialProperty35_" + i);
                shipOrderInfo.setMaterialProperty36("MaterialProperty36_" + i);
                shipOrderInfo.setMaterialProperty37("MaterialProperty37_" + i);
                shipOrderInfo.setMaterialProperty38("MaterialProperty38_" + i);
                shipOrderInfo.setMaterialProperty39("MaterialProperty39_" + i);
                shipOrderInfo.setMaterialProperty40("MaterialProperty40_" + i);
                shipOrderInfo.setMaterialProperty41("MaterialProperty41_" + i);
                shipOrderInfo.setMaterialProperty42("MaterialProperty42_" + i);
                shipOrderInfo.setMaterialProperty43("MaterialProperty43_" + i);
                shipOrderInfo.setMaterialProperty44("MaterialProperty44_" + i);
                shipOrderInfo.setMaterialProperty45("MaterialProperty45_" + i);
                shipOrderInfo.setMaterialProperty46("MaterialProperty46_" + i);
                shipOrderInfo.setMaterialProperty47("MaterialProperty47_" + i);
                shipOrderInfo.setMaterialProperty48("MaterialProperty48_" + i);
                shipOrderInfo.setMaterialProperty49("MaterialProperty49_" + i);
                shipOrderInfo.setMaterialProperty50("MaterialProperty50_" + i);

                shipOrderInfo.setShipOrderItemProperty1("ShipOrderItemProperty1_" + i);
                shipOrderInfo.setShipOrderItemProperty2("ShipOrderItemProperty2_" + i);
                shipOrderInfo.setShipOrderItemProperty3("ShipOrderItemProperty3_" + i);
                shipOrderInfo.setShipOrderItemProperty4("ShipOrderItemProperty4_" + i);
                shipOrderInfo.setShipOrderItemProperty5("ShipOrderItemProperty5_" + i);
                shipOrderInfo.setShipOrderItemProperty6("ShipOrderItemProperty6_" + i);
                shipOrderInfo.setShipOrderItemProperty7("ShipOrderItemProperty7_" + i);
                shipOrderInfo.setShipOrderItemProperty8("ShipOrderItemProperty8_" + i);
                shipOrderInfo.setShipOrderItemProperty9("ShipOrderItemProperty9_" + i);
                shipOrderInfo.setShipOrderItemProperty10("ShipOrderItemProperty10_" + i);
                shipOrderInfo.setShipOrderItemProperty11("ShipOrderItemProperty11_" + i);
                shipOrderInfo.setShipOrderItemProperty12("ShipOrderItemProperty12_" + i);
                shipOrderInfo.setShipOrderItemProperty13("ShipOrderItemProperty13_" + i);
                shipOrderInfo.setShipOrderItemProperty14("ShipOrderItemProperty14_" + i);
                shipOrderInfo.setShipOrderItemProperty15("ShipOrderItemProperty15_" + i);
                shipOrderInfo.setShipOrderItemProperty16("ShipOrderItemProperty16_" + i);
                shipOrderInfo.setShipOrderItemProperty17("ShipOrderItemProperty17_" + i);
                shipOrderInfo.setShipOrderItemProperty18("ShipOrderItemProperty18_" + i);
                shipOrderInfo.setShipOrderItemProperty19("ShipOrderItemProperty19_" + i);
                shipOrderInfo.setShipOrderItemProperty20("ShipOrderItemProperty20_" + i);

                shipOrderInfoList.add(shipOrderInfo);
            }
            for (ShipOrderInfo item : shipOrderInfoList) {
                objectPool.returnObject(item);
            }


        }

        stopwatch.stop();
        //199 milliSeconds:没有重置接着从第一次start()的时候计时
        System.out.println(MessageFormat.format("costTimePool {0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    }
    public void costTimeUnPool() throws Exception {

        String[] names = {"上海市", "徐汇区", "漕河泾", "闵行区", "中国", "鞋子", "帽子", "太阳", "月亮",
                "初中", "高中", "小学", "大学", "佘山", "浦东区", "陆家嘴", "张江", "北京市", "黄山",
                "复旦", "同济", "海洋", "石油", "乌龟", "王八", "苹果树", "梨树", "电影", "香蕉",
                "小猫", "狼狗", "鸡肉", "牛肉", "金枪鱼",
        };
        int length = names.length;

        ShipOrderInfo shipOrderInfo = null;
        //调用目标方法之前执行，对已有方法进行功能拓展
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (long j = 0; j < 1000; j++) {
            List<ShipOrderInfo> shipOrderInfoList = new ArrayList<>();
            for (long m = 0; m < 1000; m++) {
                if (m == 999) {
                    int md = 0;
                }
                long i = j * 1000 + m;
                long id = i + 1;
                BigDecimal quantity = BigDecimal.valueOf(i);

                shipOrderInfo = new ShipOrderInfo();
                shipOrderInfo.setApplyShipOrderId(id);
                shipOrderInfo.setApplyShipOrderCode("ApplyShipOrderCode" + i);
                shipOrderInfo.setApplyShipOrderItemId(id);
                shipOrderInfo.setApplyShipOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setApplyShipOrderItemAllocatedPkgQuantity(quantity);

                shipOrderInfo.setId(id);
                shipOrderInfo.setShipOrderCode("ShipOrderCode" + i);
                shipOrderInfo.setShipOrderItemAllocatedPkgQuantity(quantity);
                shipOrderInfo.setShipOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setShipPickOrderId(id);
                shipOrderInfo.setShipPickOrderItemId(id);
                shipOrderInfo.setShipPickOrderItemRequiredPkgQuantity(quantity);
                shipOrderInfo.setShipOrderItemAllocatedPkgQuantity(quantity);
                shipOrderInfo.setInventoryId(id);
                shipOrderInfo.setInventoryItemId(id);
                shipOrderInfo.setMaterialId(id);
                int index = (int) i % length;
                shipOrderInfo.setMaterialName(names[index]);
                shipOrderInfo.setMaterialCode("material" + i);
                shipOrderInfo.setSerialNo("SerialNo" + i);
                shipOrderInfo.setWorkOrderId(id);
                shipOrderInfo.setLocationId(id);
                shipOrderInfo.setTaskCompletedTime(LocalDateTime.now());

                shipOrderInfo.setWmsTaskId(id);
                shipOrderInfo.setTaskNo("WmsTask" + i);
                shipOrderInfo.setInventoryItemDetailId(id);
                shipOrderInfo.setPallet("Palletcode" + i);
                shipOrderInfo.setMovedPkgQuantity(quantity);
                shipOrderInfo.setMaterialId(id);

                shipOrderInfo.setMaterialProperty1("MaterialProperty1_" + i);
                shipOrderInfo.setMaterialProperty2("MaterialProperty2_" + i);
                shipOrderInfo.setMaterialProperty3("MaterialProperty3_" + i);
                shipOrderInfo.setMaterialProperty4("MaterialProperty4_" + i);
                shipOrderInfo.setMaterialProperty5("MaterialProperty5_" + i);
                shipOrderInfo.setMaterialProperty6("MaterialProperty6_" + i);
                shipOrderInfo.setMaterialProperty7("MaterialProperty7_" + i);
                shipOrderInfo.setMaterialProperty8("MaterialProperty8_" + i);
                shipOrderInfo.setMaterialProperty9("MaterialProperty9_" + i);
                shipOrderInfo.setMaterialProperty10("MaterialProperty10_" + i);
                shipOrderInfo.setMaterialProperty11("MaterialProperty11_" + i);
                shipOrderInfo.setMaterialProperty12("MaterialProperty12_" + i);
                shipOrderInfo.setMaterialProperty13("MaterialProperty13_" + i);
                shipOrderInfo.setMaterialProperty14("MaterialProperty14_" + i);
                shipOrderInfo.setMaterialProperty15("MaterialProperty15_" + i);
                shipOrderInfo.setMaterialProperty16("MaterialProperty16_" + i);
                shipOrderInfo.setMaterialProperty17("MaterialProperty17_" + i);
                shipOrderInfo.setMaterialProperty18("MaterialProperty18_" + i);
                shipOrderInfo.setMaterialProperty19("MaterialProperty19_" + i);
                shipOrderInfo.setMaterialProperty20("MaterialProperty20_" + i);
                shipOrderInfo.setMaterialProperty21("MaterialProperty21_" + i);
                shipOrderInfo.setMaterialProperty22("MaterialProperty22_" + i);
                shipOrderInfo.setMaterialProperty23("MaterialProperty23_" + i);
                shipOrderInfo.setMaterialProperty24("MaterialProperty24_" + i);
                shipOrderInfo.setMaterialProperty25("MaterialProperty25_" + i);
                shipOrderInfo.setMaterialProperty26("MaterialProperty26_" + i);
                shipOrderInfo.setMaterialProperty27("MaterialProperty27_" + i);
                shipOrderInfo.setMaterialProperty28("MaterialProperty28_" + i);
                shipOrderInfo.setMaterialProperty29("MaterialProperty29_" + i);
                shipOrderInfo.setMaterialProperty30("MaterialProperty30_" + i);
                shipOrderInfo.setMaterialProperty31("MaterialProperty31_" + i);
                shipOrderInfo.setMaterialProperty32("MaterialProperty32_" + i);
                shipOrderInfo.setMaterialProperty33("MaterialProperty33_" + i);
                shipOrderInfo.setMaterialProperty34("MaterialProperty34_" + i);
                shipOrderInfo.setMaterialProperty35("MaterialProperty35_" + i);
                shipOrderInfo.setMaterialProperty36("MaterialProperty36_" + i);
                shipOrderInfo.setMaterialProperty37("MaterialProperty37_" + i);
                shipOrderInfo.setMaterialProperty38("MaterialProperty38_" + i);
                shipOrderInfo.setMaterialProperty39("MaterialProperty39_" + i);
                shipOrderInfo.setMaterialProperty40("MaterialProperty40_" + i);
                shipOrderInfo.setMaterialProperty41("MaterialProperty41_" + i);
                shipOrderInfo.setMaterialProperty42("MaterialProperty42_" + i);
                shipOrderInfo.setMaterialProperty43("MaterialProperty43_" + i);
                shipOrderInfo.setMaterialProperty44("MaterialProperty44_" + i);
                shipOrderInfo.setMaterialProperty45("MaterialProperty45_" + i);
                shipOrderInfo.setMaterialProperty46("MaterialProperty46_" + i);
                shipOrderInfo.setMaterialProperty47("MaterialProperty47_" + i);
                shipOrderInfo.setMaterialProperty48("MaterialProperty48_" + i);
                shipOrderInfo.setMaterialProperty49("MaterialProperty49_" + i);
                shipOrderInfo.setMaterialProperty50("MaterialProperty50_" + i);

                shipOrderInfo.setShipOrderItemProperty1("ShipOrderItemProperty1_" + i);
                shipOrderInfo.setShipOrderItemProperty2("ShipOrderItemProperty2_" + i);
                shipOrderInfo.setShipOrderItemProperty3("ShipOrderItemProperty3_" + i);
                shipOrderInfo.setShipOrderItemProperty4("ShipOrderItemProperty4_" + i);
                shipOrderInfo.setShipOrderItemProperty5("ShipOrderItemProperty5_" + i);
                shipOrderInfo.setShipOrderItemProperty6("ShipOrderItemProperty6_" + i);
                shipOrderInfo.setShipOrderItemProperty7("ShipOrderItemProperty7_" + i);
                shipOrderInfo.setShipOrderItemProperty8("ShipOrderItemProperty8_" + i);
                shipOrderInfo.setShipOrderItemProperty9("ShipOrderItemProperty9_" + i);
                shipOrderInfo.setShipOrderItemProperty10("ShipOrderItemProperty10_" + i);
                shipOrderInfo.setShipOrderItemProperty11("ShipOrderItemProperty11_" + i);
                shipOrderInfo.setShipOrderItemProperty12("ShipOrderItemProperty12_" + i);
                shipOrderInfo.setShipOrderItemProperty13("ShipOrderItemProperty13_" + i);
                shipOrderInfo.setShipOrderItemProperty14("ShipOrderItemProperty14_" + i);
                shipOrderInfo.setShipOrderItemProperty15("ShipOrderItemProperty15_" + i);
                shipOrderInfo.setShipOrderItemProperty16("ShipOrderItemProperty16_" + i);
                shipOrderInfo.setShipOrderItemProperty17("ShipOrderItemProperty17_" + i);
                shipOrderInfo.setShipOrderItemProperty18("ShipOrderItemProperty18_" + i);
                shipOrderInfo.setShipOrderItemProperty19("ShipOrderItemProperty19_" + i);
                shipOrderInfo.setShipOrderItemProperty20("ShipOrderItemProperty20_" + i);

                shipOrderInfoList.add(shipOrderInfo);
            }

        }

        stopwatch.stop();
        //199 milliSeconds:没有重置接着从第一次start()的时候计时
        System.out.println(MessageFormat.format("costTimeUnPool {0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    }
}
