package xyz.biandeshen.dict;

/**
 * @author fjp
 * @Title: TestEnum
 * @ProjectName commons-tests
 * @Description: 参考 TimeUnit 类
 * @date 2019/11/1116:03
 * @see java.util.concurrent.TimeUnit
 */
public enum TestEnum {
	/**
	 *
	 */
	ONE {
		public String toString() {
			return super.toString();
		}
	};
	
	
	/**
	 * Returns the name of this enum constant, as contained in the
	 * declaration.  This method may be overridden, though it typically
	 * isn't necessary or desirable.  An enum type should override this
	 * method when a more "programmer-friendly" string form exists.
	 *
	 * @return the name of this enum constant
	 */
	@Override
	public String toString() {
		return super.toString();
	}}
