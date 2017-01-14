package com.example.sungwon.usaepaysandbox;

/**
 * Created by SungWon on 1/12/2017.
 */

public class PaymentLibraryConstants {
    /**
     * User-specific source key. Please replace with the source key created from the Gateway.
     */
    public static String SOURCE_KEY = "_95k469ivih82RKXPv7T9dE44vKHR71Y";


    /**
     * User-specific source pin. Please replace with the source pin created from the Gateway.
     */
    public static String SOURCE_PIN = "p6wfNC";

    /**
     * Determines whether or not to process transactions using the sandbox server. Use sandbox mode
     * for testing. Change to {@code false} when ready for production.
     */
    public static boolean SANDBOX_MODE = true;



    /**
     * String key for sending a {@code long} transaction reference number.
     */
    public static String TRANSACTION_REFNUM = "TRANSACTION_REFNUM";

    /**
     * String key for sending a {@code TransactionRequest} to {@code PaymentLibraryActivity}.
     */
    public static String TRANSACTION_REQUEST = "TRANSACTION_REQUEST";

    /**
     * String key for receiving a {@code TransactionResponse} from {@code PaymentLibraryActivity}
     * after the transaction has been processed.
     */
    public static String TRANSACTION_RESPONSE = "TRANSACTION_RESPONSE";

    /**
     * String key for receiving a {@code GatewayException} from {@code PaymentLibraryActivity} if an
     * error was encountered.
     */
    public static String GATEWAY_EXCEPTION = "GATEWAY_EXCEPTION";


    /**
     * Determine whether or not sanebod mode is enabled.
     * If sandbox mode is enabled, then the transactions will go to the sandbox gateway
     * instead of the production gateway.
     *
     * @return True if Sandbox Mode is enabled
     */
    public static boolean isSandboxMode() {
        return SANDBOX_MODE;
    }

    /**
     * Set sandbox mode
     *
     * @param sandboxMode True if sandbox mode is enabled
     */
    public static void setSandboxMode(boolean sandboxMode) {
        SANDBOX_MODE = sandboxMode;
    }

    /**
     *
     * @return User-specific source key
     */
    public static String getSourceKey() {
        return SOURCE_KEY;
    }
    /**
     * set the User-specific source key
     * @param sourceKey the User-specific source key
     */
    public static void setSourceKey(String sourceKey) {
        SOURCE_KEY = sourceKey;
    }

    /**
     * Get the User-specific source pin
     * @return SOURCE_PIN User-Specific source pin
     */
    public static String getSourcePin() {
        return SOURCE_PIN;
    }

    /**
     * Set the User-specific source pin
     * @param sourcePin User-specific source pin
     */
    public static void setSourcePin(String sourcePin) {
        SOURCE_PIN = sourcePin;
    }
}
