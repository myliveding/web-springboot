package com.dzr.test;

import com.dzr.po.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * @author dingzr
 * @Description
 * @ClassName JacksonTest
 * @since 2017/5/24 10:29
 */
public class JacksonTest {

    private static void handelMap() throws Exception{
        String jsonString="[{'id':'1'},{'id':'2'}]";
        ObjectMapper mapper = new ObjectMapper();
        List<User> beanList = mapper.readValue(jsonString, new TypeReference<List<User>>(){});
    }

    private static void jsonToType(String socialsetJson){
        try {
            //json解析并进行校验，组装数据
            ObjectMapper mapper = new ObjectMapper();
            //获取社保公积金不同时段政策包的集合
            Map mapInfo = mapper.readValue(socialsetJson, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        User student1 = new User();
        student1.setAddress("五险");
        student1.setId(1);

        User student3 = new User();
        student1.setAddress("一金");
        student1.setId(2);

        ObjectMapper mapper = new ObjectMapper();

        //Convert between List and JSON
        List<User> userList = new ArrayList<User>();
        userList.add(student1);
        userList.add(student3);
        String jsonfromList = mapper.writeValueAsString(userList);
        System.out.println(jsonfromList);
        //List Type is not required here.
        List<User> userList2 = mapper.readValue(jsonfromList, List.class);
        System.out.println(userList2);
        System.out.println("************************************");

        //Convert Map to JSON
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userList", userList);
        map.put("class", "ClassName");
        String jsonfromMap =  mapper.writeValueAsString(map);
        System.out.println(jsonfromMap);

        Map map2 = mapper.readValue(jsonfromMap, Map.class);
        System.out.println(map2);
        System.out.println(map2.get("userList"));
        System.out.println("************************************");

        //Convert Array to JSON
        User[] stuArr = {student1, student3};
        String jsonfromArr =  mapper.writeValueAsString(stuArr);
        System.out.println(jsonfromArr);
        User[] stuArr2 =  mapper.readValue(jsonfromArr, User[].class);
        System.out.println(Arrays.toString(stuArr2) + "---" + stuArr2[0].getId());
    }
}
