package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author chenmingjun
 * @Date 2017/2/23 16:37
 */
public class Person implements Serializable {

	private String Id;
	private String name;
	private Integer age;
	private String gender;
	private Date birthday;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override public String toString() {
		return "Person{" +
				"Id='" + Id + '\'' +
				", name='" + name + '\'' +
				", age=" + age +
				", gender='" + gender + '\'' +
				", birthday=" + birthday +
				'}';
	}
}
