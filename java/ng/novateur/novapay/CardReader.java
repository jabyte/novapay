package ng.novateur.novapay;

import android.os.RemoteException;
import wangpos.sdk4.libbasebinder.BankCard;
import wangpos.sdk4.libbasebinder.Core;

import java.util.Arrays;

class CardReader {
    private Core core;
    private BankCard card;
    private String cardInformation;
    private String cardPIN;

    CardReader(Core core, BankCard card) {
        this.core = core;
        this.card = card;
        this.cardPIN = "";
        this.cardInformation = "";

        //cardInformation = readCard();
    }

    static String readCard(Core core, BankCard card) {
        byte[] cardDetails = new byte[512];
        int[] cardDetailsLen = new int[1];

        int status;

        try {
            status = card.readCard(BankCard.CARD_TYPE_NORMAL, BankCard.CARD_MODE_PICC | BankCard.CARD_MODE_ICC | BankCard.CARD_MODE_MAG, 120, cardDetails, cardDetailsLen, "ng.novateur.novatest");
            if (status == 0) {//&& (cardDetails[0] == 0x00) || cardDetails[0] == 0x05 || cardDetails[0] == 0x07)) {
                switch (cardDetails[0]) {
//                    case 0x00:
//                        String cardInfo = convertHexToString(bytesToHexString(cardDetails));
//
//                        cardInfo = cardInfo.replace("^", "-");
//                        cardInfo = cardInfo.split("=")[0];
//                        cardInfo = cardInfo.split("\"")[1];
//                        core.buzzer();
//                        return Arrays.toString(cardDetails);//convertHexToString(bytesToHexString(cardDetails));//cardInfo;
//                        core.buzzer();
//                        break;
                    case 0x05:
                    case 0x07:
                        core.buzzer();
                        return Arrays.toString(cardDetails);
//                        core.buzzer();
//                        break;
                }
            } else {
                return "Error reading card!";
            }
            return "Empty";
        } catch (RemoteException e) {
            return e.getMessage() + "\n" + e.getCause();
        }
    }

    String readCard() {
        byte[] cardDetails = new byte[512];
        int[] cardDetailsLen = new int[1];

        int status;

        try {
            status = card.readCard(BankCard.CARD_TYPE_NORMAL, BankCard.CARD_MODE_PICC | BankCard.CARD_MODE_ICC | BankCard.CARD_MODE_MAG, 120, cardDetails, cardDetailsLen, "ng.novateur.novatest");
            if (status == 0) {//&& (cardDetails[0] == 0x00) || cardDetails[0] == 0x05 || cardDetails[0] == 0x07)) {
                switch (cardDetails[0]) {
                    case 0x00:
//                        String cardInfo = convertHexToString(bytesToHexString(cardDetails));
//
//                        cardInfo = cardInfo.replace("^", "-");
//                        cardInfo = cardInfo.split("=")[0];
//                        cardInfo = cardInfo.split("\"")[1];
                        return cardInformation = Arrays.toString(cardDetails);//convertHexToString(bytesToHexString(cardDetails));//cardInfo;
//                        core.buzzer();
//                        break;
//                    case 0x05:
//                    case 0x07:
//                        this.cardInformation = convertHexToString(Arrays.toString(cardDetails));
//                        this.cardInformation = convertHexToString(cardDetails.toString());
//                        core.buzzer();
//                        break;
                }
            } else {
                return "Error reading card!";
            }
            return cardInformation;
        } catch (RemoteException e) {
            return e.getMessage() + "\n" + e.getCause();
        }
    }

    String getCardInformation() {
        return this.cardInformation;
    }

    String getCardNumber() {
        if (!this.cardInformation.equals("")) {
            return (this.cardInformation.split("-")[0]).substring(1, 17);
        }
        return "";
    }

    String getCardExpiryYear() {
        if (!this.cardInformation.equals("")) {
            String[] temp = this.cardInformation.split("-");
            String cardExpr = temp[temp.length - 1].trim();
            return cardExpr.substring(0, 2).trim();
        }
        return "";
    }

    String getCardExpiryMonth() {
        if (!this.cardInformation.isEmpty()) {
            String[] temp = this.cardInformation.split("-");
            String cardExpr = temp[temp.length - 1].trim();
            return cardExpr.substring(2, 4).trim();
        }
        return "";
    }

    String getCardCVV() {
        if (!this.cardInformation.isEmpty()) {
            String[] temp = this.cardInformation.split("-");
            String cardExpr = temp[temp.length - 1].trim();
            return cardExpr.substring(12, 15).trim();
        }
        return "";
    }

    private String bytesToHexString(byte[] bs) {
        char[] CS = "0123456789ABCDEF".toCharArray();
        char[] cs = new char[bs.length * 2];
        int io = 0;
        byte[] var6 = bs;
        int var5 = bs.length;

        for (int var4 = 0; var4 < var5; ++var4) {
            byte n = var6[var4];
            cs[io++] = CS[n >> 4 & 15];
            cs[io++] = CS[n >> 0 & 15];
        }
        return new String(cs);
    }

    private String convertHexToString(String hex) {

        String ascii = "";
        String str;

        // Convert hex string to "even" length
        int rmd, length;
        length = hex.length();
        rmd = length % 2;
        if (rmd == 1)
            hex = "0" + hex;

        // split into two characters
        for (int i = 0; i < hex.length() - 1; i += 2) {
            //split the hex into pairs
            String pair = hex.substring(i, (i + 2));
            //convert hex to decimal
            int dec = Integer.parseInt(pair, 16);
            str = checkCode(dec);
            ascii = ascii.concat(str);
        }
        return ascii;
    }

    private String checkCode(int dec) {
        //convert the decimal to character
        if (dec < 32 || dec > 126 && dec < 161)
            return "";
        return Character.toString((char) dec);
    }
}