package com.roomex.assessment.transformation;

import com.roomex.assessment.integration.Language;

import javax.xml.transform.Transformer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

public class PartnerAStyleSheetCompanion implements TransformerDecorator {

    @Override
    public void decorate(Transformer transformer) {
        String msgId = UUID.randomUUID().toString();
        transformer.setParameter("message_id", msgId);

        String nonce = Base64.getEncoder().encodeToString(msgId.getBytes());
        transformer.setParameter("nonce", nonce);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));
        String now = formatter.format(Instant.now());
        transformer.setParameter("created", now);
    }

    // The methods below are all xslt extension functions and are specific to the 'partnera.xslt'

    // Password is Base64(Sha1($Nonce + $Created + Sha1($ClearPassword)))
    public String password(String nonce, String created, String clear) throws NoSuchAlgorithmException {
        String hashedPassword = toSHA1(clear.getBytes());
        String aggregatePassword = nonce.concat(created).concat(hashedPassword);
        String hashedAggregatePassword = toSHA1(aggregatePassword.getBytes());

        return Base64.getEncoder().encodeToString(hashedAggregatePassword.getBytes());
    }

    public String numberOfNights(String start, String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        if(!endDate.isAfter(startDate.plusDays(1L))) {
            System.out.println("invalid date parameters from xml input. stay end must be at least one day after stay start.");
            return "0";
        }

        long elapsedDays = ChronoUnit.DAYS.between(startDate, endDate);
        return String.valueOf(elapsedDays);
    }

    public static String toSHA1(byte[] convertme) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return new String(md.digest(convertme));
    }

    public String languageCode(String primaryLangId) {
        try {
            return Language.getLanguageId(primaryLangId).toString();
        } catch(IllegalArgumentException iae) {
            // If the primaryLangId is not recognised, default to the id supplied
            return primaryLangId;
        }
    }
}
