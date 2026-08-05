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
				log.warn("Desktop.browse failed: {}", e.getMessage());
			}

			try {
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					try {
						Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
						log.info("Browser opened via rundll32: {}", url);
					} catch (Exception e2) {
						log.warn("rundll32 failed, trying cmd start: {}", e2.getMessage());
						Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
						log.info("Browser opened via cmd start: {}", url);
					}
				} else if (os.contains("mac")) {
					Runtime.getRuntime().exec(new String[]{"open", url});
					log.info("Browser opened via open: {}", url);
				} else {
					Runtime.getRuntime().exec(new String[]{"xdg-open", url});
					log.info("Browser opened via xdg-open: {}", url);
				}
			} catch (Exception e) {
				log.error("Failed to open browser automatically. Please open {} manually. Error: {}", url, e.getMessage());
			}
		}, "browser-launcher").start();
	}
}
