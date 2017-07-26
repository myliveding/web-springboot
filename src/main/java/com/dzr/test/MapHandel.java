package com.dzr.test;

import com.dzr.po.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author dingzr
 * @Description
 * @ClassName MapHandel
 * @since 2017/5/31 14:41
 */
public class MapHandel {

    public static Map<String,Object> getNum(){
        Map<String,Object> map = new HashMap<>();
        map.put("oneParam", 1);
//        handel(map);
        return map;
    }

    private static void handel(Map<String,Object> map){
        map.put("twoParam",2);
    }

    private static void mapHandel(){
        Map<String, Integer> items = new HashMap<>();
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
        }

        items.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
        items.forEach((k,v)->{
            System.out.println("Item : " + k + " Count : " + v);
            if("E".equals(k)){
                System.out.println("Hello E");
            }
        });
    }

    private static void ergodicSteam(){
        List<User> list = SteamTest.getList(6);
        User user = new User();
        user.setId(1);
        user.setAge(2);
        user.setAddress("addres" + 2);
        list.add(user);

        Map map = list.stream()
                .map(User::getAge)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        map.forEach((k,v)->{
            System.out.println("Item : " + k + " Count : " + v);
            if(v.toString().equals("2")){
                System.out.println("Hello " + k);
            }
        });
    }

    /**
     * 注意：for-each循环在Java 5中被引入所以该方法只能应用于java 5或更高的版本中。
     * 如果你遍历的是一个空的map对象，for-each循环将抛出NullPointerException，
     * 因此在遍历前你总是应该检查空引用。
     */
    private void ergodic1(){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    /**
     * 该方法比entrySet遍历在性能上稍好（快了10%），而且代码更加干净。
     */
    private void ergodic2(){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        //遍历map中的键
        for (Integer key : map.keySet()) {
            System.out.println("Key = " + key);
        }

        //遍历map中的值
        for (Integer value : map.values()) {
            System.out.println("Value = " + value);
        }
    }

    /**
     * 你也可以在keySet和values上应用同样的方法。
     * 该种方式看起来冗余却有其优点所在。
     * 首先，在老版本java中这是惟一遍历map的方式。
     * 另一个好处是，你可以在遍历时调用iterator.remove()来删除entries，另两个方法则不能。
     * 根据javadoc的说明，如果在for-each遍历中尝试使用此方法，结果是不可预测的。
     * 从性能方面看，该方法类同于for-each遍历（即方法二）的性能。
     */
    private void ergodic3(){
        //使用泛型：
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Integer> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        //不使用泛型：
        Map mapNo = new HashMap();
        Iterator entriesNo = mapNo.entrySet().iterator();
        while (entriesNo.hasNext()) {
            Map.Entry entry = (Map.Entry) entriesNo.next();
            Integer key = (Integer)entry.getKey();
            Integer value = (Integer)entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);
        }
    }

    /**
     * 作为方法一的替代，这个代码看上去更加干净；但实际上它相当慢且无效率。
     * 因为从键取值是耗时的操作（与方法一相比，在不同的Map实现中该方法慢了20%~200%）。
     * 如果你安装了FindBugs，它会做出检查并警告你关于哪些是低效率的遍历。所以尽量避免使用。
     */
    private void ergodic4(){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Integer key : map.keySet()) {
            Integer value = map.get(key);
            System.out.println("Key = " + key + ", Value = " + value);
        }
    }

    //如果仅需要键(keys)或值(values)使用方法二。
    // 如果你使用的语言版本低于java 5，或是打算在遍历时删除entries，必须使用方法三。
    // 否则使用方法一(键值都要)。

    public static void main(String[] args) {

    }

}
