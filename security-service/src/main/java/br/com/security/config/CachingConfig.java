package br.com.security.config;

import static br.com.security.service.CustomClientDetailsService.CLIENT_ID_CACHE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * The type Caching config.
 *
 * @author Matheus
 */
@Configuration
@EnableCaching
public class CachingConfig {

	/**
	 * Cache manager cache manager.
	 *
	 * @param redisTemplate the redis template
	 * @return the cache manager
	 */
	@Bean
	public static CacheManager cacheManager(final RedisTemplate<?, ?> redisTemplate) {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		final Map<String, Long> expiresMap = new HashMap<>();
		expiresMap.put(CLIENT_ID_CACHE, 10L);
		final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setUsePrefix(true);
		cacheManager.setExpires(expiresMap);
		return cacheManager;
	}
}
