package com.example.service;

import com.example.constact.RedisKeyConstant;
import com.example.entity.Person;
import com.example.util.RedisManage;
import com.example.util.SerializeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalAccountService {

	@Autowired
	private RedisManage redisManage;

	public String getCurrentAccountId(final String token) {
		Person person = getPeron(token);
		if (person != null) {
			return person.getId();
		}
		return null;
	}

	public String getAccountDisplayName(final String token) {
		Person person = getPeron(token);
		if (person != null) {
			return person.getName();
		}
		return null;
	}

	public Person getPeron(final String token) {
		byte[] bytes = redisManage.getFromMap(RedisKeyConstant.ACCOUNT_MAP_KEY, (RedisKeyConstant.ACCOUNT_MAP_FIELD_PRE + token).getBytes());
		if (bytes != null && bytes.length > 0) {
			return (Person) SerializeUtils.deserialize(bytes);
		}
		return null;
	}

	//	public static String getAccountGardenId() {
	//		String gardenId = (String) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_GARDEN_ID);
	//		if (StringUtils.isBlank(gardenId)) {
	//			return null;
	//		}
	//		return gardenId;
	//	}

	/**
	 * 获取部门名称和部门ID的map，格式为Map<部门ID,部门名称>
	 *
	 * @return
	 */
	//	public static Map<String, String> getDepartmentMap() {
	//		List<Department> departments = getDepartmentService().getDepartmentsByAccountId(getCurrentAccountId());
	//		Map<String, String> map = new HashMap<String, String>();
	//		if (departments == null) {
	//			return null;
	//		}
	//		for (Department department : departments) {
	//			map.put(department.getId(), department.getName());
	//		}
	//		return map;
	//	}

	/**
	 * 获取当前用户所属部门的id数组
	 */
	//	public static String[] getCurrentDepartmentIds() {
	//		Map<String, String> departmentMap = AccountUtils.getDepartmentMap();
	//		String[] result = {};
	//		if (departmentMap != null) {
	//			result = departmentMap.keySet().toArray(result);
	//		}
	//		return result;
	//	}

}
