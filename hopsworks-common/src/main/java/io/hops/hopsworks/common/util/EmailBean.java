/*
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package io.hops.hopsworks.common.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class EmailBean {

  private static final Logger LOG = Logger.getLogger(EmailBean.class.getName());

  @Resource(lookup = "mail/BBCMail")
  private Session mailSession;

  @Asynchronous
  public void sendEmail(String to, Message.RecipientType recipientType,
      String subject, String body) throws
      MessagingException, SendFailedException {

    MimeMessage message = new MimeMessage(mailSession);
    message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
    InternetAddress[] address = {new InternetAddress(to)};
    message.setRecipients(recipientType, address);
    message.setSubject(subject);
    message.setContent(body, "text/html");

    // set the timestamp
    message.setSentDate(new Date());

    message.setText(body);
    try {
      Transport.send(message);
    } catch (MessagingException ex) {
      LOG.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  @Asynchronous
  public void sendEmails(String listAddrs, String subject, String body) throws MessagingException, SendFailedException {

    List<String> addrs = Arrays.asList(listAddrs.split("\\s*,\\s*"));

    boolean first = false;
    MimeMessage message = new MimeMessage(mailSession);
    message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
    message.setSubject(subject);
    message.setContent(body, "text/html");
    message.setSentDate(new Date());
    message.setText(body);
    for (String addr : addrs) {

      InternetAddress[] address = {new InternetAddress(addr)};
      if (first) {
        message.setRecipients(Message.RecipientType.TO, address);
        first = false;
      } else {
        message.setRecipients(Message.RecipientType.CC, address);
      }

    }
    try {
      Transport.send(message);
    } catch (MessagingException ex) {
      LOG.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }
}
