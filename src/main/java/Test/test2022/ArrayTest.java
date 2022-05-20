package Test.test2022;

import java.util.Arrays;
import java.util.List;

public class ArrayTest {
    public void test() {
        fun();
        mergeSortedArray();
        sort();
    }

    private void fun() {
        int[] array = new int[2];
        array[0] = 0;
        array[1] = 1;
        int[] array1 = array;
        int[] array2 = new int[2];
        array2[0] = 0;
        array2[1] = 11;
        array1 = array2;
        int m = 0;
    }

    private void mergeSortedArray() {
        int[] a = new int[]{3, 6, 7, 9};
        int[] b = {1, 2, 5, 7, 8};
        int mergedLen = a.length + b.length;
        int[] merged = new int[mergedLen];
        int lenA = a.length - 1;
        int lenB = b.length - 1;
        int lenM = mergedLen - 1;
        while (lenA >= 0 && lenB >= 0) {
            //从后往前插入，从大到小插入
            merged[lenM--] = a[lenA] >= b[lenB] ? a[lenA--] : b[lenB--];
        }
        //a没有插完的继续插入
        while (lenA >= 0) {
            merged[lenM--] = a[lenA--];
        }
        //b没有插完的继续插入
        while (lenB >= 0) {
            merged[lenM--] = b[lenB--];
        }
        int m = 0;
    }


    private void sort() {
        int[] a = new int[]{3, 6, 7, 9, 5, 2, 8};

        //选择排序
        //从头部每次冒一个最大值或最小值在
        for(int i=0;i<a.length-1;i++)//获取头部待比较数据
        {
            for(int j=i+1;j<a.length;j++)//需要比较的数据
            {
                //从大到小
//                if(a[i]<a[j])
//                {
//                    int temp=a[i];
//                    a[i]=a[j];
//                    a[j]=temp;
//                }
               //从小到大
                if(a[i]>a[j])
                {
                    int temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }


        //冒泡排序 Bubble Sort
        int[] b = new int[]{3, 6, 7, 9, 5, 2, 8};

        //从尾部每次冒一个最大值或最小值
        for(int i=0;i<b.length-1;i++)   //循环次数
        {
            //已经有序i个数据，所以只需要排b.length-i-1次
            //要和j+1位置比较，所以最大值是b.length-i-1
            for(int j=0;j<b.length-i-1;j++)//内部数据比较。当前索引位置和索引+1位置比较
            {
                //从大到小
//                if(a[i]<a[j])
//                {
//                    int temp=a[i];
//                    a[i]=a[j];
//                    a[j]=temp;
//                }
                //从小到大
                if(b[j]>b[j+1])
                {
                    int temp=b[j];
                    b[j]=b[j+1];
                    b[j+1]=temp;
                }
            }
        }

        System.out.println(Arrays.toString(b));






        Integer[] array= new Integer[]{3, 6, 7, 9, 5, 2, 8};
        List<Integer> list=Arrays.asList(array);
        System.out.println(list);
        System.out.println(Arrays.toString(a));
        int m=0;
    }
}
