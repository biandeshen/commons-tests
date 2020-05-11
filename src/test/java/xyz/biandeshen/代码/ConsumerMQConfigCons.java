package xyz.biandeshen.代码;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fjp
 * @Title: ConsumerMQConfig
 * @Description: 消费Mq配置
 * @date 2019/10/10 15:19
 */
@SuppressWarnings("all")
@Component
@ConfigurationProperties(prefix = "MQ.consumer")
public class ConsumerMQConfigCons {
	private String consumerRocketMQAddr;
	private String consumerGroupName;
	
	//private int consumerThreadMax = 32;
	//private int consumerThreadMin = 8;
	private int consumerThreadMax;
	private int consumerThreadMin;
	
	
	/**
	 * 获取
	 *
	 * @return consumerRocketMQAddr
	 */
	public String getConsumerRocketMQAddr() {
		return this.consumerRocketMQAddr;
	}
	
	/**
	 * 设置
	 *
	 * @param consumerRocketMQAddr
	 */
	public void setConsumerRocketMQAddr(String consumerRocketMQAddr) {
		this.consumerRocketMQAddr = consumerRocketMQAddr;
	}
	
	/**
	 * 获取
	 *
	 * @return consumerGroupName
	 */
	public String getConsumerGroupName() {
		return this.consumerGroupName;
	}
	
	/**
	 * 设置
	 *
	 * @param consumerGroupName
	 */
	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}
	
	/**
	 * 获取 private int consumerThreadMax = 32;private int consumerThreadMin = 8;
	 *
	 * @return consumerThreadMax private int consumerThreadMax = 32;private int
	 * consumerThreadMin = 8;
	 */
	public int getConsumerThreadMax() {
		return this.consumerThreadMax;
	}
	
	/**
	 * 设置 private int consumerThreadMax = 32;private int consumerThreadMin = 8;
	 *
	 * @param consumerThreadMax
	 * 		private int consumerThreadMax = 32;private int consumerThreadMin = 8;
	 */
	public void setConsumerThreadMax(int consumerThreadMax) {
		this.consumerThreadMax = consumerThreadMax;
	}
	
	/**
	 * 获取
	 *
	 * @return consumerThreadMin
	 */
	public int getConsumerThreadMin() {
		return this.consumerThreadMin;
	}
	
	/**
	 * 设置
	 *
	 * @param consumerThreadMin
	 */
	public void setConsumerThreadMin(int consumerThreadMin) {
		this.consumerThreadMin = consumerThreadMin;
	}
}
