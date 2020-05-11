package xyz.biandeshen.代码;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.endpoint.*;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author fjp
 * @Title: ActuatorConfig
 * @ProjectName dj-fjp-baili
 * @Description: Actuator 信息拦截
 * @date 2019/12/1315:57
 */
@Configuration
public class ActuatorConfig {
	/**
	 * 默认启用全部信息拦截
	 */
	private boolean enable = true;
	
	@Bean
	@ConfigurationProperties(prefix = "endpoints.enable")
	public EndpointAutoConfiguration endpointAutoConfiguration(ObjectProvider<HealthAggregator> healthAggregator,
	                                                           ObjectProvider<Map<String, HealthIndicator>> healthIndicators, ObjectProvider<List<InfoContributor>> infoContributors, ObjectProvider<Collection<PublicMetrics>> publicMetrics, ObjectProvider<TraceRepository> traceRepository) {
		if (enable) {
			return new CustomerEndpointAutoConfiguration(healthAggregator, healthIndicators, infoContributors,
			                                             publicMetrics, traceRepository);
		}
		return new EndpointAutoConfiguration(healthAggregator, healthIndicators, infoContributors,
		                                     publicMetrics, traceRepository);
	}
	
	class CustomerEndpointAutoConfiguration extends EndpointAutoConfiguration {
		/**
		 * 默认全部种类信息拦截
		 */
		private boolean enable = false;
		
		public CustomerEndpointAutoConfiguration(ObjectProvider<HealthAggregator> healthAggregator,
		                                         ObjectProvider<Map<String, HealthIndicator>> healthIndicators,
		                                         ObjectProvider<List<InfoContributor>> infoContributors,
		                                         ObjectProvider<Collection<PublicMetrics>> publicMetrics,
		                                         ObjectProvider<TraceRepository> traceRepository) {
			super(healthAggregator, healthIndicators, infoContributors, publicMetrics, traceRepository);
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.env")
		public EnvironmentEndpoint environmentEndpoint() {
			EnvironmentEndpoint environmentEndpoint = super.environmentEndpoint();
			environmentEndpoint.setEnabled(enable);
			return environmentEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.health")
		public HealthEndpoint healthEndpoint() {
			HealthEndpoint healthEndpoint = super.healthEndpoint();
			healthEndpoint.setEnabled(enable);
			return healthEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.beans")
		public BeansEndpoint beansEndpoint() {
			BeansEndpoint beansEndpoint = super.beansEndpoint();
			beansEndpoint.setEnabled(enable);
			return beansEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.info")
		public InfoEndpoint infoEndpoint() throws Exception {
			InfoEndpoint infoEndpoint = super.infoEndpoint();
			infoEndpoint.setEnabled(enable);
			return infoEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.metrics")
		public MetricsEndpoint metricsEndpoint() {
			MetricsEndpoint metricsEndpoint = super.metricsEndpoint();
			metricsEndpoint.setEnabled(enable);
			return metricsEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.trace")
		public TraceEndpoint traceEndpoint() {
			TraceEndpoint traceEndpoint = super.traceEndpoint();
			traceEndpoint.setEnabled(enable);
			return traceEndpoint;
		}
		
		@Override
		@Bean
		@ConfigurationProperties(prefix = "endpoints.dump")
		public DumpEndpoint dumpEndpoint() {
			DumpEndpoint dumpEndpoint = super.dumpEndpoint();
			dumpEndpoint.setEnabled(enable);
			return dumpEndpoint;
		}
		
		@Override
		@Bean
		@ConditionalOnBean({LoggingSystem.class})
		@ConfigurationProperties(prefix = "endpoints.loggers")
		public LoggersEndpoint loggersEndpoint(LoggingSystem loggingSystem) {
			return super.loggersEndpoint(loggingSystem);
		}
	}
}
