package com.feilong.entity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.*;

@Data
@Accessors(chain = true)
public class User implements Serializable {

  private Integer id;
  private String username;
  private String password;
  private Integer age;
  private String sex;
  private Integer phone;

}
