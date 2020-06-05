package xyz.biandeshen.图灵学院.面试突击;   /**
 * @Title: UserMapper
 * @ProjectName commons-tests
 * @Description: TODO
 * @author fjp
 * @date 2020/5/1323:14
 */

/**
 * @FileName: UserMapper
 * @Author: admin
 * @Date: 2020/5/13 23:14
 * @Description: 用户mapper
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
@Mapper
public interface UserMapper {
	
	@Select("")
	public User query(String id);
}