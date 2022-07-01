package me.hsgamer.hscore.downloader.webstream;

import me.hsgamer.hscore.downloader.core.loader.InputStreamLoader;
import me.hsgamer.hscore.downloader.core.object.DownloadInfo;
import me.hsgamer.hscore.web.UserAgent;
import me.hsgamer.hscore.web.WebUtils;

import java.io.IOException;
import java.io.InputStream;

public class WebInputStreamLoader implements InputStreamLoader {
  private final UserAgent userAgent;

  public WebInputStreamLoader(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public WebInputStreamLoader() {
    this(UserAgent.FIREFOX);
  }

  @Override
  public InputStream load(DownloadInfo downloadInfo) throws IOException {
    return userAgent.assignToConnection(WebUtils.createConnection(downloadInfo.getDirectLink())).getInputStream();
  }
}
