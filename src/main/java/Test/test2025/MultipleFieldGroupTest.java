package Test.test2025;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultipleFieldGroupTest {

    public void test() {
        List<ZoneMaterialProjectInfo> infoList = new ArrayList<>();
        ZoneMaterialProjectInfo zoneMaterialProjectInfo = new ZoneMaterialProjectInfo();
        zoneMaterialProjectInfo.setId(1);
        zoneMaterialProjectInfo.setZoneCode("1");
        zoneMaterialProjectInfo.setMaterialCode("1");
        zoneMaterialProjectInfo.setProjectNo("1");
        infoList.add(zoneMaterialProjectInfo);
        zoneMaterialProjectInfo = new ZoneMaterialProjectInfo();
        zoneMaterialProjectInfo.setId(2);
        zoneMaterialProjectInfo.setZoneCode("2");
        zoneMaterialProjectInfo.setMaterialCode("2");
        zoneMaterialProjectInfo.setProjectNo("2");
        infoList.add(zoneMaterialProjectInfo);
        Map<ZoneMaterialProject, ZoneMaterialProjectInfo> map = infoList.stream().collect(Collectors.toMap(p -> {
            ZoneMaterialProject zoneMaterialProject = new ZoneMaterialProject();
            zoneMaterialProject.setZoneCode(p.getZoneCode());
            zoneMaterialProject.setMaterialCode(p.getMaterialCode());
            zoneMaterialProject.setProjectNo(p.getProjectNo());
            return zoneMaterialProject;
        }, p -> p));

        Map<ZoneMaterialProject, ZoneMaterialProjectInfo> map1 = infoList.stream().collect(Collectors.toMap(p -> {
            ZoneMaterialProject zoneMaterialProject = new ZoneMaterialProject();
            zoneMaterialProject.setZoneCode(p.getZoneCode());
            zoneMaterialProject.setMaterialCode(p.getMaterialCode());
            zoneMaterialProject.setProjectNo(p.getProjectNo());
            return zoneMaterialProject;
        }, p -> p));


        for (Map.Entry<ZoneMaterialProject, ZoneMaterialProjectInfo> entry : map.entrySet()) {
            ZoneMaterialProjectInfo info = map1.get(entry.getKey());
            int m = 0;
        }

        ZoneMaterialProject zoneMaterialProject2 = new ZoneMaterialProject();
    }
}

/**
 * 随便找一个springboot 项目把jar包内生成的带有@data注解的实体类.class文件拖入idea中可以看到生成后的class
 * @Getter: 为所有非静态字段生成 getter 方法。
 *
 * @Setter: 为所有非 final 的非静态字段生成 setter 方法。
 *
 * @ToString: 生成 toString() 方法。
 *
 * @EqualsAndHashCode: 生成 equals(Object o) 和 hashCode() 方法。
 *
 * RequiredArgsConstructor: 生成一个包含所有 final 字段和带有 @NonNull 注解且未初始化的字段的构造函数。
 *
 *
 *
 *
 * Java 的默认构造函数规则
 * 当一个类没有显式定义任何构造函数时，Java 编译器会自动为该类生成一个默认的无参构造函数（Default Constructor）。
 *@Data 不会移除默认构造函数 - 它只是添加方法，不会删除已有的或默认的构造函数
 *
 * @Data 包含 @RequiredArgsConstructor - 但这只会生成额外的构造函数，不会影响默认构造函数的存在
 *
 *
 *
 */

//@Data
//  @NoArgsConstructor  //      . 无参构造函数 -
//@AllArgsConstructor 全参构造函数 -
//必需参数构造函数 - @RequiredArgsConstructor（已包含在 @Data 中）  @RequiredArgsConstructor	生成包含 final 和 @NonNull 字段的构造函数	✅ 是
//@EqualsAndHashCode// 生成 equals(Object o) 和 hashCode() 方法。
//@EqualsAndHashCode(callSuper = true) // 将父类的字段也纳入 equals 和 hashCode.此处没有父类
class ZoneMaterialProject {
    private String zoneCode;
    public String materialCode;
    public String projectNo;

    //右键 generator
    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getProjectNo() {
        return projectNo;
    }

    /**
     * 对象做key,map 通过对象key取value:先执行hashcode-->如何hashcode相等，然后执行equals
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ZoneMaterialProject that = (ZoneMaterialProject) o;
        return Objects.equals(zoneCode, that.zoneCode) && Objects.equals(materialCode, that.materialCode) && Objects.equals(projectNo, that.projectNo);
    }


    @Override
    public int hashCode() {
        return Objects.hash(zoneCode, materialCode, projectNo);
    }
//    @EqualsAndHashCode.Exclude // 排除此字段，避免循环引用等问题
//private Customer customer;

//    ZoneMaterialProject(String zoneCode)
//    {
//        this.zoneCode=zoneCode;
//    }


//    @NonNull
//    private Long id;
// Lombok 会生成：
// 1. Product() - 来自 @NoArgsConstructor
// 2. Product(Long id, String name, Double price) - 来自 @AllArgsConstructor
// 3. Product(Long id) - 来自 @Data 隐含的 @RequiredArgsConstructor

    //lombox 会生成类似下面的
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ZoneMaterialProject zoneMaterialProject = (ZoneMaterialProject) o;
//        return Objects.equals(zoneCode, zoneMaterialProject.zoneCode) &&
//                Objects.equals(materialCode, zoneMaterialProject.materialCode) &&
//                Objects.equals(projectNo,zoneMaterialProject.projectNo);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(zoneCode, materialCode, projectNo);
//    }
}


@Data
class ZoneMaterialProjectInfo {
    private Integer id;
    private String zoneCode;
    public String materialCode;
    public String projectNo;
}