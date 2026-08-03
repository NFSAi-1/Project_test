package com.example.talent_explorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger log = LoggerFactory.getLogger(BrowserLauncher.class);

	@Value("${app.browser.url:http://localhost:8081}")
	private String url;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
			try {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI(url));
					log.info("Browser opened via Desktop API: {}", url);
					return;
				}
			} catch (Exception e) {
				log.warn("Desktop.browse failed, trying Runtime.exec fallback: {}", e.getMessage());
			}

			try {
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
				} else if (os.contains("mac")) {
					Runtime.getRuntime().exec(new String[]{"open", url});
				} else {
					Runtime.getRuntime().exec(new String[]{"xdg-open", url});
				}
				log.info("Browser opened via Runtime.exec: {}", url);
			} catch (Exception e) {
				log.error("Failed to open browser: {}", e.getMessage());
			}
		}, "browser-launcher").start();
	}
}
