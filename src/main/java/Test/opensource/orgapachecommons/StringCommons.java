package Test.opensource.orgapachecommons;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringCommons {
    public void test() {
        //判断空
        Boolean result = StringUtils.isBlank("   ");//会 trim
        String str = StringUtils.trim("   ");

        //集合拼接逗号
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        String listJoinSeparator = StringUtils.join(list, ',');

        //尾部操作
        String removedStr = StringUtils.removeEnd("123,", ",");
        Boolean endWith = StringUtils.startsWith(",dsdsd", ",");
        Integer m = 0;
    }
}
