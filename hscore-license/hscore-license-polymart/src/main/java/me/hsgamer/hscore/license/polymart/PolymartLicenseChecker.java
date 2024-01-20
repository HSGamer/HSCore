package me.hsgamer.hscore.license.polymart;

import com.google.gson.JsonElement;
import me.hsgamer.hscore.gson.GsonUtils;
import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.common.LicenseStatus;
import me.hsgamer.hscore.web.WebUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The license checker for Polymart
 */
public class PolymartLicenseChecker implements LicenseChecker {
  private final String resource;
  private final boolean isPaid;
  private final boolean isDeepCheck;
  private final PolymartLicenseFetcher fetcher;
  private Consumer<Throwable> exceptionHandler = Throwable::printStackTrace;

  /**
   * Create a new license checker
   *
   * @param resource    the resource id
   * @param isPaid      whether the resource is paid
   * @param isDeepCheck whether the checker should check the license deeply using Polymart API
   * @param fetcher     the license fetcher
   */
  public PolymartLicenseChecker(String resource, boolean isPaid, boolean isDeepCheck, PolymartLicenseFetcher fetcher) {
    this.resource = resource;
    this.isPaid = isPaid;
    this.isDeepCheck = isDeepCheck;
    this.fetcher = fetcher;
  }

  /**
   * Create a new license checker with the default fetcher
   *
   * @param resource    the resource id
   * @param isPaid      whether the resource is paid
   * @param isDeepCheck whether the checker should check the license deeply using Polymart API
   */
  public PolymartLicenseChecker(String resource, boolean isPaid, boolean isDeepCheck) {
    this(resource, isPaid, isDeepCheck, PolymartLicenseFetcher.defaultFetcher());
  }

  /**
   * Check whether the checker can be used
   *
   * @param identifier the identifier
   *
   * @return true if it can be used
   */
  public static boolean isAvailable(String identifier) {
    return "1".equals(identifier);
  }

  /**
   * Check whether the checker can be used
   *
   * @return true if it can be used
   */
  public static boolean isAvailable() {
    return isAvailable("%%__POLYMART__%%");
  }

  /**
   * Set the exception handler
   *
   * @param exceptionHandler the exception handler
   */
  public PolymartLicenseChecker setExceptionHandler(Consumer<Throwable> exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    return this;
  }

  private Optional<Boolean> isDeepValid(PolymartLicenseEntry entry) {
    String url = WebUtils.makeUrl("https://api.polymart.org/v1/verifyPurchase/", entry.toQueryMap());
    try (
      InputStream inputStream = WebUtils.createConnection(url).getInputStream();
      InputStreamReader reader = new InputStreamReader(inputStream)
    ) {
      /*
       * {
       *   "request": "...",
       *   "response": {
       *     "success": true
       *   }
       * }
       */
      JsonElement element = GsonUtils.parse(reader);
      if (!element.isJsonObject()) {
        throw new IOException("Invalid JSON");
      }
      JsonElement response = element.getAsJsonObject().get("response");
      if (!response.isJsonObject()) {
        throw new IOException("Invalid JSON");
      }
      JsonElement successElement = response.getAsJsonObject().get("success");
      if (!successElement.isJsonPrimitive()) {
        throw new IOException("Invalid JSON");
      }
      return Optional.of(successElement.getAsBoolean());
    } catch (Exception e) {
      exceptionHandler.accept(e);
      return Optional.empty();
    }
  }

  @Override
  public LicenseResult checkLicense() {
    PolymartLicenseEntry entry = fetcher.fetchLicense();

    LicenseStatus status;
    if (!entry.isValid(isPaid)) {
      status = LicenseStatus.INVALID;
    } else if (!resource.equals(entry.resource)) {
      status = LicenseStatus.UNKNOWN;
    } else if (isPaid && isDeepCheck) {
      status = isDeepValid(entry)
        .map(isDeepValid -> isDeepValid ? LicenseStatus.VALID : LicenseStatus.INVALID)
        .orElse(LicenseStatus.OFFLINE);
    } else {
      status = LicenseStatus.VALID;
    }

    return new LicenseResult(status, entry.toProperties());
  }
}
