package br.com.security.config;

import static redis.clients.jedis.Protocol.DEFAULT_HOST;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

/**
 * The type Redis embedded config.
 *
 * @author Matheus
 */
@Slf4j
@Configuration
public class RedisEmbeddedConfig {

	/**
	 * The constant MAX_NUMBER_PORTS.
	 */
	private static final int MAX_NUMBER_PORTS = 65535;
	/**
	 * The Port.
	 */
	private int port;
	/**
	 * The Redis server.
	 */
	private RedisServer redisServer;

	/**
	 * Connection factory jedis connection factory.
	 *
	 * @return the jedis connection factory
	 * @throws IOException the io exception
	 */
	@Bean
	public JedisConnectionFactory connectionFactory() throws IOException {
		if (this.portAvailable()) {
			log.info("Starting Embedded Redis Server on port {}...", this.port);
			this.redisServer = new RedisServer(this.port);
			this.redisServer.start();
		}
		final JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setPort(this.port);
		return connectionFactory;
	}

	/**
	 * Port available boolean.
	 *
	 * @return the boolean
	 */
	private boolean portAvailable() {
		final Random random = new Random();
		boolean available = false;
		while (!available) {
			this.port = random.nextInt(MAX_NUMBER_PORTS);
			try (final Socket socket = new Socket(DEFAULT_HOST, port)) {
				available = false;
			} catch (final IOException e) {
				available = true;
			}
		}
		return true;
	}

	/**
	 * Destroy.
	 */
	@PreDestroy
	void destroy() {
		log.info("Stoping Embedded Redis Server...");
		this.redisServer.stop();
	}
}
