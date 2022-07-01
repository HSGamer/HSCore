package me.hsgamer.hscore.downloader.core.loader;

import me.hsgamer.hscore.downloader.core.object.DownloadInfo;

import java.io.IOException;
import java.io.InputStream;

/**
 * The {@link InputStream} loader for {@link DownloadInfo}
 */
public interface InputStreamLoader {
  /**
   * Load the {@link InputStream} from the {@link DownloadInfo}
   *
   * @param downloadInfo the {@link DownloadInfo}
   *
   * @return the {@link InputStream}
   */
  InputStream load(DownloadInfo downloadInfo) throws IOException;
}
