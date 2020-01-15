/**
 * FileName: FileFilter
 * Author:   admin
 * Date:     2020/1/13 15:07
 * Description: 文件过滤
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/13           版本号
 */
package xyz.biandeshen.lambda;   /**
 * @Title: FileFilter
 * @ProjectName commons-tests
 * @Description: TODO
 * @author fjp
 * @date 2020/1/1315:07
 */

import java.io.File;

/**
 * 〈文件过滤〉
 *
 * @author admin
 * @since 1.0.0
 */
@FunctionalInterface
public interface WordFilter {
	boolean filter(String word);
}
