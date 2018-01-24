package com.yikang.heartmark.common.business.chat;

import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chang on 9/30/2014.
 */
public class SimpleReceiptReceivedListener implements ReceiptReceivedListener {
    private static Logger logger = LoggerFactory.getLogger(SimpleReceiptReceivedListener.class);

    @Override
    public void onReceiptReceived(String fromJid, String toJid, String receiptId) {
        //receiptId is always null??? and can't get packetId, so what used for it????
        logger.debug("onReceiptReceived: fromJid = " + fromJid + ", toJid = " + toJid + ", receiptId = " + receiptId);
    }
}
